package br.gov.dere.application.access;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.Test;

class ControleTentativaLoginTest {
  @Test
  void bloqueiaOLoginNaQuintaFalhaELiberaDepoisDaJanela() {
    var instante = new AtomicReference<>(Instant.parse("2026-10-22T15:00:00Z"));
    var controle = new ControleTentativaLogin(instante::get);
    for (var i = 0; i < 4; i++) controle.registrarFalha("Eduardo", "10.0.0.8");
    assertFalse(controle.bloqueado("eduardo", "10.0.0.8"));
    controle.registrarFalha("eduardo", "10.0.0.9");
    assertTrue(controle.bloqueado("EDUARDO", "10.0.0.8"));
    assertFalse(controle.bloqueado("outro", "10.0.0.9"));
    instante.set(instante.get().plusSeconds(16 * 60));
    assertFalse(controle.bloqueado("eduardo", "10.0.0.8"));
  }

  @Test
  void sucessoZeraOContadorDoLoginEDoIp() {
    var controle = new ControleTentativaLogin();
    for (var i = 0; i < 4; i++) controle.registrarFalha("eduardo", "10.0.0.8");
    controle.registrarSucesso("eduardo", "10.0.0.8");
    controle.registrarFalha("eduardo", "10.0.0.8");
    assertFalse(controle.bloqueado("eduardo", "10.0.0.8"));
  }
}