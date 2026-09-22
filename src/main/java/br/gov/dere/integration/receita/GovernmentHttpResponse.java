package br.gov.dere.integration.receita;

public record GovernmentHttpResponse(int statusCode, String body) {
  public boolean successful() { return statusCode >= 200 && statusCode < 300; }
}
