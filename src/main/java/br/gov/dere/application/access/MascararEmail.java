package br.gov.dere.application.access;

public final class MascararEmail {
  private MascararEmail() {}

  public static String aplicar(String email) {
    if (email == null || email.isBlank()) return "";
    var texto = email.trim();
    var arroba = texto.indexOf('@');
    if (arroba <= 0 || arroba == texto.length() - 1) return "***";
    var local = texto.substring(0, arroba);
    var dominio = texto.substring(arroba);
    if (local.length() <= 1) return local.charAt(0) + "***" + dominio;
    if (local.length() == 2) return local.charAt(0) + "*" + dominio;
    if (local.length() == 3) return local + dominio;
    return local.substring(0, 3) + "*******" + dominio;
  }
}
