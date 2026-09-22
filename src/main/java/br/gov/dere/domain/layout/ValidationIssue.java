package br.gov.dere.domain.layout;

public record ValidationIssue(String phase, String code, String field, String message, Severity severity) {
  public enum Severity {
    WARNING,
    ERROR
  }
}
