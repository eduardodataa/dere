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
import javax.security.auth.x500.X500Principal;
import org.bouncycastle.asn1.x509.CertificatePolicies;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class CertificateService {
  private final DereEntityCertificateRepository repo;
  private final PocSecretBox box;
  private final CertificatePasswordStore senhas;

  public CertificateService(
      DereEntityCertificateRepository repo,
      @Value("${dere.certificate.encryption-key}") String key,
      CertificatePasswordStore senhas) {
    if (key == null || key.isBlank() || key.contains("change-me")) throw new IllegalStateException("Chave de proteção de certificado não configurada");
    this.repo = repo;
    this.box = new PocSecretBox(key);
    this.senhas = senhas;
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
    repo.findByEntityIdAndActiveTrue(entityId).forEach(current -> {
      current.deactivate();
      apagarSenha(current);
      repo.save(current);
    });
    var referencia = senhas.guardar(entityId, password);
    var entity = new DereEntityCertificateEntity(entityId, label, box.encrypt(file.getBytes()), referencia, alias, certificate.getNotAfter().toInstant());
    return repo.save(entity);
  }

  @Transactional
  public void delete(Long entityId, Long id) {
    var entity = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Certificado não encontrado"));
    if (!entityId.equals(entity.getEntityId())) throw new IllegalArgumentException("Certificado de outra entidade");
    entity.deactivate();
    apagarSenha(entity);
    repo.save(entity);
  }

  public LoadedCertificate loadForSigning(Long id) throws Exception {
    var entity = repo.findById(id).orElseThrow();
    if (!entity.isActive()) throw new IllegalStateException("Certificado inativo");
    if (entity.getValidUntil() == null || entity.getValidUntil().isBefore(Instant.now())) throw new IllegalStateException("Certificado expirado");
    var password = senhaDe(entity).toCharArray();
    var keyStore = KeyStore.getInstance("PKCS12");
    keyStore.load(new ByteArrayInputStream(box.decrypt(entity.getP12Ciphertext())), password);
    return new LoadedCertificate(keyStore, password, entity.getAliasName());
  }

  public List<CertificateDto> list(Long entityId) {
    return repo.findByEntityIdAndActiveTrue(entityId).stream().map(this::visao).toList();
  }

  private CertificateDto visao(DereEntityCertificateEntity entity) {
    long days = entity.getValidUntil() == null ? -1 : Duration.between(Instant.now(), entity.getValidUntil()).toDays();
    String status = days < 0 ? "EXPIRED" : days <= 30 ? "EXPIRING_SOON" : "VALID";
    var detalhe = detalhe(entity);
    return new CertificateDto(
        entity.getId(),
        entity.getLabel(),
        entity.getAliasName(),
        detalhe.subject(),
        detalhe.issuer(),
        detalhe.issuedAt(),
        entity.getValidUntil(),
        detalhe.icpBrasil(),
        status,
        days);
  }

  private String senhaDe(DereEntityCertificateEntity entity) throws Exception {
    if (entity.getSecretRef() != null && !entity.getSecretRef().isBlank()) {
      return senhas.ler(entity.getSecretRef());
    }
    if (entity.getPasswordCiphertext() != null && !entity.getPasswordCiphertext().isBlank()) {
      var senha = box.decrypt(entity.getPasswordCiphertext());
      var referencia = senhas.guardar(entity.getEntityId(), senha);
      entity.secretRef(referencia);
      entity.clearPasswordCiphertext();
      repo.save(entity);
      return senha;
    }
    throw new IllegalStateException("Senha do certificado não encontrada no cofre");
  }

  private void apagarSenha(DereEntityCertificateEntity entity) {
    if (entity.getSecretRef() == null || entity.getSecretRef().isBlank()) return;
    senhas.apagar(entity.getSecretRef());
    entity.secretRef(null);
  }

  private CertificadoLido detalhe(DereEntityCertificateEntity entity) {
    try {
      var senha = senhaDe(entity).toCharArray();
      var keyStore = KeyStore.getInstance("PKCS12");
      keyStore.load(new ByteArrayInputStream(box.decrypt(entity.getP12Ciphertext())), senha);
      var alias = entity.getAliasName() != null && keyStore.containsAlias(entity.getAliasName())
          ? entity.getAliasName()
          : keyStore.aliases().nextElement();
      if (!(keyStore.getCertificate(alias) instanceof X509Certificate certificado)) {
        return CertificadoLido.vazio();
      }
      return new CertificadoLido(
          cn(certificado.getSubjectX500Principal()),
          cn(certificado.getIssuerX500Principal()),
          certificado.getNotBefore().toInstant(),
          icpBrasil(certificado));
    } catch (Exception ignorado) {
      return CertificadoLido.vazio();
    }
  }

  private static String cn(X500Principal principal) {
    if (principal == null) return "";
    var nome = principal.getName();
    for (var parte : nome.split(",")) {
      var item = parte.trim();
      if (item.regionMatches(true, 0, "CN=", 0, 3)) return item.substring(3).trim();
    }
    return nome;
  }

  private static boolean icpBrasil(X509Certificate certificado) {
    if (contemIcp(certificado.getSubjectX500Principal()) || contemIcp(certificado.getIssuerX500Principal())) return true;
    try {
      var holder = new JcaX509CertificateHolder(certificado);
      if (holder.getExtensions() == null) return false;
      var politicas = CertificatePolicies.fromExtensions(holder.getExtensions());
      if (politicas == null) return false;
      for (var info : politicas.getPolicyInformation()) {
        var oid = info.getPolicyIdentifier().getId();
        if (oid != null && oid.startsWith("2.16.76.1")) return true;
      }
    } catch (Exception ignorado) {
      return false;
    }
    return false;
  }

  private static boolean contemIcp(X500Principal principal) {
    if (principal == null) return false;
    var nome = principal.getName().toUpperCase();
    return nome.contains("ICP-BRASIL") || nome.contains("ICP BRASIL") || nome.contains("AUTORIDADE CERTIFICADORA RAIZ BRASILEIRA");
  }

  public record CertificateDto(
      Long id,
      String label,
      String alias,
      String subject,
      String issuer,
      Instant issuedAt,
      Instant validUntil,
      boolean icpBrasil,
      String status,
      long daysUntilExpiry) {}

  public record LoadedCertificate(KeyStore keyStore, char[] password, String alias) {}

  private record CertificadoLido(String subject, String issuer, Instant issuedAt, boolean icpBrasil) {
    static CertificadoLido vazio() {
      return new CertificadoLido("", "", null, false);
    }
  }
}
