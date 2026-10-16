package br.gov.dere.application.transmissao;

import java.time.Instant;

public record VisaoTransmissao(
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
    String environment,
    Instant createdAt,
    Instant sentAt,
    Instant processedAt,
    String csv,
    String eventXml,
    String batchXml,
    String returnXml) {
  public VisaoTransmissao resumida() {
    return new VisaoTransmissao(batchId, eventId, layout, sourceName, operationType, eventIdentifier, protocol, receiptNumber, status,
        simulated, environment, createdAt, sentAt, processedAt, null, null, null, null);
  }
}
