package br.gov.dere.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "dere_user")
public class DereUserEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(nullable = false, unique = true)
  private String login;
  @Column(name = "password_hash", nullable = false)
  private String passwordHash;
  @Column(name = "display_name", nullable = false)
  private String displayName;
  @Column(name = "email")
  private String email;
  private boolean active = true;
  @Column(name = "is_master", nullable = false)
  private boolean master = false;
  @Column(name = "created_at")
  private Instant createdAt = Instant.now();
  @ManyToMany
  @JoinTable(name = "dere_user_entity", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "entity_id"))
  private Set<DereEntityEntity> entities = new HashSet<>();

  protected DereUserEntity() {}

  public DereUserEntity(String login, String passwordHash, String displayName, String email) {
    this.login = login;
    this.passwordHash = passwordHash;
    this.displayName = displayName;
    this.email = email;
  }

  public Long getId() {
    return id;
  }

  public String getLogin() {
    return login;
  }

  public String getPasswordHash() {
    return passwordHash;
  }

  public String getDisplayName() {
    return displayName;
  }

  public String getEmail() {
    return email;
  }

  public boolean isMaster() {
    return master;
  }

  public void setMaster(boolean value) {
    master = value;
  }

  public Set<DereEntityEntity> getEntities() {
    return entities;
  }

  public void update(String newLogin, String name, String hash, String newEmail) {
    if (newLogin != null && !newLogin.isBlank()) login = newLogin.trim();
    if (name != null && !name.isBlank()) displayName = name.trim();
    if (hash != null && !hash.isBlank()) passwordHash = hash;
    if (newEmail != null) email = newEmail.isBlank() ? null : newEmail.trim();
  }

  public void definirSenha(String hash) {
    passwordHash = hash;
  }
}
