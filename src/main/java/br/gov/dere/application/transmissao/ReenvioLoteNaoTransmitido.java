package br.gov.dere.application.transmissao;

import br.gov.dere.application.access.AccessService;
import br.gov.dere.application.configuracao.ConfiguracaoIntegracaoService;
import br.gov.dere.application.d1001.D1001ImportService;
import br.gov.dere.application.receita.SituacaoReceitaService;
import br.gov.dere.domain.EventStatus;
import br.gov.dere.domain.transmissao.ResultadoEnvio;
import br.gov.dere.integration.receita.ExcecaoTransmissaoGoverno;
import br.gov.dere.persistence.DereBatchEntity;
import br.gov.dere.persistence.DereBatchRepository;
import br.gov.dere.persistence.DereEventEntity;
import br.gov.dere.persistence.DereEventRepository;
import br.gov.dere.persistence.DereTentativaTransmissaoEntity;
import br.gov.dere.persistence.DereTentativaTransmissaoRepository;
import java.time.Instant;
import java.util.Locale;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ReenvioLoteNaoTransmitido {
  private static final Logger log = LoggerFactory.getLogger(ReenvioLoteNaoTransmitido.class);
  private static final long RESERVA_SEGUNDOS = 120;
  private static final long ESPERA_SEGUNDOS = 60;

  private final DereBatchRepository batches;
  private final DereEventRepository events;
  private final DereTentativaTransmissaoRepository attempts;
  private final ConfiguracaoIntegracaoService integracao;
  private final SituacaoReceitaService situacao;
  private final TransmissaoGovernoPort government;
  private final ArmazenamentoArtefatoTransmissao artefatos;
  private final D1001ImportService importD1001;
  private final AccessService access;
  private final TransacaoIsolada transacao;

  public ReenvioLoteNaoTransmitido(
      DereBatchRepository batches,
      DereEventRepository events,
      DereTentativaTransmissaoRepository attempts,
      ConfiguracaoIntegracaoService integracao,
      SituacaoReceitaService situacao,
      @Qualifier("httpTransmissaoGovernoAdapter") TransmissaoGovernoPort government,
      ArmazenamentoArtefatoTransmissao artefatos,
      D1001ImportService importD1001,
      AccessService access,
      TransacaoIsolada transacao) {
    this.batches = batches;
    this.events = events;
    this.attempts = attempts;
    this.integracao = integracao;
    this.situacao = situacao;
    this.government = government;
    this.artefatos = artefatos;
    this.importD1001 = importD1001;
    this.access = access;
    this.transacao = transacao;
  }

  public String reenviarSolicitado(Long userId, Long entityId, Long batchId) {
    access.assertAccess(userId, entityId);
    if (!integracao.current().realAdapterEnabled()) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, "O reenvio real está desligado neste ambiente.");
    }
    var recusa = transacao.executar(() -> motivoRecusa(entityId, batchId));
    if ("ausente".equals(recusa)) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lote não encontrado");
    if ("outra".equals(recusa)) throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Lote de outra entidade");
    if (recusa != null) throw new ResponseStatusException(HttpStatus.CONFLICT, textoRecusa(recusa));
    if (!situacao.atual().envioLiberado()) {
      transacao.executar(() -> colocarNaFila(batchId));
      return "A Receita ainda não entregou o token. O lote ficou aguardando e será enviado com o mesmo identificador quando ela voltar.";
    }
    Reserva reserva;
    try {
      reserva = transacao.executar(() -> reservarManual(batchId));
    } catch (RuntimeException ex) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, "Não foi possível reservar o lote para reenvio.");
    }
    if (reserva == null) throw new ResponseStatusException(HttpStatus.CONFLICT, "O lote já está em envio ou não tem o XML gravado.");
    try {
      var resultado = government.submit(reserva.loteXml(), reserva.identificador());
      if (resultado == null || resultado.protocol() == null || resultado.protocol().isBlank()) {
        var corpo = resultado == null ? "" : resultado.responseBody();
        transacao.executar(() -> encerrarDesconhecido(batchId, corpo));
        return "O envio saiu, mas a Receita não devolveu protocolo. Este identificador não será repetido sozinho.";
      }
      transacao.executar(() -> concluir(batchId, resultado));
      gravarCadastro(reserva, batchId);
      return "Lote reenviado com o mesmo identificador. O protocolo foi registrado.";
    } catch (EnvioNaoIniciadoException ex) {
      situacao.registrarIndisponivel();
      transacao.executar(() -> adiar(batchId));
      return "O lote ainda não saiu. Ele será enviado com o mesmo identificador quando a Receita voltar.";
    } catch (ExcecaoTransmissaoGoverno ex) {
      if (ex.statusCode() == 429) situacao.registrarIndisponivel();
      if (duplicidade(ex.responseBody())) {
        transacao.executar(() -> encerrarDesconhecido(batchId, ex.responseBody()));
        return "A Receita indicou que este identificador já existe. O lote fica para conciliação e não será enviado de novo.";
      }
      if (recusaDefinitiva(ex)) {
        transacao.executar(() -> rejeitar(batchId, ex));
        return "A Receita recusou este identificador. O lote não será enviado de novo.";
      }
      transacao.executar(() -> encerrarDesconhecido(batchId, ex.responseBody()));
      return "O envio saiu, mas o protocolo não voltou. Este identificador não será repetido sozinho.";
    } catch (RuntimeException ex) {
      transacao.executar(() -> encerrarDesconhecido(batchId, ex.getMessage()));
      return "O envio saiu, mas o protocolo não voltou. Este identificador não será repetido sozinho.";
    }
  }

  @Scheduled(fixedDelayString = "${dere.transmission.poll-delay-ms:30000}")
  public void reenviarPendentes() {
    if (!integracao.current().realAdapterEnabled()) return;
    if (!situacao.atual().envioLiberado()) return;
    var agora = Instant.now();
    for (var lote : batches.findTop20ByStatusAndNextQueryAtLessThanEqualOrderByNextQueryAtAsc(EventStatus.AGUARDANDO_ENVIO, agora)) {
      try {
        reenviar(lote.getId());
      } catch (RuntimeException ex) {
        log.warn("reenvio do lote {} não concluído: {}", lote.getId(), ex.toString());
      }
    }
  }

  private void reenviar(Long batchId) {
    Reserva reserva;
    try {
      reserva = transacao.executar(() -> reservar(batchId));
    } catch (RuntimeException ex) {
      return;
    }
    if (reserva == null) return;
    ResultadoEnvio resultado;
    try {
      resultado = government.submit(reserva.loteXml(), reserva.identificador());
    } catch (EnvioNaoIniciadoException ex) {
      situacao.registrarIndisponivel();
      transacao.executar(() -> adiar(batchId));
      return;
    } catch (ExcecaoTransmissaoGoverno ex) {
      if (ex.statusCode() == 429) situacao.registrarIndisponivel();
      if (duplicidade(ex.responseBody())) transacao.executar(() -> encerrarDesconhecido(batchId, ex.responseBody()));
      else if (recusaDefinitiva(ex)) transacao.executar(() -> rejeitar(batchId, ex));
      else transacao.executar(() -> encerrarDesconhecido(batchId, ex.responseBody()));
      return;
    } catch (RuntimeException ex) {
      transacao.executar(() -> encerrarDesconhecido(batchId, ex.getMessage()));
      return;
    }
    if (resultado == null || resultado.protocol() == null || resultado.protocol().isBlank()) {
      var corpo = resultado == null ? "" : resultado.responseBody();
      transacao.executar(() -> encerrarDesconhecido(batchId, corpo));
      return;
    }
    transacao.executar(() -> concluir(batchId, resultado));
    if (!"D-1001".equals(reserva.leiaute())) return;
    gravarCadastro(reserva, batchId);
  }

  private void gravarCadastro(Reserva reserva, Long batchId) {
    if (!"D-1001".equals(reserva.leiaute())) return;
    try {
      importD1001.persistValidatedXml(reserva.userId(), reserva.entityId(), reserva.arquivo(), reserva.xmlEvento());
    } catch (RuntimeException ex) {
      log.warn("lote {} aceito na recepção sem gravar o cadastro local: {}", batchId, ex.toString());
    }
  }

  private Reserva reservarManual(Long batchId) {
    var batch = batches.findById(batchId).orElse(null);
    var evento = evento(batchId);
    if (batch == null || evento == null) return null;
    if (batch.getStatus() == EventStatus.RESULTADO_DESCONHECIDO) {
      evento.aguardandoEnvio();
      batch.aguardandoEnvio();
      events.save(evento);
      batches.save(batch);
    }
    return reservar(batchId);
  }

  private Void colocarNaFila(Long batchId) {
    var batch = batches.findById(batchId).orElse(null);
    var evento = evento(batchId);
    if (batch == null || evento == null) return null;
    if (batch.getProtocol() != null && !batch.getProtocol().isBlank()) return null;
    if (batch.getStatus() != EventStatus.RESULTADO_DESCONHECIDO && batch.getStatus() != EventStatus.AGUARDANDO_ENVIO) return null;
    evento.aguardandoEnvio();
    batch.aguardandoEnvio();
    events.save(evento);
    batches.save(batch);
    return null;
  }

  private String motivoRecusa(Long entityId, Long batchId) {
    var batch = batches.findById(batchId).orElse(null);
    var evento = evento(batchId);
    if (batch == null || evento == null) return "ausente";
    if (batch.getEntityId() != null && !batch.getEntityId().equals(entityId)) return "outra";
    if (evento.getEntityId() != null && !evento.getEntityId().equals(entityId)) return "outra";
    if (evento.isSimulated()) return "simulado";
    if (batch.getProtocol() != null && !batch.getProtocol().isBlank()) return "protocolo";
    if (batch.getStatus() != EventStatus.RESULTADO_DESCONHECIDO && batch.getStatus() != EventStatus.AGUARDANDO_ENVIO) return "status";
    var xml = artefatos.ler(batch.getRequestXml());
    if (xml == null || xml.isBlank()) return "xml";
    return null;
  }

  private static String textoRecusa(String codigo) {
    return switch (codigo) {
      case "simulado" -> "Lote simulado não é reenviado à Receita.";
      case "protocolo" -> "Este lote já tem protocolo. A consulta segue sozinha.";
      case "xml" -> "O XML assinado deste lote não está gravado.";
      default -> "Este lote não pode ser reenviado.";
    };
  }

  private Reserva reservar(Long batchId) {
    var batch = batches.findById(batchId).orElse(null);
    var evento = evento(batchId);
    if (batch == null || evento == null) return null;
    if (batch.getProtocol() != null && !batch.getProtocol().isBlank()) {
      if (batch.getStatus() == EventStatus.AGUARDANDO_ENVIO) {
        evento.sent();
        batch.received(batch.getProtocol());
        events.save(evento);
        batches.save(batch);
      }
      return null;
    }
    if (evento.isSimulated()) {
      encerrar(batch, evento, null);
      return null;
    }
    if (!batch.claimReenvio(Instant.now(), RESERVA_SEGUNDOS)) return null;
    var loteXml = artefatos.ler(batch.getRequestXml());
    if (loteXml == null || loteXml.isBlank()) {
      encerrar(batch, evento, null);
      return null;
    }
    batches.saveAndFlush(batch);
    return new Reserva(loteXml, evento.getEventIdentifier(), evento.getEventType(), evento.getUserId(), evento.getEntityId(),
        evento.getSourceName(), artefatos.ler(evento.getXmlUnsigned()));
  }

  private Void adiar(Long batchId) {
    var batch = batches.findById(batchId).orElse(null);
    if (batch == null) return null;
    batch.adiarReenvio(ESPERA_SEGUNDOS);
    batches.save(batch);
    return null;
  }

  private Void concluir(Long batchId, ResultadoEnvio resultado) {
    var batch = batches.findById(batchId).orElse(null);
    var evento = evento(batchId);
    if (batch == null || evento == null || batch.getStatus() != EventStatus.AGUARDANDO_ENVIO) return null;
    var corpo = resultado.responseBody() == null ? "" : resultado.responseBody();
    registrarTentativa(evento.getId(), resultado.protocol(), corpo, resultado.httpStatus());
    batch.received(resultado.protocol(), artefatos.guardar(evento.getEventIdentifier(), "recepcao", corpo));
    batches.save(batch);
    evento.sent();
    events.save(evento);
    log.info("lote {} reenviado, protocolo {}", batchId, resultado.protocol());
    return null;
  }

  private Void rejeitar(Long batchId, ExcecaoTransmissaoGoverno ex) {
    var batch = batches.findById(batchId).orElse(null);
    var evento = evento(batchId);
    if (batch == null || evento == null || batch.getStatus() != EventStatus.AGUARDANDO_ENVIO) return null;
    var corpo = ex.responseBody() == null ? "" : ex.responseBody();
    batch.completed(EventStatus.REJECTED, artefatos.guardar(evento.getEventIdentifier(), "retorno", corpo));
    batches.save(batch);
    evento.processed(EventStatus.REJECTED, null);
    events.save(evento);
    var proxima = attempts.findTopByEventIdAndOperationOrderByAttemptDesc(evento.getId(), "SUBMIT")
        .map(item -> (item.getAttempt() == null ? 0 : item.getAttempt()) + 1)
        .orElse(1);
    var tentativa = new DereTentativaTransmissaoEntity(evento.getId(), "SUBMIT", proxima, UUID.randomUUID().toString());
    tentativa.complete("FAILURE", ex.statusCode(), "GOV_REJECTED", 0, "receita://submit/", "receita://retorno/", null);
    attempts.save(tentativa);
    return null;
  }

  private Void encerrarDesconhecido(Long batchId, String corpo) {
    var batch = batches.findById(batchId).orElse(null);
    var evento = evento(batchId);
    if (batch == null || evento == null) return null;
    encerrar(batch, evento, corpo);
    return null;
  }

  private void encerrar(DereBatchEntity batch, DereEventEntity evento, String corpo) {
    if (batch.getStatus() != EventStatus.AGUARDANDO_ENVIO) return;
    var gravado = corpo == null || corpo.isBlank() ? null : artefatos.guardar(evento.getEventIdentifier(), "retorno", corpo);
    batch.resultadoDesconhecido(gravado);
    evento.resultadoDesconhecido();
    batches.save(batch);
    events.save(evento);
  }

  private void registrarTentativa(Long eventId, String protocolo, String retorno, int httpStatus) {
    var proxima = attempts.findTopByEventIdAndOperationOrderByAttemptDesc(eventId, "SUBMIT")
        .map(item -> (item.getAttempt() == null ? 0 : item.getAttempt()) + 1)
        .orElse(1);
    var tentativa = new DereTentativaTransmissaoEntity(eventId, "SUBMIT", proxima, UUID.randomUUID().toString());
    tentativa.complete("SUCCESS", httpStatus, "GOV_ACCEPTED", 0, "receita://submit/" + protocolo, "receita://retorno/" + protocolo, null);
    attempts.save(tentativa);
  }

  private DereEventEntity evento(Long batchId) {
    return events.findByBatchId(batchId).stream().findFirst().orElse(null);
  }

  private static boolean recusaDefinitiva(ExcecaoTransmissaoGoverno ex) {
    var http = ex.statusCode();
    return http == 400 || http == 401 || http == 403 || http == 404;
  }

  private static boolean duplicidade(String corpo) {
    return corpo != null && corpo.toLowerCase(Locale.ROOT).contains("duplic");
  }

  private record Reserva(String loteXml, String identificador, String leiaute, Long userId, Long entityId, String arquivo, String xmlEvento) {}
}
