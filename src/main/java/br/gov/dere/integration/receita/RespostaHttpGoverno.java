package br.gov.dere.integration.receita;

public record RespostaHttpGoverno(int statusCode, String body) {
  public boolean successful() { return statusCode >= 200 && statusCode < 300; }
}
