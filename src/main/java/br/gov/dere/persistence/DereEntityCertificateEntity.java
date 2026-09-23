package br.gov.dere.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "dere_entity_certificate")
public class DereEntityCertificateEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(name = "entity_id", nullable = false)
  private Long entityId;
  @Column(nullable = false)
  private String label;
  @Lob
  @Column(name = "p12_ciphertext", nullable = false)
  private byte[] p12Ciphertext;
  @Column(name = "password_ciphertext", length = 1000)
  private String passwordCiphertext;
  @Column(name = "secret_ref")
  private String secretRef;
  @Column(name = "alias_name")
  private String aliasName;
  @Column(name = "valid_until")
  private Instant validUntil;
  private boolean active = true;
  @Column(name = "created_at")
  private Instant createdAt = Instant.now();

  protected DereEntityCertificateEntity() {}

  public DereEntityCertificateEntity(Long entityId, String label, byte[] p12, String secretRef, String alias, Instant until) {
    this.entityId = entityId;
    this.label = label;
    this.p12Ciphertext = p12;
    this.secretRef = secretRef;
    this.aliasName = alias;
    this.validUntil = until;
  }

  public void deactivate() {
    active = false;
  }

  public void secretRef(String secretRef) {
    this.secretRef = secretRef;
  }

  public void clearPasswordCiphertext() {
    passwordCiphertext = null;
  }

  public Long getId() {
    return id;
  }

  public Long getEntityId() {
    return entityId;
  }

  public String getLabel() {
    return label;
  }

  public String getAliasName() {
    return aliasName;
  }

  public Instant getValidUntil() {
    return validUntil;
  }

  public boolean isActive() {
    return active;
  }

  public byte[] getP12Ciphertext() {
    return p12Ciphertext;
  }

  public String getPasswordCiphertext() {
    return passwordCiphertext;
  }

  public String getSecretRef() {
    return secretRef;
  }
}
