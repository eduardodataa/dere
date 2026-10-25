package br.gov.dere.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

@Entity
@Table(name = "dere_entity_logo")
public class DereEntidadeLogoEntity {
  @Id
  @Column(name = "entity_id")
  private Long entityId;
  @Column(nullable = false, length = 64)
  private String tipo;
  @Lob
  @Column(nullable = false)
  private byte[] conteudo;

  protected DereEntidadeLogoEntity() {}

  public DereEntidadeLogoEntity(Long entityId) {
    this.entityId = entityId;
  }

  public Long getEntityId() { return entityId; }
  public String getTipo() { return tipo; }
  public byte[] getConteudo() { return conteudo; }

  public void definir(String tipo, byte[] conteudo) {
    this.tipo = tipo;
    this.conteudo = conteudo;
  }
}
