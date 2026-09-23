package br.gov.dere.application.transmission;

import java.time.Instant;

public record TransmissionView(
    Long batchId,
    Long eventId,
    String layout,
    String sourceName,
    String operationType,
    String eventIdentifier,
    String protocol,
    String receiptNumber,
    String status,
    boolean simulated,
    Instant createdAt,
    Instant sentAt,
    Instant processedAt,
    String csv,
    String eventXml,
    String batchXml,
    String returnXml) {
  public TransmissionView resumida() {
    return new TransmissionView(batchId, eventId, layout, sourceName, operationType, eventIdentifier, protocol, receiptNumber, status,
        simulated, createdAt, sentAt, processedAt, null, null, null, null);
  }
}
