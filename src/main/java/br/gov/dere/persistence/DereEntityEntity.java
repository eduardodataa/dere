package br.gov.dere.persistence;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.*;

@Entity
@Table(name = "dere_entity")
public class DereEntityEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(name = "cnpj_root", nullable = false, unique = true)
  private String cnpjRoot;
  @Column(name = "legal_name", nullable = false)
  private String legalName;
  private boolean active = true;
  @Column(name = "created_at")
  private Instant createdAt = Instant.now();
  @Column(name = "client_id")
  private String clientId;
  @Column(name = "client_secret", length = 2048)
  private String clientSecret;
  @Column(name = "credential_valid_until")
  private String validadeCredencial;
  @ManyToMany(mappedBy = "entities")
  private Set<DereUserEntity> users = new HashSet<>();

  protected DereEntityEntity() {}

  public DereEntityEntity(String c, String n) {
    cnpjRoot = c;
    legalName = n;
  }

  public Long getId() {
    return id;
  }

  public String getCnpjRoot() {
    return cnpjRoot;
  }

  public String getLegalName() {
    return legalName;
  }

  public String getClientId() {
    return clientId;
  }

  public String getClientSecret() {
    return clientSecret;
  }

  public String getValidadeCredencial() {
    return validadeCredencial;
  }

  public Set<DereUserEntity> getUsers() {
    return users;
  }

  public void update(String c, String n) {
    cnpjRoot = c;
    legalName = n;
  }

  public void clientId(String valor) {
    clientId = valor == null || valor.isBlank() ? null : valor;
  }

  public void clientSecret(String valor) {
    clientSecret = valor == null || valor.isBlank() ? null : valor;
  }

  public void validadeCredencial(String valor) {
    validadeCredencial = valor == null || valor.isBlank() ? null : valor;
  }
}
