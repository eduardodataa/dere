package br.gov.dere.domain.layout;

public record ProblemaValidacao(String phase, String code, String field, String message, Severity severity) {
  public enum Severity {
    WARNING,
    ERROR
  }
}
