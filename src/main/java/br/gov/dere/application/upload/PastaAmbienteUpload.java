package br.gov.dere.application.upload;

import java.util.Locale;

public final class PastaAmbienteUpload {
  private PastaAmbienteUpload() {}

  public static String de(String deployment) {
    var valor = deployment == null ? "" : deployment.trim().toLowerCase(Locale.ROOT);
    return switch (valor) {
      case "producao", "production" -> "prod";
      case "homologacao", "homologação" -> "hml";
      case "staging", "stg" -> "stg";
      default -> "local";
    };
  }
}
