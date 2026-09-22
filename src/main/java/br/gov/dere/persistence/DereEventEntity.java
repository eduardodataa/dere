package br.gov.dere.persistence;

import br.gov.dere.domain.EventStatus;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import java.time.Instant;

@DynamicInsert
@DynamicUpdate
@Entity
@Table(name = "dere_event")
public class DereEventEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(name = "event_type", nullable = false)
  private String eventType;
  @Column(name = "event_identifier", nullable = false, unique = true, length = 42)
  private String eventIdentifier;
  @Column(nullable = false)
  private String environment;
  @Column(name = "reference_period")
  private String referencePeriod;
  @Column(name = "operation_type")
  private String operationType;
  @Column(name = "schema_version", nullable = false)
  private String schemaVersion;
  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private EventStatus status;
  @Lob
  @Column(name = "xml_unsigned")
  private String xmlUnsigned;
  @Lob
  @Column(name = "xml_signed")
  private String xmlSigned;
  @Column(name = "receipt_number")
  private String receiptNumber;
  @Column(name = "created_at", nullable = false)
  private Instant createdAt;
  private Instant sentAt;
  private Instant processedAt;
  @Column(name = "user_id")
  private Long userId;
  @Column(name = "entity_id")
  private Long entityId;
  @Column(name = "batch_id")
  private Long batchId;
  @Lob
  @Column(name = "csv_source")
  private String csvSource;
  @Column(name = "source_name")
  private String sourceName;
  @Column(nullable = false)
  private boolean simulated;

  protected DereEventEntity() {}

  public DereEventEntity(String type, String identifier, String environment, String version, String xml) {
    eventType = type;
    eventIdentifier = identifier;
    this.environment = environment;
    schemaVersion = version;
    xmlUnsigned = xml;
    status = EventStatus.GENERATED;
    createdAt = Instant.now();
  }

  public Long getId() { return id; }
  public String getEventType() { return eventType; }
  public String getEventIdentifier() { return eventIdentifier; }
  public String getXmlUnsigned() { return xmlUnsigned; }
  public String getXmlSigned() { return xmlSigned; }
  public EventStatus getStatus() { return status; }
  public String getReceiptNumber() { return receiptNumber; }
  public Instant getCreatedAt() { return createdAt; }
  public Instant getSentAt() { return sentAt; }
  public Instant getProcessedAt() { return processedAt; }
  public Long getUserId() { return userId; }
  public Long getEntityId() { return entityId; }
  public Long getBatchId() { return batchId; }
  public String getCsvSource() { return csvSource; }
  public String getSourceName() { return sourceName; }
  public boolean isSimulated() { return simulated; }
  public String getOperationType() { return operationType; }

  public void persistId(Long value) { id = value; }
  public void ownedBy(Long user, Long entity) { userId = user; entityId = entity; }
  public void attachBatch(Long value) { batchId = value; }
  public void source(String name, String csv, boolean mock) { sourceName = name; csvSource = csv; simulated = mock; }
  public void operation(String value) { operationType = value; }
  public void unsigned(String xml) { xmlUnsigned = xml; }
  public void signed(String xml) { xmlSigned = xml; status = EventStatus.SIGNED; }
  public void sent() { status = EventStatus.PROTOCOL_RECEIVED; sentAt = Instant.now(); }
  public void processed(EventStatus next, String receipt) { status = next; receiptNumber = receipt; processedAt = Instant.now(); }
}
