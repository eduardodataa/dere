package br.gov.dere.application.certificate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import org.junit.jupiter.api.Test;

class LocalCertificatePasswordStoreTest {
  @Test
  void guardaLeEApagaForaDoBanco() throws Exception {
    var pasta = Files.createTempDirectory("cert-secrets-unit");
    var store = new LocalCertificatePasswordStore(pasta, "local-poc-key-not-for-production");
    var ref = store.guardar(7L, "changeit");
    assertTrue(ref.startsWith("local:"));
    assertEquals("changeit", store.ler(ref));
    store.apagar(ref);
    assertFalse(Files.exists(pasta.resolve(ref.substring(6))));
  }

  @Test
  void rejeitaReferenciaEstranha() throws Exception {
    var pasta = Files.createTempDirectory("cert-secrets-unit");
    var store = new LocalCertificatePasswordStore(pasta, "local-poc-key-not-for-production");
    assertThrows(IllegalArgumentException.class, () -> store.ler("../x"));
    assertThrows(IllegalArgumentException.class, () -> store.ler("local:../x.enc"));
  }
}
