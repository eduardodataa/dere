package br.gov.dere.application.transmissao;

final class Recibos {
  private Recibos() {}

  static boolean falso(String recibo) {
    return recibo != null && recibo.matches("REC-\\d+");
  }
}
