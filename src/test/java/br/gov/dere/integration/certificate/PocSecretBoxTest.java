package br.gov.dere.integration.certificate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;

class PocSecretBoxTest {
  @Test
  void encryptsAndDecryptsTextAndBytes() throws Exception {
    var box = new PocSecretBox("local-poc-key-not-for-production");
    var cipher = box.encrypt("senha-do-certificado");
    assertFalse(cipher.contains("senha-do-certificado"));
    assertEquals("senha-do-certificado", box.decrypt(cipher));
    var payload = "conteudo-p12".getBytes(StandardCharsets.UTF_8);
    assertEquals("conteudo-p12", new String(box.decrypt(box.encrypt(payload)), StandardCharsets.UTF_8));
  }
}
