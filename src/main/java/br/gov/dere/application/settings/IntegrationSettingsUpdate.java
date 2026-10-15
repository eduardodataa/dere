package br.gov.dere.application.settings;

public record IntegrationSettingsUpdate(
    Boolean realAdapterEnabled,
    Boolean mockEnabled,
    String baseUrl,
    String tokenUrl,
    String clientId,
    String clientSecret,
    String environment,
    String schemaVersion) {}
