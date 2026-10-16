package br.gov.dere.application.configuracao;

public record ConfiguracaoIntegracao(
    boolean realAdapterEnabled,
    boolean mockEnabled,
    String baseUrl,
    String tokenUrl,
    String clientId,
    String clientSecret,
    boolean clientSecretConfigured,
    String environment,
    String schemaVersion) {}
