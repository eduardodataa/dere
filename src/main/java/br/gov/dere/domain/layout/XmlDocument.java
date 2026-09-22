package br.gov.dere.domain.layout;

public record XmlDocument(String content) {
  public XmlDocument {
    if (content == null || content.isBlank()) {
      throw new IllegalArgumentException("Documento XML vazio");
    }
  }
}
