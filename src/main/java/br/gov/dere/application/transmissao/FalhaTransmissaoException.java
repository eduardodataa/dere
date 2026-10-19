package br.gov.dere.application.transmissao;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class FalhaTransmissaoException extends ResponseStatusException {
  private final DiagnosticoTransmissao diagnostico;

  public FalhaTransmissaoException(DiagnosticoTransmissao diagnostico, Throwable causa) {
    super(HttpStatus.valueOf(diagnostico.httpAplicacao()), diagnostico.mensagem(), causa);
    this.diagnostico = diagnostico;
  }

  public DiagnosticoTransmissao diagnostico() {
    return diagnostico;
  }
}
