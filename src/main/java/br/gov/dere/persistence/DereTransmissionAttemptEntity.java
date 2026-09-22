package br.gov.dere.persistence;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "dere_transmission_attempt")
public class DereTransmissionAttemptEntity {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
  @Column(name = "event_id", nullable = false) private Long eventId;
  @Column(nullable = false) private String operation;
  @Column(nullable = false) private Integer attempt;
  @Column(name = "started_at", nullable = false) private Instant startedAt;
  @Column(name = "finished_at") private Instant finishedAt;
  @Column(name = "http_status") private Integer httpStatus;
  @Column(name = "government_code") private String governmentCode;
  @Column(name = "duration_ms") private Long durationMs;
  @Column(name = "correlation_id", nullable = false) private String correlationId;
  @Column(name = "request_reference") private String requestReference;
  @Column(name = "response_reference") private String responseReference;
  @Column(nullable = false) private String outcome;
  @Lob private String errorMessage;

  protected DereTransmissionAttemptEntity() {}
  public DereTransmissionAttemptEntity(Long eventId, String operation, Integer attempt, String correlationId) {
    this.eventId = eventId; this.operation = operation; this.attempt = attempt; this.correlationId = correlationId; this.startedAt = Instant.now(); this.outcome = "STARTED";
  }
  public void complete(String outcome, Integer httpStatus, String governmentCode, long durationMs, String requestReference, String responseReference, String errorMessage) { this.outcome = outcome; this.httpStatus = httpStatus; this.governmentCode = governmentCode; this.durationMs = durationMs; this.requestReference = requestReference; this.responseReference = responseReference; this.errorMessage = errorMessage; this.finishedAt = Instant.now(); }
  public Long getId() { return id; }
  public String getOutcome() { return outcome; }
  public Instant getStartedAt() { return startedAt; }
}
