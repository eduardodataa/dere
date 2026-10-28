package br.gov.dere.application.transmissao;

public class EnvioNaoIniciadoException extends RuntimeException {
  public EnvioNaoIniciadoException(Throwable causa) {
    super(causa);
  }
}
