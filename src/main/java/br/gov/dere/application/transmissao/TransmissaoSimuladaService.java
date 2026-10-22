package br.gov.dere.application.transmissao;

import br.gov.dere.application.access.AccessService;
import br.gov.dere.application.certificado.CertificadoService;
import br.gov.dere.application.csv.DereCsvConverter;
import br.gov.dere.application.d1001.D1001ImportService;
import br.gov.dere.application.periodico.D1101CsvConverter;
import br.gov.dere.application.periodico.D1199CsvConverter;
import br.gov.dere.application.pgcc.D1011CsvConverter;
import br.gov.dere.application.configuracao.ConfiguracaoIntegracaoService;
import br.gov.dere.application.validacao.ServicoValidacaoLeiaute;
import br.gov.dere.application.validacao.relatorio.Critica;
import br.gov.dere.application.validacao.relatorio.RelatorioValidacao;
import br.gov.dere.application.validacao.relatorio.TipoCritica;
import br.gov.dere.domain.EventStatus;
import br.gov.dere.domain.transmissao.ResultadoConsulta;
import br.gov.dere.integration.receita.ExcecaoTransmissaoGoverno;
import br.gov.dere.integration.signature.DereXmlSigner;
import br.gov.dere.integration.signature.DereXmlSignatureVerifier;
import br.gov.dere.integration.signature.CertificadoTesteLocal;
import br.gov.dere.integration.xml.DereBatchBuilder;
import br.gov.dere.persistence.DereBatchEntity;
import br.gov.dere.persistence.DereBatchRepository;
import br.gov.dere.persistence.DereEventEntity;
import br.gov.dere.persistence.DereEventErrorRepository;
import br.gov.dere.persistence.DereEventRepository;
import br.gov.dere.persistence.DereTentativaTransmissaoEntity;
import br.gov.dere.persistence.DereTentativaTransmissaoRepository;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class TransmissaoSimuladaService {
  private static final DateTimeFormatter PROTOCOLO = DateTimeFormatter.ofPattern("yyyyMMddHHmmss").withZone(ZoneOffset.UTC);
  private final ConfiguracaoIntegracaoService integration;
  private final TransmissaoGovernoPort government;
  private final AccessService access;
  private final ServicoValidacaoLeiaute validacao;
  private final D1001ImportService importD1001;
  private final CertificadoService certificates;
  private final DereEventRepository events;
  private final DereEventErrorRepository eventErrors;
  private final DereBatchRepository batches;
  private final DereTentativaTransmissaoRepository attempts;
  private final ArmazenamentoArtefatoTransmissao artefatos;
  private final TransacaoIsolada transacao;
  private final ConstrutorXmlRetornoOficial retornos = new ConstrutorXmlRetornoOficial();
  private final DereXmlSigner signer = new DereXmlSigner();
  private final DereXmlSignatureVerifier verifier = new DereXmlSignatureVerifier();
  private final DereCsvConverter csvD1001 = new DereCsvConverter();
  private final D1011CsvConverter csvD1011 = new D1011CsvConverter();
  private final D1101CsvConverter csvD1101 = new D1101CsvConverter();
  private final D1199CsvConverter csvD1199 = new D1199CsvConverter();

  public TransmissaoSimuladaService(
      ConfiguracaoIntegracaoService integration,
      TransmissaoGovernoPort government,
      AccessService access,
      ServicoValidacaoLeiaute validacao,
      D1001ImportService importD1001,
      CertificadoService certificates,
      DereEventRepository events,
      DereEventErrorRepository eventErrors,
      DereBatchRepository batches,
      DereTentativaTransmissaoRepository attempts,
      ArmazenamentoArtefatoTransmissao artefatos,
      TransacaoIsolada transacao) {
    this.integration = integration;
    this.government = government;
    this.access = access;
    this.validacao = validacao;
    this.importD1001 = importD1001;
    this.certificates = certificates;
    this.events = events;
    this.eventErrors = eventErrors;
    this.batches = batches;
    this.attempts = attempts;
    this.artefatos = artefatos;
    this.transacao = transacao;
  }

  public ResultadoEnvioSimulado simulate(Long userId, Long entityId, RequisicaoEnvioSimulado request) {
    exigirMock();
    access.assertAccess(userId, entityId);
    var cnpj = access.entity(entityId).getCnpjRoot();
    var leiaute = normalizarLeiaute(request.layout());
    var origem = request.origem() == null ? "CSV" : request.origem().trim().toUpperCase();
    var arquivo = request.arquivo() == null || request.arquivo().isBlank() ? leiaute.toLowerCase() + ".dat" : request.arquivo();
    var relatorio = "XML".equals(origem)
        ? validacao.validarXml(leiaute, request.conteudo(), arquivo, cnpj, entityId)
        : validacao.validarCsv(leiaute, request.conteudo(), arquivo, cnpj, entityId);
    if (!relatorio.valido()) return new ResultadoEnvioSimulado(false, relatorio, null);
    var xml = relatorio.xml();
    var csv = relatorio.csv();
    var identificador = identificador(leiaute, xml);
    var jaEnviado = events.findByEventIdentifier(identificador);
    if (jaEnviado.isPresent()) {
      var existente = jaEnviado.get();
      var loteExistente = existente.getBatchId() == null ? null : batches.findById(existente.getBatchId()).orElse(null);
      var problema = existente.getStatus() == EventStatus.RESULTADO_DESCONHECIDO
          ? "O envio anterior deste id não teve confirmação da Receita. O lote pode ter sido recebido. Não reenvie este id; concilie pelo protocolo, se a Receita o tiver emitido."
          : "Já existe transmissão com este id. O lote anterior está em Transmissões. Para alteração ou exclusão use outro id (tpOper 2 ou 3).";
      return new ResultadoEnvioSimulado(false, comCritica(relatorio, arquivo, "id", identificador,
          "Identificador de evento ainda não transmitido", problema),
          loteExistente == null ? visao(null, existente) : visao(loteExistente, existente));
    }
    var cfg = integration.current();
    SigningKey chave;
    String assinado;
    String loteXml;
    try {
      chave = carregarChave(entityId);
      assinado = signer.sign(xml, chave.keyStore(), chave.password(), chave.alias());
      if (!verifier.verify(assinado, chave.keyStore(), chave.alias())) throw new IllegalStateException("Assinatura XMLDSig inválida");
      loteXml = new DereBatchBuilder().build(cnpj, List.of(assinado));
    } catch (ResponseStatusException ex) {
      throw ex;
    } catch (Exception ex) {
      throw falhaLocal(ex, identificador);
    }
    var preparado = transacao.executar(() -> persistirEnvio(userId, entityId, cfg.environment(), cfg.schemaVersion(), cfg.realAdapterEnabled(), leiaute, identificador, arquivo, xml, csv, assinado, loteXml));
    try {
      if (cfg.realAdapterEnabled()) {
        var envio = government.submit(loteXml, identificador);
        if (envio.protocol() == null || envio.protocol().isBlank()) {
          throw new ExcecaoTransmissaoGoverno(502, true, envio.responseBody());
        }
        var recepcao = envio.responseBody() == null ? "" : envio.responseBody();
        return transacao.executar(() -> concluirEnvio(preparado, envio.protocol(), recepcao, envio.httpStatus(), false, leiaute, userId, entityId, arquivo, xml, relatorio));
      }
      var agora = Instant.now();
      var protocolo = novoProtocolo();
      var recepcao = assinar(retornos.recepcao(cnpj, cnpj, protocolo, agora), chave);
      return transacao.executar(() -> concluirEnvio(preparado, protocolo, recepcao, 200, true, leiaute, userId, entityId, arquivo, xml, relatorio));
    } catch (ExcecaoTransmissaoGoverno ex) {
      var definitiva = recusaDefinitiva(ex);
      if (definitiva) transacao.executar(() -> rejeitarEnvio(preparado, ex));
      throw definitiva ? falhaGoverno(ex, identificador) : falhaDesconhecido(ex, identificador);
    } catch (ResponseStatusException ex) {
      throw ex;
    } catch (Exception ex) {
      throw falhaLocal(ex, identificador);
    }
  }

  public ConsultaProtocolo consultarProtocolo(Long userId, Long entityId, String protocolo) {
    access.assertAccess(userId, entityId);
    var numero = protocolo == null ? "" : protocolo.trim();
    if (numero.isBlank()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Informe o protocolo");
    var batch = batches.findByProtocol(numero)
        .filter(item -> entityId.equals(item.getEntityId()))
        .orElse(null);
    if (protocoloSimulado(numero)) {
      if (batch == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Este protocolo é de envio simulado e não está nesta entidade.");
      var local = visao(batch, eventoDoLote(batch.getId()));
      return new ConsultaProtocolo(local.protocol(), local.status(), local.receiptNumber(), null, local.returnXml(), local,
          "Este protocolo foi gerado no envio simulado. A Receita Federal não consulta esse número.");
    }
    var cfg = integration.current();
    if (!cfg.realAdapterEnabled()) {
      if (batch == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Protocolo não encontrado no envio simulado");
      var evento = eventoDoLote(batch.getId());
      var local = visao(batch, evento);
      return new ConsultaProtocolo(local.protocol(), local.status(), local.receiptNumber(), null, local.returnXml(), local, null);
    }
    try {
      var consulta = government.query(numero);
      if (batch == null) return consultaSemLote(numero, consulta);
      return transacao.executar(() -> gravarConsultaProtocolo(batch.getId(), numero, consulta));
    } catch (ExcecaoTransmissaoGoverno ex) {
      throw falhaConsulta(numero, ex);
    } catch (ResponseStatusException ex) {
      throw ex;
    } catch (Exception ex) {
      throw falhaLocal(ex, null);
    }
  }

  public VisaoTransmissao query(Long userId, Long entityId, Long batchId) {
    exigirMock();
    access.assertAccess(userId, entityId);
    var batch = batches.findById(batchId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Lote não encontrado"));
    if (batch.getEntityId() != null && !batch.getEntityId().equals(entityId)) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Lote de outra entidade");
    }
    var evento = eventoDoLote(batchId);
    if (reciboFinal(evento)) return visao(batch, evento);
    if (batch.getProtocol() == null || batch.getProtocol().isBlank()) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, "O lote pode ter sido recebido pela Receita, mas o protocolo não foi confirmado. Não reenvie este identificador. Concilie pelo protocolo, se a Receita o tiver emitido.");
    }
    try {
      if (integration.current().realAdapterEnabled()) {
        var consulta = government.query(batch.getProtocol());
        return transacao.executar(() -> gravarConsultaReal(batchId, consulta));
      }
      var simulada = prepararConsultaSimulada(entityId, batch, evento);
      return transacao.executar(() -> gravarConsultaSimulada(batchId, simulada.recibo(), simulada.xml()));
    } catch (ResponseStatusException ex) {
      throw ex;
    } catch (Exception ex) {
      throw falhaLocal(ex, evento.getEventIdentifier());
    }
  }

  public List<VisaoTransmissao> listar(Long userId, Long entityId) {
    access.assertAccess(userId, entityId);
    return events.findByEntityIdOrderByIdDesc(entityId).stream()
        .map(evento -> visao(evento.getBatchId() == null ? null : batches.findById(evento.getBatchId()).orElse(null), evento).resumida())
        .toList();
  }

  public VisaoTransmissao detalhe(Long userId, Long entityId, Long batchId) {
    access.assertAccess(userId, entityId);
    var batch = batches.findById(batchId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Lote não encontrado"));
    if (batch.getEntityId() != null && !batch.getEntityId().equals(entityId)) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Lote de outra entidade");
    }
    return visao(batch, eventoDoLote(batchId));
  }

  @Transactional
  public void excluir(Long userId, Long entityId, Long batchId) {
    access.assertAccess(userId, entityId);
    if (!access.user(userId).isMaster()) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Apenas usuário master pode excluir transmissão");
    }
    var batch = batches.findById(batchId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Lote não encontrado"));
    if (batch.getEntityId() != null && !batch.getEntityId().equals(entityId)) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Lote de outra entidade");
    }
    var eventos = events.findByBatchId(batchId);
    for (var evento : eventos) {
      attempts.deleteByEventId(evento.getId());
      eventErrors.deleteByEventId(evento.getId());
      events.delete(evento);
    }
    batches.delete(batch);
  }

  private Preparacao persistirEnvio(
      Long userId,
      Long entityId,
      String environment,
      String schemaVersion,
      boolean adaptadorReal,
      String leiaute,
      String identificador,
      String arquivo,
      String xml,
      String csv,
      String assinado,
      String loteXml) {
    var evento = new DereEventEntity(leiaute, identificador, environment, schemaVersion, null);
    evento.ownedBy(userId, entityId);
    evento.source(arquivo, null, !adaptadorReal);
    evento.operation(operacao(leiaute, xml));
    evento = events.saveAndFlush(evento);
    evento.source(arquivo, artefatos.guardar(identificador, "csv", csv), !adaptadorReal);
    events.saveAndFlush(evento);
    evento.unsigned(artefatos.guardar(identificador, "evento", xml));
    events.saveAndFlush(evento);
    evento.signed(artefatos.guardar(identificador, "assinado", assinado));
    evento.resultadoDesconhecido();
    events.saveAndFlush(evento);
    var batch = new DereBatchEntity("<DeRE/>");
    batch.ownedBy(userId, entityId);
    batch = batches.saveAndFlush(batch);
    batch.request(artefatos.guardar(identificador, "lote", loteXml));
    batch.resultadoDesconhecido();
    batches.saveAndFlush(batch);
    evento.attachBatch(batch.getId());
    events.saveAndFlush(evento);
    return new Preparacao(evento.getId(), batch.getId());
  }

  private ResultadoEnvioSimulado concluirEnvio(
      Preparacao preparado,
      String protocolo,
      String recepcao,
      int httpStatus,
      boolean mock,
      String leiaute,
      Long userId,
      Long entityId,
      String arquivo,
      String xml,
      RelatorioValidacao relatorio) {
    var evento = events.findById(preparado.eventId()).orElseThrow();
    var batch = batches.findById(preparado.batchId()).orElseThrow();
    registrarTentativa(evento.getId(), "SUBMIT", protocolo, recepcao, httpStatus, mock);
    batch.received(protocolo, artefatos.guardar(evento.getEventIdentifier(), "recepcao", recepcao));
    batches.saveAndFlush(batch);
    evento.sent();
    events.saveAndFlush(evento);
    if ("D-1001".equals(leiaute)) importD1001.persistValidatedXml(userId, entityId, arquivo, xml);
    return new ResultadoEnvioSimulado(true, relatorio, visao(batch, evento));
  }

  private Void rejeitarEnvio(Preparacao preparado, ExcecaoTransmissaoGoverno ex) {
    var evento = events.findById(preparado.eventId()).orElseThrow();
    var batch = batches.findById(preparado.batchId()).orElseThrow();
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

  private boolean reciboFinal(DereEventEntity evento) {
    var recibo = evento.getReceiptNumber();
    if (recibo == null || recibo.isBlank()) return false;
    return evento.isSimulated() || !integration.current().realAdapterEnabled() || !Recibos.falso(recibo);
  }

  private static boolean recusaDefinitiva(ExcecaoTransmissaoGoverno ex) {
    var http = ex.statusCode();
    return http == 400 || http == 401 || http == 403 || http == 404;
  }

  private ConsultaProtocolo consultaSemLote(String numero, ResultadoConsulta consulta) {
    var retorno = consulta.responseBody() == null ? "" : consulta.responseBody();
    return new ConsultaProtocolo(numero, statusReceita(consulta).name(), reciboDe(consulta), consulta.codigoGoverno(), retorno, null, null);
  }

  private ConsultaProtocolo gravarConsultaProtocolo(Long batchId, String numero, ResultadoConsulta consulta) {
    var batch = batches.findById(batchId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Lote não encontrado"));
    var evento = eventoDoLote(batchId);
    aplicarRetorno(batch, evento, consulta, numero);
    return new ConsultaProtocolo(numero, statusReceita(consulta).name(), reciboDe(consulta), consulta.codigoGoverno(),
        consulta.responseBody() == null ? "" : consulta.responseBody(), visao(batch, evento), null);
  }

  private VisaoTransmissao gravarConsultaReal(Long batchId, ResultadoConsulta consulta) {
    var batch = batches.findById(batchId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Lote não encontrado"));
    var evento = eventoDoLote(batchId);
    aplicarRetorno(batch, evento, consulta, batch.getProtocol());
    return visao(batch, evento);
  }

  private void aplicarRetorno(DereBatchEntity batch, DereEventEntity evento, ResultadoConsulta consulta, String protocolo) {
    var retorno = consulta.responseBody() == null ? "" : consulta.responseBody();
    var status = statusReceita(consulta);
    batch.completed(status, artefatos.guardar(evento.getEventIdentifier(), "retorno", retorno));
    batches.save(batch);
    if (status != EventStatus.PROCESSING) {
      evento.processed(status, reciboDe(consulta));
      events.save(evento);
    }
    registrarTentativa(evento.getId(), "QUERY", protocolo, retorno, consulta.httpStatus(), false);
  }

  private ConsultaSimulada prepararConsultaSimulada(Long entityId, DereBatchEntity batch, DereEventEntity evento) throws Exception {
    var chave = carregarChave(entityId);
    var agora = Instant.now();
    var recebido = batch.getSentAt() == null ? agora : batch.getSentAt();
    var xmlEvento = artefatos.ler(evento.getXmlUnsigned());
    var periodo = periodo(evento.getEventType(), xmlEvento);
    var recibo = novoRecibo(batch.getId());
    var hash = hash(xmlEvento);
    var entidade = access.entity(entityId);
    var tabela = retornos.eventoTabela(evento.getEventIdentifier(), entidade.getCnpjRoot(),
        evento.getEventType(), batch.getProtocol(), recibo, hash, recebido, agora, periodo[0], periodo[1]);
    var tabelaAssinada = assinar(tabela, chave);
    var consulta = assinar(retornos.consulta(entidade.getCnpjRoot(), entidade.getCnpjRoot(),
        batch.getProtocol(), recebido, agora, tabelaAssinada), chave);
    return new ConsultaSimulada(recibo, consulta);
  }

  private VisaoTransmissao gravarConsultaSimulada(Long batchId, String recibo, String consulta) {
    var batch = batches.findById(batchId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Lote não encontrado"));
    var evento = eventoDoLote(batchId);
    batch.completed(EventStatus.ACCEPTED, artefatos.guardar(evento.getEventIdentifier(), "retorno", consulta));
    batches.save(batch);
    evento.processed(EventStatus.ACCEPTED, recibo);
    events.save(evento);
    registrarTentativa(evento.getId(), "QUERY", batch.getProtocol(), consulta, 200, true);
    return visao(batch, evento);
  }

  private static String reciboDe(ResultadoConsulta consulta) {
    return consulta.receipt() == null || consulta.receipt().isBlank() ? null : consulta.receipt();
  }

  private static EventStatus statusReceita(ResultadoConsulta consulta) {
    return switch (consulta.state()) {
      case ACCEPTED, ACCEPTED_WITH_WARNINGS -> EventStatus.ACCEPTED;
      case REJECTED -> EventStatus.REJECTED;
      default -> EventStatus.PROCESSING;
    };
  }

  private record ConsultaSimulada(String recibo, String xml) {}

  private DereEventEntity eventoDoLote(Long batchId) {
    return events.findByBatchId(batchId).stream()
        .max(Comparator.comparing(DereEventEntity::getId))
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Evento do lote não encontrado"));
  }

  private static String statusDe(DereBatchEntity batch, DereEventEntity evento) {
    if (batch != null && batch.getStatus() != null) return batch.getStatus().name();
    return evento.getStatus() == null ? null : evento.getStatus().name();
  }

  private VisaoTransmissao visao(DereBatchEntity batch, DereEventEntity evento) {
    return new VisaoTransmissao(
        batch == null ? null : batch.getId(),
        evento.getId(),
        evento.getEventType(),
        evento.getSourceName(),
        evento.getOperationType(),
        evento.getEventIdentifier(),
        batch == null ? null : batch.getProtocol(),
        evento.getReceiptNumber(),
        statusDe(batch, evento),
        evento.isSimulated(),
        evento.getEnvironment(),
        evento.getCreatedAt(),
        evento.getSentAt(),
        evento.getProcessedAt(),
        artefatos.ler(evento.getCsvSource()),
        artefatos.ler(evento.getXmlUnsigned()),
        batch == null ? null : artefatos.ler(batch.getRequestXml()),
        batch == null ? null : artefatos.ler(batch.getResponseXml()));
  }

  private SigningKey carregarChave(Long entityId) throws Exception {
    var disponiveis = certificates.list(entityId);
    if (disponiveis.isEmpty()) {
      if (integration.current().realAdapterEnabled()) {
        throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Cadastre o certificado A1 da entidade antes de enviar.");
      }
      return new SigningKey(CertificadoTesteLocal.create(), "changeit".toCharArray(), "dere-test");
    }
    var carregado = certificates.loadForSigning(disponiveis.get(0).id());
    return new SigningKey(carregado.keyStore(), carregado.password(), carregado.alias());
  }

  private String assinar(String xml, SigningKey chave) throws Exception {
    var assinado = signer.sign(xml, chave.keyStore(), chave.password(), chave.alias());
    if (!verifier.verify(assinado, chave.keyStore(), chave.alias())) throw new IllegalStateException("Assinatura XMLDSig inválida");
    return assinado;
  }

  private void registrarTentativa(Long eventId, String operacao, String protocolo, String retorno, int httpStatus, boolean mock) {
    var proxima = attempts.findTopByEventIdAndOperationOrderByAttemptDesc(eventId, operacao)
        .map(item -> (item.getAttempt() == null ? 0 : item.getAttempt()) + 1)
        .orElse(1);
    var tentativa = new DereTentativaTransmissaoEntity(eventId, operacao, proxima, UUID.randomUUID().toString());
    var prefixo = mock ? "mock://" : "receita://";
    tentativa.complete("SUCCESS", httpStatus, mock ? "MOCK_ACCEPTED" : "GOV_ACCEPTED", 0,
        prefixo + operacao.toLowerCase() + "/" + protocolo, prefixo + "retorno/" + protocolo, null);
    attempts.save(tentativa);
  }

  private void exigirMock() {
    var cfg = integration.current();
    if (cfg.realAdapterEnabled()) return;
    if (!cfg.mockEnabled()) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, "Envio simulado desabilitado. Ative o mock ou o adaptador real em Configurações.");
    }
  }

  private static String normalizarLeiaute(String layout) {
    if (layout == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Leiaute obrigatório");
    var valor = layout.trim().toUpperCase();
    if (!"D-1001".equals(valor) && !"D-1011".equals(valor) && !"D-1101".equals(valor) && !"D-1199".equals(valor)) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Leiaute não suportado no envio simulado");
    }
    return valor;
  }

  private String identificador(String leiaute, String xml) {
    try {
      if ("D-1011".equals(leiaute)) return csvD1011.fromXml(xml).id();
      if ("D-1101".equals(leiaute)) return csvD1101.fromXml(xml).id();
      if ("D-1199".equals(leiaute)) return csvD1199.fromXml(xml).id();
      return csvD1001.fromXml(xml).id();
    } catch (Exception ex) {
      throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Não foi possível ler o id do evento", ex);
    }
  }

  private String operacao(String leiaute, String xml) {
    try {
      if ("D-1011".equals(leiaute)) return String.valueOf(csvD1011.fromXml(xml).operation());
      if ("D-1101".equals(leiaute)) return String.valueOf(csvD1101.fromXml(xml).operation());
      if ("D-1199".equals(leiaute)) return String.valueOf(csvD1199.fromXml(xml).operation());
      return String.valueOf(csvD1001.fromXml(xml).operation());
    } catch (Exception ex) {
      return null;
    }
  }

  private LocalDate[] periodo(String leiaute, String xml) {
    try {
      if ("D-1011".equals(leiaute)) {
        var modelo = csvD1011.fromXml(xml);
        return new LocalDate[] { modelo.validFrom(), modelo.validTo() };
      }
      if ("D-1101".equals(leiaute)) {
        return new LocalDate[] { inicioPeriodo(csvD1101.fromXml(xml).period()), null };
      }
      if ("D-1199".equals(leiaute)) {
        return new LocalDate[] { inicioPeriodo(csvD1199.fromXml(xml).period()), null };
      }
      var modelo = csvD1001.fromXml(xml);
      return new LocalDate[] { modelo.validFrom(), modelo.validTo() };
    } catch (Exception ex) {
      return new LocalDate[] { LocalDate.now(ZoneOffset.UTC), null };
    }
  }

  private static LocalDate inicioPeriodo(String perApur) {
    if (perApur == null || perApur.length() < 7) return LocalDate.now(ZoneOffset.UTC);
    return LocalDate.parse(perApur + "-01");
  }

  private RelatorioValidacao comCritica(RelatorioValidacao base, String arquivo, String coluna, String valor, String esperado, String problema) {
    var criticas = new ArrayList<>(base.criticas());
    criticas.add(new Critica(arquivo, 1, coluna, valor, TipoCritica.NEGOCIO, esperado, problema, "/DeRE"));
    return RelatorioValidacao.de(base.leiaute(), base.origem(), base.arquivo(), base.totalLinhas(), criticas)
        .comArtefatos(base.xml(), base.csv(), base.relatorioXlsx());
  }

  private static boolean protocoloSimulado(String protocolo) {
    return protocolo.matches("1\\.\\d{14}\\.\\d{6}");
  }

  private static FalhaTransmissaoException falhaDesconhecido(ExcecaoTransmissaoGoverno ex, String identificador) {
    var diagnostico = ApresentacaoErroTransmissao.resultadoDesconhecido(ex, identificador);
    ApresentacaoErroTransmissao.registrar(diagnostico, ex.responseBody());
    return new FalhaTransmissaoException(diagnostico, ex);
  }

  private static FalhaTransmissaoException falhaGoverno(ExcecaoTransmissaoGoverno ex, String identificador) {
    var diagnostico = ApresentacaoErroTransmissao.deGoverno(ex, identificador);
    ApresentacaoErroTransmissao.registrar(diagnostico, ex.responseBody());
    return new FalhaTransmissaoException(diagnostico, ex);
  }

  private static FalhaTransmissaoException falhaConsulta(String protocolo, ExcecaoTransmissaoGoverno ex) {
    var diagnostico = ApresentacaoErroTransmissao.deConsulta(protocolo, ex);
    ApresentacaoErroTransmissao.registrar(diagnostico, ex.responseBody());
    return new FalhaTransmissaoException(diagnostico, ex);
  }

  private static FalhaTransmissaoException falhaLocal(Exception ex, String identificador) {
    var diagnostico = ApresentacaoErroTransmissao.deFalhaLocal(ex, identificador);
    ApresentacaoErroTransmissao.registrar(diagnostico, null);
    return new FalhaTransmissaoException(diagnostico, ex);
  }

  private static String novoProtocolo() {
    return "1." + PROTOCOLO.format(Instant.now()) + "." + String.format("%06d", ThreadLocalRandom.current().nextInt(1_000_000));
  }

  private static String novoRecibo(Long batchId) {
    return "1.1." + PROTOCOLO.format(Instant.now()) + String.format("%05d", batchId == null ? 0 : batchId % 100000);
  }

  private static String hash(String xml) throws Exception {
    var digest = MessageDigest.getInstance("SHA-256").digest((xml == null ? "" : xml).getBytes(StandardCharsets.UTF_8));
    return Base64.getEncoder().encodeToString(digest);
  }

  private record SigningKey(java.security.KeyStore keyStore, char[] password, String alias) {}
  private record Preparacao(Long eventId, Long batchId) {}
}
