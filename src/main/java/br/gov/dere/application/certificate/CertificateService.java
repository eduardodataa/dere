package br.gov.dere.application.certificate;

import br.gov.dere.integration.certificate.PocSecretBox;
import br.gov.dere.persistence.DereEntityCertificateEntity;
import br.gov.dere.persistence.DereEntityCertificateRepository;
import java.io.ByteArrayInputStream;
import java.security.KeyStore;
import java.security.cert.X509Certificate;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class CertificateService {
  private final DereEntityCertificateRepository repo;
  private final PocSecretBox box;

  public CertificateService(DereEntityCertificateRepository repo, @Value("${dere.certificate.encryption-key}") String key) {
    if (key == null || key.isBlank() || key.contains("change-me")) throw new IllegalStateException("Chave de proteção de certificado não configurada");
    this.repo = repo; this.box = new PocSecretBox(key);
  }

  @Transactional
  public DereEntityCertificateEntity save(Long entityId, String label, MultipartFile file, String password) throws Exception {
    if (file.isEmpty()) throw new IllegalArgumentException("Arquivo vazio");
    var keyStore = KeyStore.getInstance("PKCS12");
    try (var input = file.getInputStream()) { keyStore.load(input, password.toCharArray()); }
    var aliases = keyStore.aliases(); if (!aliases.hasMoreElements()) throw new IllegalArgumentException("P12 não contém certificado");
    String alias = aliases.nextElement();
    if (!(keyStore.getCertificate(alias) instanceof X509Certificate certificate)) throw new IllegalArgumentException("P12 não contém certificado X509");
    if (!keyStore.isKeyEntry(alias)) throw new IllegalArgumentException("P12 não contém chave privada");
    repo.findByEntityIdAndActiveTrue(entityId).forEach(current -> { current.deactivate(); repo.save(current); });
    var entity = new DereEntityCertificateEntity(entityId, label, box.encrypt(file.getBytes()), box.encrypt(password), alias, certificate.getNotAfter().toInstant());
    return repo.save(entity);
  }

  public LoadedCertificate loadForSigning(Long id) throws Exception {
    var entity = repo.findById(id).orElseThrow();
    if (!entity.isActive()) throw new IllegalStateException("Certificado inativo");
    if (entity.getValidUntil() == null || entity.getValidUntil().isBefore(Instant.now())) throw new IllegalStateException("Certificado expirado");
    var password = box.decrypt(entity.getPasswordCiphertext()).toCharArray();
    var keyStore = KeyStore.getInstance("PKCS12");
    keyStore.load(new ByteArrayInputStream(box.decrypt(entity.getP12Ciphertext())), password);
    return new LoadedCertificate(keyStore, password, entity.getAliasName());
  }

  public List<CertificateDto> list(Long entityId) {
    return repo.findByEntityIdAndActiveTrue(entityId).stream().map(entity -> {
      long days = entity.getValidUntil() == null ? -1 : Duration.between(Instant.now(), entity.getValidUntil()).toDays();
      String status = days < 0 ? "EXPIRED" : days <= 30 ? "EXPIRING_SOON" : "VALID";
      return new CertificateDto(entity.getId(), entity.getLabel(), entity.getAliasName(), entity.getValidUntil(), status, days);
    }).toList();
  }

  public record CertificateDto(Long id, String label, String alias, Instant validUntil, String status, long daysUntilExpiry) {}
  public record LoadedCertificate(KeyStore keyStore, char[] password, String alias) {}
}
