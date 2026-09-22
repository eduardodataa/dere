package br.gov.dere.domain.layout;

import java.util.List;

public record ValidationResult(List<ValidationIssue> issues) {
  public ValidationResult {
    issues = issues == null ? List.of() : List.copyOf(issues);
  }

  public static ValidationResult success() {
    return new ValidationResult(List.of());
  }

  public boolean valid() {
    return issues.stream().noneMatch(issue -> issue.severity() == ValidationIssue.Severity.ERROR);
  }
}
