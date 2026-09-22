package br.gov.dere.domain.layout;

public enum LayoutCode {
  D1001("D-1001"),
  D1011("D-1011");

  private final String displayName;

  LayoutCode(String displayName) {
    this.displayName = displayName;
  }

  public String displayName() {
    return displayName;
  }
}
