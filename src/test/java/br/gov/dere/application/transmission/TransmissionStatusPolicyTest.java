package br.gov.dere.application.transmission;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import br.gov.dere.domain.transmission.TransmissionState;
import org.junit.jupiter.api.Test;

class TransmissionStatusPolicyTest {
  @Test void mapsGovernmentStatesToApplicationStates() {
    assertEquals(TransmissionState.ACCEPTED, TransmissionStatusPolicy.fromGovernmentCode("ACEITO"));
    assertEquals(TransmissionState.ACCEPTED, TransmissionStatusPolicy.fromGovernmentCode("accepted"));
    assertEquals(TransmissionState.ACCEPTED_WITH_WARNINGS, TransmissionStatusPolicy.fromGovernmentCode("ACEITO_COM_OCORRENCIAS"));
    assertEquals(TransmissionState.REJECTED, TransmissionStatusPolicy.fromGovernmentCode("REJEITADO"));
    assertEquals(TransmissionState.PROCESSING, TransmissionStatusPolicy.fromGovernmentCode(" "));
    assertEquals(TransmissionState.PROCESSING, TransmissionStatusPolicy.fromGovernmentCode("DESCONHECIDO"));
  }
  @Test void identifiesTransientResponses() {
    assertTrue(TransmissionStatusPolicy.retryable(408));
    assertTrue(TransmissionStatusPolicy.retryable(429));
    assertTrue(TransmissionStatusPolicy.retryable(503));
    assertEquals(false, TransmissionStatusPolicy.retryable(400));
  }
}
