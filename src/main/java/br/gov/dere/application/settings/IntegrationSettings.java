package br.gov.dere.application.settings;

public record IntegrationSettings(
    boolean realAdapterEnabled,
    boolean mockEnabled,
    String baseUrl,
    String tokenUrl,
    String clientId,
    String clientSecret,
    boolean clientSecretConfigured,
    String environment,
    String schemaVersion) {}
