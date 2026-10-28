package br.gov.dere.integration.receita;

public class FalhaAntesDaTransmissao extends RuntimeException {
  public FalhaAntesDaTransmissao(Throwable causa) {
    super(causa);
  }
}
