package br.gov.dere.domain.transmissao;

public enum EstadoTransmissao {
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
