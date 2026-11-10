package br.gov.dere.application.access;

import java.security.SecureRandom;

public final class GeradorSenhaProvisoria {
  private static final char[] ALFABETO = "ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz23456789".toCharArray();
  private static final SecureRandom ALEATORIO = new SecureRandom();

  private GeradorSenhaProvisoria() {}

  public static String gerar() {
    var senha = new char[12];
    for (var i = 0; i < senha.length; i++) {
      senha[i] = ALFABETO[ALEATORIO.nextInt(ALFABETO.length)];
    }
    return new String(senha);
  }
}
