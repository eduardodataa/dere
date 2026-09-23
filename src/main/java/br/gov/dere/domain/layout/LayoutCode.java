package br.gov.dere.domain.layout;

public enum LayoutCode {
  D1001("D-1001"),
  D1011("D-1011"),
  D1101("D-1101"),
  D1199("D-1199");

  private final String displayName;

  LayoutCode(String displayName) {
    this.displayName = displayName;
  }

  public String displayName() {
    return displayName;
  }
}
