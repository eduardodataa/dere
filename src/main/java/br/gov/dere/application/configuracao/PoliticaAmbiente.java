package br.gov.dere.application.configuracao;

public final class PoliticaAmbiente {
  public static final String LOCAL = "local";
  public static final String STAGING = "staging";
  public static final String HOMOLOGACAO = "homologacao";
  public static final String PRODUCAO = "producao";
  public static final String URL_PRE_PRODUCAO = "https://api.receitafederal.gov.br/prr-dere";
  public static final String URL_PRODUCAO = "https://api.receitafederal.gov.br/dere";

  private PoliticaAmbiente() {}

  public static String normalizar(String valor) {
    if (valor == null || valor.isBlank()) return LOCAL;
    return switch (valor.trim().toLowerCase()) {
      case "staging", "stg" -> STAGING;
      case "homologacao", "homologação", "hml" -> HOMOLOGACAO;
      case "producao", "produção", "production", "prod" -> PRODUCAO;
      default -> LOCAL;
    };
  }

  public static ConfiguracaoIntegracao aplicar(String implantacao, ConfiguracaoIntegracao atual) {
    return switch (normalizar(implantacao)) {
      case STAGING -> new ConfiguracaoIntegracao(false, true, atual.baseUrl(), atual.tokenUrl(), atual.clientId(),
          atual.clientSecret(), atual.clientSecretConfigured(), atual.environment(), atual.schemaVersion());
      case HOMOLOGACAO -> new ConfiguracaoIntegracao(true, false, URL_PRE_PRODUCAO, atual.tokenUrl(), atual.clientId(),
          atual.clientSecret(), atual.clientSecretConfigured(), "restricted-production", atual.schemaVersion());
      case PRODUCAO -> new ConfiguracaoIntegracao(true, false, URL_PRODUCAO, atual.tokenUrl(), atual.clientId(),
          atual.clientSecret(), atual.clientSecretConfigured(), "production", atual.schemaVersion());
      default -> atual;
    };
  }

  public static String rodape(String implantacao) {
    return switch (normalizar(implantacao)) {
      case STAGING -> "Ambiente de staging. Os envios são simulados.";
      case HOMOLOGACAO -> "Ambiente de homologação. Os envios são de pré-produção.";
      default -> null;
    };
  }

  public static String rotulo(String implantacao) {
    return switch (normalizar(implantacao)) {
      case STAGING -> "Staging";
      case HOMOLOGACAO -> "Homologação";
      case PRODUCAO -> "Produção";
      default -> "Local";
    };
  }
}
