package br.gov.dere.application.access;

import java.util.regex.Pattern;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public final class ValidadorEmail {
  private static final Pattern PADRAO = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

  private ValidadorEmail() {}

  public static String obrigatorio(String email) {
    var texto = email == null ? "" : email.trim();
    if (texto.isBlank()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Informe o e-mail.");
    if (texto.length() > 255 || !PADRAO.matcher(texto).matches()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "E-mail inválido.");
    }
    return texto;
  }
}
