package br.gov.dere.application.configuracao;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public final class ValidadeCredencial {
  private static final DateTimeFormatter EXIBICAO = DateTimeFormatter.ofPattern("dd/MM/yyyy");

  private ValidadeCredencial() {}

  public static LocalDate ler(String texto) {
    if (texto == null || texto.isBlank()) return null;
    try {
      return LocalDate.parse(texto.trim());
    } catch (DateTimeParseException ex) {
      return null;
    }
  }

  public static boolean vencida(String texto, LocalDate hoje) {
    var data = ler(texto);
    return data != null && data.isBefore(hoje);
  }

  public static String exibir(String texto) {
    var data = ler(texto);
    return data == null ? "" : data.format(EXIBICAO);
  }
}
