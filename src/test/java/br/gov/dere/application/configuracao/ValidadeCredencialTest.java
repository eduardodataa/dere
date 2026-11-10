package br.gov.dere.application.configuracao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class ValidadeCredencialTest {
  @Test
  void diaDaValidadeAindaVale() {
    assertFalse(ValidadeCredencial.vencida("2026-10-31", LocalDate.of(2026, 10, 31)));
    assertTrue(ValidadeCredencial.vencida("2026-10-31", LocalDate.of(2026, 11, 10)));
    assertFalse(ValidadeCredencial.vencida("", LocalDate.of(2026, 11, 10)));
    assertFalse(ValidadeCredencial.vencida("31/10/2026", LocalDate.of(2026, 11, 10)));
    assertNull(ValidadeCredencial.ler("31/10/2026"));
    assertEquals("31/10/2026", ValidadeCredencial.exibir("2026-10-31"));
  }
}
