package br.gov.dere.application.transmission;

import br.gov.dere.application.access.AccessService;
import br.gov.dere.application.certificate.CertificateService;
import br.gov.dere.application.csv.DereCsvConverter;
import br.gov.dere.application.d1001.D1001ImportService;
import br.gov.dere.application.pgcc.D1011CsvConverter;
import br.gov.dere.application.validation.ServicoValidacaoLeiaute;
import br.gov.dere.application.validation.relatorio.Critica;
import br.gov.dere.application.validation.relatorio.RelatorioValidacao;
import br.gov.dere.application.validation.relatorio.TipoCritica;
import br.gov.dere.domain.EventStatus;
import br.gov.dere.integration.signature.DereXmlSigner;
import br.gov.dere.integration.signature.DereXmlSignatureVerifier;
import br.gov.dere.integration.signature.LocalTestCertificate;
import br.gov.dere.integration.xml.DereBatchBuilder;
import br.gov.dere.persistence.DereBatchEntity;
import br.gov.dere.persistence.DereBatchRepository;
import br.gov.dere.persistence.DereEventEntity;
import br.gov.dere.persistence.DereEventRepository;
import br.gov.dere.persistence.DereTransmissionAttemptEntity;
import br.gov.dere.persistence.DereTransmissionAttemptRepository;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class SimulatedTransmissionService {
  private static final DateTimeFormatter PROTOCOLO = DateTimeFormatter.ofPattern("yyyyMMddHHmmss").withZone(ZoneOffset.UTC);
  private final boolean mockEnabled;
  private final String environment;
  private final String schemaVersion;
  private final AccessService access;
  private final ServicoValidacaoLeiaute validacao;
  private final D1001ImportService importD1001;
  private final CertificateService certificates;
  private final DereEventRepository events;
  private final DereBatchRepository batches;
  private final DereTransmissionAttemptRepository attempts;
  private final TransmissionArtifactStore artefatos;
  private final OfficialReturnXmlBuilder retornos = new OfficialReturnXmlBuilder();
  private final DereXmlSigner signer = new DereXmlSigner();
  private final DereXmlSignatureVerifier verifier = new DereXmlSignatureVerifier();
  private final DereCsvConverter csvD1001 = new DereCsvConverter();
  private final D1011CsvConverter csvD1011 = new D1011CsvConverter();

  public SimulatedTransmissionService(
      @Value("${dere.mock.enabled:true}") boolean mockEnabled,
      @Value("${dere.environment:restricted-production}") String environment,
      @Value("${dere.schema-version:nota_2026_001}") String schemaVersion,
      AccessService access,
      ServicoValidacaoLeiaute validacao,
      D1001ImportService importD1001,
      CertificateService certificates,
      DereEventRepository events,
      DereBatchRepository batches,
      DereTransmissionAttemptRepository attempts,
      TransmissionArtifactStore artefatos) {
    this.mockEnabled = mockEnabled;
    this.environment = environment;
    this.schemaVersion = schemaVersion;
    this.access = access;
    this.validacao = validacao;
    this.importD1001 = importD1001;
    this.certificates = certificates;
    this.events = events;
    this.batches = batches;
    this.attempts = attempts;
    this.artefatos = artefatos;
  }

  @Transactional
  public SimulateSendResult simulate(Long userId, Long entityId, SimulateSendRequest request) {
    exigirMock();
    access.assertAccess(userId, entityId);
    var cnpj = access.entity(entityId).getCnpjRoot();
    var leiaute = normalizarLeiaute(request.layout());
    var origem = request.origem() == null ? "CSV" : request.origem().trim().toUpperCase();
    var arquivo = request.arquivo() == null || request.arquivo().isBlank() ? leiaute.toLowerCase() + ".dat" : request.arquivo();
    var relatorio = "XML".equals(origem)
        ? validacao.validarXml(leiaute, request.conteudo(), arquivo, cnpj, entityId)
        : validacao.validarCsv(leiaute, request.conteudo(), arquivo, cnpj, entityId);
    if (!relatorio.valido()) return new SimulateSendResult(false, relatorio, null);
    var xml = relatorio.xml();
    var csv = relatorio.csv();
    var identificador = identificador(leiaute, xml);
    var jaEnviado = events.findByEventIdentifier(identificador);
    if (jaEnviado.isPresent()) {
      var existente = jaEnviado.get();
      var loteExistente = existente.getBatchId() == null ? null : batches.findById(existente.getBatchId()).orElse(null);
      return new SimulateSendResult(false, comCritica(relatorio, arquivo, "id", identificador,
          "Identificador de evento ainda não transmitido",
          "Já existe transmissão com este id. O lote anterior está em Transmissões. Para alteração ou exclusão use outro id (tpOper 2 ou 3)."),
          loteExistente == null ? visao(null, existente) : visao(loteExistente, existente));
    }
    try {
      var chave = carregarChave(entityId);
      var assinado = signer.sign(xml, chave.keyStore(), chave.password(), chave.alias());
      if (!verifier.verify(assinado, chave.keyStore(), chave.alias())) throw new IllegalStateException("Assinatura XMLDSig inválida");
      var lote = new DereBatchBuilder().build(cnpj, List.of(assinado));
      var evento = new DereEventEntity(leiaute, identificador, environment, schemaVersion, null);
      evento.ownedBy(userId, entityId);
      evento.source(arquivo, csvCurto(csv), true);
      evento.operation(operacao(leiaute, xml));
      evento = events.saveAndFlush(evento);
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
      var protocolo = novoProtocolo();
      var recepcao = assinar(retornos.recepcao(cnpj, cnpj, protocolo, agora), chave);
      batch.received(protocolo, artefatos.guardar(identificador, "recepcao", recepcao));
      batches.saveAndFlush(batch);
      evento.sent();
      events.saveAndFlush(evento);
      registrarTentativa(evento.getId(), "SUBMIT", protocolo, recepcao);
      if ("D-1001".equals(leiaute)) importD1001.persistValidatedXml(userId, entityId, arquivo, xml);
      return new SimulateSendResult(true, relatorio, visao(batch, evento));
    } catch (ResponseStatusException ex) {
      throw ex;
    } catch (Exception ex) {
      throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, mensagem(ex), ex);
    }
  }

  @Transactional
  public TransmissionView query(Long userId, Long entityId, Long batchId) {
    exigirMock();
    access.assertAccess(userId, entityId);
    var batch = batches.findById(batchId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Lote não encontrado"));
    if (batch.getEntityId() != null && !batch.getEntityId().equals(entityId)) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Lote de outra entidade");
    }
    var evento = eventoDoLote(batchId);
    if (evento.getReceiptNumber() != null && !evento.getReceiptNumber().isBlank()) return visao(batch, evento);
    try {
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
      registrarTentativa(evento.getId(), "QUERY", batch.getProtocol(), consulta);
      return visao(batch, evento);
    } catch (ResponseStatusException ex) {
      throw ex;
    } catch (Exception ex) {
      throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, mensagem(ex), ex);
    }
  }

  public List<TransmissionView> listar(Long userId, Long entityId) {
    access.assertAccess(userId, entityId);
    return events.findByEntityIdOrderByIdDesc(entityId).stream()
        .map(evento -> visao(evento.getBatchId() == null ? null : batches.findById(evento.getBatchId()).orElse(null), evento).resumida())
        .toList();
  }

  public TransmissionView detalhe(Long userId, Long entityId, Long batchId) {
    access.assertAccess(userId, entityId);
    var batch = batches.findById(batchId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Lote não encontrado"));
    if (batch.getEntityId() != null && !batch.getEntityId().equals(entityId)) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Lote de outra entidade");
    }
    return visao(batch, eventoDoLote(batchId));
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

  private TransmissionView visao(DereBatchEntity batch, DereEventEntity evento) {
    return new TransmissionView(
        batch == null ? null : batch.getId(),
        evento.getId(),
        evento.getEventType(),
        evento.getSourceName(),
        evento.getEventIdentifier(),
        batch == null ? null : batch.getProtocol(),
        evento.getReceiptNumber(),
        statusDe(batch, evento),
        evento.isSimulated(),
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
      return new SigningKey(LocalTestCertificate.create(), "changeit".toCharArray(), "dere-test");
    }
    var carregado = certificates.loadForSigning(disponiveis.get(0).id());
    return new SigningKey(carregado.keyStore(), carregado.password(), carregado.alias());
  }

  private String assinar(String xml, SigningKey chave) throws Exception {
    var assinado = signer.sign(xml, chave.keyStore(), chave.password(), chave.alias());
    if (!verifier.verify(assinado, chave.keyStore(), chave.alias())) throw new IllegalStateException("Assinatura XMLDSig inválida");
    return assinado;
  }

  private void registrarTentativa(Long eventId, String operacao, String protocolo, String retorno) {
    var tentativa = new DereTransmissionAttemptEntity(eventId, operacao, 1, UUID.randomUUID().toString());
    tentativa.complete("SUCCESS", 200, "MOCK_ACCEPTED", 0, "mock://" + operacao.toLowerCase() + "/" + protocolo, "mock://retorno/" + protocolo, null);
    attempts.save(tentativa);
  }

  private void exigirMock() {
    if (!mockEnabled) throw new ResponseStatusException(HttpStatus.CONFLICT, "Envio simulado desabilitado (DERE_MOCK_ENABLED=false).");
  }

  private static String normalizarLeiaute(String layout) {
    if (layout == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Leiaute obrigatório");
    var valor = layout.trim().toUpperCase();
    if (!"D-1001".equals(valor) && !"D-1011".equals(valor)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Leiaute não suportado no envio simulado");
    return valor;
  }

  private String identificador(String leiaute, String xml) {
    try {
      return "D-1011".equals(leiaute) ? csvD1011.fromXml(xml).id() : csvD1001.fromXml(xml).id();
    } catch (Exception ex) {
      throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Não foi possível ler o id do evento", ex);
    }
  }

  private String operacao(String leiaute, String xml) {
    try {
      return String.valueOf("D-1011".equals(leiaute) ? csvD1011.fromXml(xml).operation() : csvD1001.fromXml(xml).operation());
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
      var modelo = csvD1001.fromXml(xml);
      return new LocalDate[] { modelo.validFrom(), modelo.validTo() };
    } catch (Exception ex) {
      return new LocalDate[] { LocalDate.now(ZoneOffset.UTC), null };
    }
  }

  private RelatorioValidacao comCritica(RelatorioValidacao base, String arquivo, String coluna, String valor, String esperado, String problema) {
    var criticas = new ArrayList<>(base.criticas());
    criticas.add(new Critica(arquivo, 1, coluna, valor, TipoCritica.NEGOCIO, esperado, problema, "/DeRE"));
    return RelatorioValidacao.de(base.leiaute(), base.origem(), base.arquivo(), base.totalLinhas(), criticas)
        .comArtefatos(base.xml(), base.csv(), base.relatorioXlsx());
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

  private static String csvCurto(String csv) {
    if (csv == null) return null;
    return csv.getBytes(StandardCharsets.UTF_8).length > 200_000 ? null : csv;
  }

  private static String mensagem(Exception ex) {
    for (Throwable atual = ex; atual != null; atual = atual.getCause()) {
      var texto = atual.getMessage() == null ? "" : atual.getMessage();
      if (texto.contains("Socket error") || texto.contains("max_allowed_packet") || texto.contains("Packet for query is too large")
          || texto.contains("Connection is closed")) {
        return "O XML do D-1011 é maior que o max_allowed_packet do MariaDB (1 MB). Execute: SET GLOBAL max_allowed_packet=67108864;";
      }
    }
    return ex.getMessage() == null || ex.getMessage().isBlank() ? "Falha no envio simulado" : ex.getMessage();
  }

  private record SigningKey(java.security.KeyStore keyStore, char[] password, String alias) {}
}
