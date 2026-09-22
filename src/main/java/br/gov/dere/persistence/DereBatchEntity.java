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
import jakarta.persistence.Version;
import java.time.Instant;

@DynamicInsert
@DynamicUpdate
@Entity
@Table(name = "dere_batch")
public class DereBatchEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Version
  private long version;
  private String protocol;
  @Enumerated(EnumType.STRING)
  private EventStatus status;
  @Lob
  @Column(name = "request_xml", nullable = false)
  private String requestXml;
  @Lob
  private String responseXml;
  @Column(name = "created_at")
  private Instant createdAt = Instant.now();
  private Instant sentAt;
  private Instant lastQueryAt;
  @Column(name = "next_query_at")
  private Instant nextQueryAt;
  @Column(name = "lease_until")
  private Instant leaseUntil;
  @Column(name = "query_attempts", nullable = false)
  private int queryAttempts;
  @Column(name = "user_id")
  private Long userId;
  @Column(name = "entity_id")
  private Long entityId;

  protected DereBatchEntity() {}

  public DereBatchEntity(String xml) {
    requestXml = xml;
    status = EventStatus.BATCHED;
  }

  public Long getId() { return id; }
  public String getProtocol() { return protocol; }
  public EventStatus getStatus() { return status; }
  public String getRequestXml() { return requestXml; }
  public String getResponseXml() { return responseXml; }
  public int getQueryAttempts() { return queryAttempts; }
  public Instant getCreatedAt() { return createdAt; }
  public Instant getSentAt() { return sentAt; }
  public Long getUserId() { return userId; }
  public Long getEntityId() { return entityId; }

  public void persistId(Long value) { id = value; }
  public void request(String xml) { requestXml = xml; }
  public void ownedBy(Long user, Long entity) { userId = user; entityId = entity; }

  public void received(String value) {
    protocol = value;
    status = EventStatus.PROCESSING;
    sentAt = Instant.now();
    nextQueryAt = Instant.now().plusSeconds(30);
  }

  public void received(String value, String response) {
    received(value);
    responseXml = response;
  }

  public boolean claimQuery(Instant now, long leaseSeconds) {
    if (status != EventStatus.PROCESSING || (leaseUntil != null && leaseUntil.isAfter(now)) || (nextQueryAt != null && nextQueryAt.isAfter(now))) return false;
    leaseUntil = now.plusSeconds(leaseSeconds);
    queryAttempts++;
    return true;
  }

  public void completed(EventStatus value, String response) {
    status = value;
    responseXml = response;
    lastQueryAt = Instant.now();
    nextQueryAt = null;
    leaseUntil = null;
  }

  public void queryFailed(long delaySeconds) {
    lastQueryAt = Instant.now();
    nextQueryAt = Instant.now().plusSeconds(delaySeconds);
    leaseUntil = null;
  }
}
