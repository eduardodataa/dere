package br.gov.dere.domain.layout;

public record LayoutVersion(String value) {
  public LayoutVersion {
    if (value == null || value.isBlank()) {
      throw new IllegalArgumentException("Versão de layout obrigatória");
    }
  }
}
