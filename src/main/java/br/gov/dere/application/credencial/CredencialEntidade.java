package br.gov.dere.application.credencial;

public record CredencialEntidade(
    Long entityId,
    String clientId,
    String clientSecret,
    boolean clientSecretConfigured,
    String validadeCredencial) {

  public boolean completa() {
    return clientId != null
        && !clientId.isBlank()
        && clientSecret != null
        && !clientSecret.isBlank();
  }
}
