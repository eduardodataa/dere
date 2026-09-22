package br.gov.dere.application.mock;

import br.gov.dere.application.certificate.CertificateService;
import br.gov.dere.application.d1001.D1001XmlGenerator;
import br.gov.dere.domain.EventStatus;
import br.gov.dere.domain.contributor.D1001;
import br.gov.dere.integration.signature.DereXmlSigner;
import br.gov.dere.integration.signature.DereXmlSignatureVerifier;
import br.gov.dere.integration.signature.LocalTestCertificate;
import br.gov.dere.integration.xml.DereBatchBuilder;
import br.gov.dere.integration.xml.XmlSupport;
import br.gov.dere.persistence.DereBatchEntity;
import br.gov.dere.persistence.DereBatchRepository;
import br.gov.dere.persistence.DereEventEntity;
import br.gov.dere.persistence.DereEventRepository;
import br.gov.dere.persistence.DereTransmissionAttemptEntity;
import br.gov.dere.persistence.DereTransmissionAttemptRepository;
import br.gov.dere.persistence.DereValidationAuditEntity;
import br.gov.dere.persistence.DereValidationAuditRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LocalDereFlowService {
  private final DereEventRepository events;
  private final DereBatchRepository batches;
  private final DereValidationAuditRepository audits;
  private final DereTransmissionAttemptRepository attempts;
  private final CertificateService certificates;
  private final D1001XmlGenerator generator = new D1001XmlGenerator();
  private final DereXmlSigner signer = new DereXmlSigner();
  private final DereXmlSignatureVerifier verifier = new DereXmlSignatureVerifier();

  public LocalDereFlowService(DereEventRepository events, DereBatchRepository batches, DereValidationAuditRepository audits,
      DereTransmissionAttemptRepository attempts, CertificateService certificates) {
    this.events = events; this.batches = batches; this.audits = audits; this.attempts = attempts; this.certificates = certificates;
  }

  @Transactional
  public FlowResult submit(D1001 document, Long userId, Long entityId) {
    String correlationId = UUID.randomUUID().toString();
    long started = System.nanoTime();
    DereTransmissionAttemptEntity attempt = null;
    try {
      String unsigned = generator.generate(document);
      var event = events.save(new DereEventEntity("D-1001", document.id(), "restricted-production", "nota_2026_001", unsigned));
      attempt = attempts.save(new DereTransmissionAttemptEntity(event.getId(), "SUBMIT", 1, correlationId));
      var available = certificates.list(entityId);
      var loaded = available.isEmpty() ? null : certificates.loadForSigning(available.get(0).id());
      var keyStore = loaded == null ? LocalTestCertificate.create() : loaded.keyStore();
      var password = loaded == null ? "changeit".toCharArray() : loaded.password();
      var alias = loaded == null ? "dere-test" : loaded.alias();
      String signed = signer.sign(unsigned, keyStore, password, alias);
      if (!verifier.verify(signed, keyStore, alias)) throw new IllegalStateException("Assinatura XMLDSig inválida");
      validate(signed, "evtInfoContrib-v1_0_1.xsd");
      event.signed(signed); events.save(event);
      String batchXml = new DereBatchBuilder().build(document.cnpjRoot(), List.of(signed));
      validate(batchXml, "envioLoteDere-v1_0_1.xsd");
      audits.save(new DereValidationAuditEntity(userId, entityId, "SEND", "D-1001", true, "", batchXml, null));
      var batch = batches.save(new DereBatchEntity(batchXml));
      String protocol = "MOCK-" + UUID.randomUUID(); batch.received(protocol); batches.save(batch);
      attempt.complete("SUCCESS", 200, "MOCK_ACCEPTED", elapsed(started), "mock://submit/" + correlationId, "mock://protocol/" + protocol, null); attempts.save(attempt);
      return new FlowResult(batch.getId(), protocol, event.getEventIdentifier(), EventStatus.PROCESSING, batchXml);
    } catch (Exception ex) {
      if (attempt != null) { attempt.complete("FAILURE", null, null, elapsed(started), null, null, ex.getMessage()); attempts.save(attempt); }
      audits.save(new DereValidationAuditEntity(userId, entityId, "SEND", "D-1001", false, ex.getMessage(), null, null));
      throw new IllegalStateException("Falha no fluxo local DeRE", ex);
    }
  }

  @Transactional
  public FlowResult query(Long batchId) {
    var batch = batches.findById(batchId).orElseThrow();
    batch.completed(EventStatus.ACCEPTED, "<retorno><protocolo>" + batch.getProtocol() + "</protocolo><situacao>ACEITO</situacao><recibo>REC-" + batch.getId() + "</recibo></retorno>"); batches.save(batch);
    var event = events.findAll().stream().filter(item -> batch.getRequestXml().contains(item.getEventIdentifier())).findFirst().orElseThrow();
    event.processed(EventStatus.ACCEPTED, "REC-" + batch.getId()); events.save(event);
    return new FlowResult(batch.getId(), batch.getProtocol(), event.getEventIdentifier(), EventStatus.ACCEPTED, batch.getRequestXml());
  }

  private void validate(String xml, String file) throws Exception { var url = getClass().getResource("/dere/schemas/nota_2026_001/xsd/" + file); if (url == null) throw new IllegalStateException("XSD não encontrado: " + file); XmlSupport.validate(xml, url); }
  private static long elapsed(long started) { return (System.nanoTime() - started) / 1_000_000L; }
  public record FlowResult(Long batchId, String protocol, String eventId, EventStatus status, String xml) {}
}
