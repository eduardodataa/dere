package br.gov.dere.application.configuracao;

public record AtualizacaoConfiguracaoIntegracao(
    Boolean realAdapterEnabled,
    Boolean mockEnabled,
    String baseUrl,
    String tokenUrl,
    String clientId,
    String clientSecret,
    String environment,
    String schemaVersion) {}
