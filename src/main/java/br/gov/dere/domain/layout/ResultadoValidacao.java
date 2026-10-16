package br.gov.dere.domain.layout;

import java.util.List;

public record ResultadoValidacao(List<ProblemaValidacao> issues) {
  public ResultadoValidacao {
    issues = issues == null ? List.of() : List.copyOf(issues);
  }

  public static ResultadoValidacao success() {
    return new ResultadoValidacao(List.of());
  }

  public boolean valid() {
    return issues.stream().noneMatch(issue -> issue.severity() == ProblemaValidacao.Severity.ERROR);
  }
}
