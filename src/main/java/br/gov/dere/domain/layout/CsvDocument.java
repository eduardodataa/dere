package br.gov.dere.domain.layout;

public record CsvDocument(String content) {
  public CsvDocument {
    if (content == null || content.isBlank()) {
      throw new IllegalArgumentException("Documento CSV vazio");
    }
  }
}
