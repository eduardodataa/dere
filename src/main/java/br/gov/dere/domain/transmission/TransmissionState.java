package br.gov.dere.domain.transmission;

public enum TransmissionState {
  READY,
  SIGNING,
  SIGNED,
  SUBMITTING,
  PROTOCOL_RECEIVED,
  PROCESSING,
  ACCEPTED,
  ACCEPTED_WITH_WARNINGS,
  REJECTED,
  RETRY_WAIT,
  FAILED
}
