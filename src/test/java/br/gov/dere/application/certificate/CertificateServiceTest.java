package br.gov.dere.application.certificate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.gov.dere.integration.signature.LocalTestCertificate;
import br.gov.dere.persistence.DereEntityCertificateEntity;
import br.gov.dere.persistence.DereEntityCertificateRepository;
import java.io.ByteArrayOutputStream;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

class CertificateServiceTest {
  @Test
  void rejectsUnconfiguredProtectionKey() {
    var repo = mock(DereEntityCertificateRepository.class);
    assertThrows(IllegalStateException.class, () -> new CertificateService(repo, "local-poc-change-me"));
    assertThrows(IllegalStateException.class, () -> new CertificateService(repo, " "));
  }

  @Test
  void storesP12AndListsExpiryStatus() throws Exception {
    var repo = mock(DereEntityCertificateRepository.class);
    when(repo.findByEntityIdAndActiveTrue(3L)).thenReturn(List.of());
    when(repo.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
    var service = new CertificateService(repo, "local-poc-key-not-for-production");
    var stored = service.save(3L, "A1 EFPC", p12(), "changeit");
    assertEquals("dere-test", stored.getAliasName());
    assertTrue(stored.getValidUntil().isAfter(Instant.now()));
    verify(repo).save(any(DereEntityCertificateEntity.class));

    when(repo.findByEntityIdAndActiveTrue(3L)).thenReturn(List.of(stored));
    var listed = service.list(3L);
    assertEquals(1, listed.size());
    assertEquals("EXPIRING_SOON", listed.get(0).status());
  }

  @Test
  void loadsStoredCertificateForSigning() throws Exception {
    var repo = mock(DereEntityCertificateRepository.class);
    when(repo.findByEntityIdAndActiveTrue(3L)).thenReturn(List.of());
    when(repo.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
    var service = new CertificateService(repo, "local-poc-key-not-for-production");
    var stored = service.save(3L, "A1 EFPC", p12(), "changeit");
    when(repo.findById(9L)).thenReturn(Optional.of(stored));
    var loaded = service.loadForSigning(9L);
    assertEquals("dere-test", loaded.alias());
    assertTrue(loaded.keyStore().containsAlias("dere-test"));
  }

  private static MockMultipartFile p12() throws Exception {
    var keyStore = LocalTestCertificate.create();
    var bytes = new ByteArrayOutputStream();
    keyStore.store(bytes, "changeit".toCharArray());
    return new MockMultipartFile("file", "teste.p12", "application/x-pkcs12", bytes.toByteArray());
  }
}
