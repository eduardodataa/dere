package br.gov.dere.application.validation.relatorio;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Critica(
    String arquivo,
    Integer linha,
    String coluna,
    String valorEncontrado,
    TipoCritica tipo,
    String valorEsperado,
    String problema,
    String caminhoXml) {
  @JsonProperty
  public String rotuloTipo() {
    return tipo == null ? "" : tipo.rotulo();
  }
}
