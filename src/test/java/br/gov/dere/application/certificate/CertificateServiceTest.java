package br.gov.dere.application.certificate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.gov.dere.integration.certificate.PocSecretBox;
import br.gov.dere.integration.signature.LocalTestCertificate;
import br.gov.dere.persistence.DereEntityCertificateEntity;
import br.gov.dere.persistence.DereEntityCertificateRepository;
import java.io.ByteArrayOutputStream;
import java.nio.file.Files;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

class CertificateServiceTest {
  @Test
  void rejectsUnconfiguredProtectionKey() {
    var repo = mock(DereEntityCertificateRepository.class);
    var senhas = mock(CertificatePasswordStore.class);
    assertThrows(IllegalStateException.class, () -> new CertificateService(repo, "local-poc-change-me", senhas));
    assertThrows(IllegalStateException.class, () -> new CertificateService(repo, " ", senhas));
  }

  @Test
  void storesP12AndListsExpiryStatus() throws Exception {
    var repo = mock(DereEntityCertificateRepository.class);
    when(repo.findByEntityIdAndActiveTrue(3L)).thenReturn(List.of());
    when(repo.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
    var service = servico(repo);
    var stored = service.save(3L, "A1 EFPC", p12(), "changeit");
    assertEquals("dere-test", stored.getAliasName());
    assertTrue(stored.getSecretRef() != null && stored.getSecretRef().startsWith("local:"));
    assertTrue(stored.getPasswordCiphertext() == null);
    assertTrue(stored.getValidUntil().isAfter(Instant.now()));
    verify(repo).save(any(DereEntityCertificateEntity.class));

    when(repo.findByEntityIdAndActiveTrue(3L)).thenReturn(List.of(stored));
    var listed = service.list(3L);
    assertEquals(1, listed.size());
    assertEquals("EXPIRING_SOON", listed.get(0).status());
    assertEquals("DeRE POC Test", listed.get(0).subject());
    assertEquals("DeRE POC Test", listed.get(0).issuer());
    assertFalse(listed.get(0).icpBrasil());
    assertTrue(listed.get(0).issuedAt() != null);
  }

  @Test
  void loadsStoredCertificateForSigning() throws Exception {
    var repo = mock(DereEntityCertificateRepository.class);
    when(repo.findByEntityIdAndActiveTrue(3L)).thenReturn(List.of());
    when(repo.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
    var service = servico(repo);
    var stored = service.save(3L, "A1 EFPC", p12(), "changeit");
    when(repo.findById(9L)).thenReturn(Optional.of(stored));
    var loaded = service.loadForSigning(9L);
    assertEquals("dere-test", loaded.alias());
    assertTrue(loaded.keyStore().containsAlias("dere-test"));
  }

  @Test
  void migraSenhaAntigaDoBancoParaOCofre() throws Exception {
    var repo = mock(DereEntityCertificateRepository.class);
    var pasta = Files.createTempDirectory("cert-secrets-legacy");
    var chave = "local-poc-key-not-for-production";
    var box = new PocSecretBox(chave);
    var bytes = new ByteArrayOutputStream();
    LocalTestCertificate.create().store(bytes, "changeit".toCharArray());
    var legado = new DereEntityCertificateEntity(3L, "A1 EFPC", box.encrypt(bytes.toByteArray()), null, "dere-test", Instant.now().plusSeconds(86400));
    var campo = DereEntityCertificateEntity.class.getDeclaredField("passwordCiphertext");
    campo.setAccessible(true);
    campo.set(legado, box.encrypt("changeit"));
    when(repo.findById(9L)).thenReturn(Optional.of(legado));
    when(repo.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
    var service = new CertificateService(repo, chave, new LocalCertificatePasswordStore(pasta, chave));
    var loaded = service.loadForSigning(9L);
    assertEquals("dere-test", loaded.alias());
    assertTrue(legado.getSecretRef() != null && legado.getSecretRef().startsWith("local:"));
    assertTrue(legado.getPasswordCiphertext() == null);
  }

  @Test
  void desativaCertificadoDaEntidade() throws Exception {
    var repo = mock(DereEntityCertificateRepository.class);
    when(repo.findByEntityIdAndActiveTrue(3L)).thenReturn(List.of());
    when(repo.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
    var service = servico(repo);
    var stored = service.save(3L, "A1 EFPC", p12(), "changeit");
    when(repo.findById(9L)).thenReturn(Optional.of(stored));
    service.delete(3L, 9L);
    assertFalse(stored.isActive());
    verify(repo, org.mockito.Mockito.atLeastOnce()).save(stored);
  }

  private static CertificateService servico(DereEntityCertificateRepository repo) throws Exception {
    var pasta = Files.createTempDirectory("cert-secrets-test");
    return new CertificateService(repo, "local-poc-key-not-for-production", new LocalCertificatePasswordStore(pasta, "local-poc-key-not-for-production"));
  }

  private static MockMultipartFile p12() throws Exception {
    var keyStore = LocalTestCertificate.create();
    var bytes = new ByteArrayOutputStream();
    keyStore.store(bytes, "changeit".toCharArray());
    return new MockMultipartFile("file", "teste.p12", "application/x-pkcs12", bytes.toByteArray());
  }
}
