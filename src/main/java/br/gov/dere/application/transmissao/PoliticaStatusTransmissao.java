package br.gov.dere.application.transmissao;

import br.gov.dere.domain.transmissao.EstadoTransmissao;

public final class PoliticaStatusTransmissao {
  private PoliticaStatusTransmissao() {}
  public static boolean retryable(int httpStatus) { return httpStatus == 408 || httpStatus == 429 || httpStatus >= 500; }
  public static EstadoTransmissao deCodigoGoverno(String code) {
    if (code == null || code.isBlank()) return EstadoTransmissao.PROCESSING;
    return switch (code.toUpperCase()) {
      case "ACCEPTED", "ACEITO" -> EstadoTransmissao.ACCEPTED;
      case "ACCEPTED_WITH_WARNINGS", "ACEITO_COM_OCORRENCIAS" -> EstadoTransmissao.ACCEPTED_WITH_WARNINGS;
      case "REJECTED", "REJEITADO" -> EstadoTransmissao.REJECTED;
      default -> EstadoTransmissao.PROCESSING;
    };
  }
}
