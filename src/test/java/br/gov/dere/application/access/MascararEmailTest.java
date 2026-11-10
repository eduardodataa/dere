package br.gov.dere.application.access;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class MascararEmailTest {
  @Test
  void mascaraParteLocalMantendoDominio() {
    assertEquals("dud*******@hotmail.com", MascararEmail.aplicar("duducordeiro_@hotmail.com"));
  }

  @Test
  void localCurto() {
    assertEquals("a***@x.com", MascararEmail.aplicar("a@x.com"));
    assertEquals("a*@x.com", MascararEmail.aplicar("ab@x.com"));
  }

  @Test
  void localDeTresCaracteres() {
    assertEquals("abc@x.com", MascararEmail.aplicar("abc@x.com"));
  }
}
