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
      ArmazenamentoArtefatoTransmissao artefatos) {
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
  }

  @Transactional
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
      return new ResultadoEnvioSimulado(false, comCritica(relatorio, arquivo, "id", identificador,
          "Identificador de evento ainda não transmitido",
          "Já existe transmissão com este id. O lote anterior está em Transmissões. Para alteração ou exclusão use outro id (tpOper 2 ou 3)."),
          loteExistente == null ? visao(null, existente) : visao(loteExistente, existente));
    }
    try {
      var cfg = integration.current();
      var chave = carregarChave(entityId);
      var assinado = signer.sign(xml, chave.keyStore(), chave.password(), chave.alias());
      if (!verifier.verify(assinado, chave.keyStore(), chave.alias())) throw new IllegalStateException("Assinatura XMLDSig inválida");
      var lote = new DereBatchBuilder().build(cnpj, List.of(assinado));
      var evento = new DereEventEntity(leiaute, identificador, cfg.environment(), cfg.schemaVersion(), null);
      evento.ownedBy(userId, entityId);
      evento.source(arquivo, null, !cfg.realAdapterEnabled());
      evento.operation(operacao(leiaute, xml));
      evento = events.saveAndFlush(evento);
      evento.source(arquivo, artefatos.guardar(identificador, "csv", csv), !cfg.realAdapterEnabled());
      events.saveAndFlush(evento);
      evento.unsigned(artefatos.guardar(identificador, "evento", xml));
      events.saveAndFlush(evento);
      evento.signed(artefatos.guardar(identificador, "assinado", assinado));
      events.saveAndFlush(evento);
      var batch = new DereBatchEntity("<DeRE/>");
      batch.ownedBy(userId, entityId);
      batch = batches.saveAndFlush(batch);
      batch.request(artefatos.guardar(identificador, "lote", lote));
      batches.saveAndFlush(batch);
      evento.attachBatch(batch.getId());
      events.saveAndFlush(evento);
      var agora = Instant.now();
      String protocolo;
      String recepcao;
      if (cfg.realAdapterEnabled()) {
        var envio = government.submit(lote, identificador);
        protocolo = envio.protocol() == null || envio.protocol().isBlank() ? novoProtocolo() : envio.protocol();
        recepcao = envio.responseBody() == null ? "" : envio.responseBody();
        registrarTentativa(evento.getId(), "SUBMIT", protocolo, recepcao, envio.httpStatus(), false);
      } else {
        protocolo = novoProtocolo();
        recepcao = assinar(retornos.recepcao(cnpj, cnpj, protocolo, agora), chave);
        registrarTentativa(evento.getId(), "SUBMIT", protocolo, recepcao, 200, true);
      }
      batch.received(protocolo, artefatos.guardar(identificador, "recepcao", recepcao));
      batches.saveAndFlush(batch);
      evento.sent();
      events.saveAndFlush(evento);
      if ("D-1001".equals(leiaute)) importD1001.persistValidatedXml(userId, entityId, arquivo, xml);
      return new ResultadoEnvioSimulado(true, relatorio, visao(batch, evento));
    } catch (ExcecaoTransmissaoGoverno ex) {
      throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, textoEnvio(ex), ex);
    } catch (ResponseStatusException ex) {
      throw ex;
    } catch (Exception ex) {
      throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, mensagemEnvio(ex), ex);
    }
  }

  @Transactional
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
      var retorno = consulta.responseBody() == null ? "" : consulta.responseBody();
      var recibo = consulta.receipt() == null || consulta.receipt().isBlank() ? null : consulta.receipt();
      var status = switch (consulta.state()) {
        case ACCEPTED, ACCEPTED_WITH_WARNINGS -> EventStatus.ACCEPTED;
        case REJECTED -> EventStatus.REJECTED;
        default -> EventStatus.PROCESSING;
      };
      VisaoTransmissao local = null;
      if (batch != null) {
        var evento = eventoDoLote(batch.getId());
        batch.completed(status, artefatos.guardar(evento.getEventIdentifier(), "retorno", retorno));
        batches.save(batch);
        if (status != EventStatus.PROCESSING) {
          evento.processed(status, recibo);
          events.save(evento);
        }
        registrarTentativa(evento.getId(), "QUERY", numero, retorno, consulta.httpStatus(), false);
        local = visao(batch, evento);
      }
      return new ConsultaProtocolo(numero, status.name(), recibo, consulta.codigoGoverno(), retorno, local, null);
    } catch (ExcecaoTransmissaoGoverno ex) {
      throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, textoConsulta(numero, ex), ex);
    } catch (ResponseStatusException ex) {
      throw ex;
    } catch (Exception ex) {
      throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, mensagem(ex), ex);
    }
  }

  @Transactional
  public VisaoTransmissao query(Long userId, Long entityId, Long batchId) {
    exigirMock();
    access.assertAccess(userId, entityId);
    var batch = batches.findById(batchId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Lote não encontrado"));
    if (batch.getEntityId() != null && !batch.getEntityId().equals(entityId)) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Lote de outra entidade");
    }
    var evento = eventoDoLote(batchId);
    if (evento.getReceiptNumber() != null && !evento.getReceiptNumber().isBlank()) return visao(batch, evento);
    try {
      var cfg = integration.current();
      if (cfg.realAdapterEnabled()) {
        var consulta = government.query(batch.getProtocol());
        var retorno = consulta.responseBody() == null ? "" : consulta.responseBody();
        var recibo = consulta.receipt() == null || consulta.receipt().isBlank() ? null : consulta.receipt();
        var status = switch (consulta.state()) {
          case ACCEPTED, ACCEPTED_WITH_WARNINGS -> EventStatus.ACCEPTED;
          case REJECTED -> EventStatus.REJECTED;
          default -> EventStatus.PROCESSING;
        };
        batch.completed(status, artefatos.guardar(evento.getEventIdentifier(), "retorno", retorno));
        batches.save(batch);
        if (status != EventStatus.PROCESSING) {
          evento.processed(status, recibo);
          events.save(evento);
        }
        registrarTentativa(evento.getId(), "QUERY", batch.getProtocol(), retorno, consulta.httpStatus(), false);
        return visao(batch, evento);
      }
      var chave = carregarChave(entityId);
      var agora = Instant.now();
      var recebido = batch.getSentAt() == null ? agora : batch.getSentAt();
      var xmlEvento = artefatos.ler(evento.getXmlUnsigned());
      var periodo = periodo(evento.getEventType(), xmlEvento);
      var recibo = novoRecibo(batch.getId());
      var hash = hash(xmlEvento);
      var tabela = retornos.eventoTabela(evento.getEventIdentifier(), access.entity(entityId).getCnpjRoot(),
          evento.getEventType(), batch.getProtocol(), recibo, hash, recebido, agora, periodo[0], periodo[1]);
      var tabelaAssinada = assinar(tabela, chave);
      var consulta = assinar(retornos.consulta(access.entity(entityId).getCnpjRoot(), access.entity(entityId).getCnpjRoot(),
          batch.getProtocol(), recebido, agora, tabelaAssinada), chave);
      batch.completed(EventStatus.ACCEPTED, artefatos.guardar(evento.getEventIdentifier(), "retorno", consulta));
      batches.save(batch);
      evento.processed(EventStatus.ACCEPTED, recibo);
      events.save(evento);
      registrarTentativa(evento.getId(), "QUERY", batch.getProtocol(), consulta, 200, true);
      return visao(batch, evento);
    } catch (ResponseStatusException ex) {
      throw ex;
    } catch (Exception ex) {
      throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, mensagem(ex), ex);
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

  private static String textoEnvio(ExcecaoTransmissaoGoverno ex) {
    var corpo = ex.responseBody() == null ? "" : ex.responseBody().replaceAll("\\s+", " ").trim();
    if (corpo.length() > 400) corpo = corpo.substring(0, 400);
    if (ex.statusCode() == 401) return "A Receita recusou a autenticação. Verifique o certificado e as credenciais da Integra Receita.";
    if (ex.statusCode() == 403) return "A Receita recusou o envio por falta de permissão neste ambiente.";
    if (ex.statusCode() == 404) return "A Receita não aceitou o envio (recurso não encontrado).";
    if (ex.retryable() || ex.statusCode() == 408) {
      if (corpo.isBlank()) return "Falha de comunicação com a Receita Federal (HTTP " + ex.statusCode() + "). Tente novamente.";
      return "Falha de comunicação com a Receita Federal (HTTP " + ex.statusCode() + "): " + corpo;
    }
    if (corpo.isBlank()) return "A Receita recusou o envio (HTTP " + ex.statusCode() + ").";
    return "A Receita recusou o envio (HTTP " + ex.statusCode() + "): " + corpo;
  }

  private static String textoConsulta(String protocolo, ExcecaoTransmissaoGoverno ex) {
    if (ex.statusCode() == 404) return "A Receita não encontrou o protocolo " + protocolo + ".";
    var corpo = ex.responseBody() == null ? "" : ex.responseBody().replaceAll("\\s+", " ").trim();
    if (corpo.length() > 400) corpo = corpo.substring(0, 400);
    if (corpo.isBlank()) return "A Receita recusou a consulta do protocolo " + protocolo + " (HTTP " + ex.statusCode() + ").";
    return "A Receita recusou a consulta do protocolo " + protocolo + " (HTTP " + ex.statusCode() + "): " + corpo;
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

  private static String mensagem(Exception ex) {
    for (Throwable atual = ex; atual != null; atual = atual.getCause()) {
      var texto = atual.getMessage() == null ? "" : atual.getMessage();
      if (texto.contains("Socket error") || texto.contains("max_allowed_packet") || texto.contains("Packet for query is too large")
          || texto.contains("Connection is closed")) {
        return "O XML do D-1011 é maior que o max_allowed_packet do MariaDB (1 MB). Execute: SET GLOBAL max_allowed_packet=67108864;";
      }
      if (texto.contains("Duplicate entry") || texto.contains("uk_transmission_attempt") || texto.contains("ConstraintViolation")) {
        return "A consulta do protocolo foi feita na Receita, mas não foi possível gravar a tentativa. Consulte de novo para atualizar o retorno.";
      }
    }
    var texto = ex.getMessage() == null ? "" : ex.getMessage();
    if (texto.isBlank() || texto.contains("could not execute statement") || texto.contains("SQL") || texto.contains("Constraint")) {
      return "Falha ao processar a consulta do protocolo.";
    }
    return texto;
  }

  private static String mensagemEnvio(Exception ex) {
    for (Throwable atual = ex; atual != null; atual = atual.getCause()) {
      var texto = atual.getMessage() == null ? "" : atual.getMessage();
      if (texto.contains("Socket error") || texto.contains("max_allowed_packet") || texto.contains("Packet for query is too large")
          || texto.contains("Connection is closed")) {
        return "O XML é maior que o max_allowed_packet do MariaDB (1 MB). Execute: SET GLOBAL max_allowed_packet=67108864;";
      }
      if (texto.contains("Duplicate entry") || texto.contains("uk_transmission_attempt") || texto.contains("ConstraintViolation")) {
        return "O envio foi processado, mas não foi possível gravar a tentativa. Tente novamente.";
      }
    }
    var texto = ex.getMessage() == null ? "" : ex.getMessage();
    if (texto.isBlank() || texto.contains("could not execute statement") || texto.contains("SQL") || texto.contains("Constraint")) {
      return "Falha ao processar o envio à Receita Federal.";
    }
    return texto;
  }

  private record SigningKey(java.security.KeyStore keyStore, char[] password, String alias) {}
}
