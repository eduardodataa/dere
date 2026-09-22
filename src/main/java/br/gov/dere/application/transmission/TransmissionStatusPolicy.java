package br.gov.dere.application.transmission;

import br.gov.dere.domain.transmission.TransmissionState;

public final class TransmissionStatusPolicy {
  private TransmissionStatusPolicy() {}
  public static boolean retryable(int httpStatus) { return httpStatus == 408 || httpStatus == 429 || httpStatus >= 500; }
  public static TransmissionState fromGovernmentCode(String code) {
    if (code == null || code.isBlank()) return TransmissionState.PROCESSING;
    return switch (code.toUpperCase()) {
      case "ACCEPTED", "ACEITO" -> TransmissionState.ACCEPTED;
      case "ACCEPTED_WITH_WARNINGS", "ACEITO_COM_OCORRENCIAS" -> TransmissionState.ACCEPTED_WITH_WARNINGS;
      case "REJECTED", "REJEITADO" -> TransmissionState.REJECTED;
      default -> TransmissionState.PROCESSING;
    };
  }
}
