package br.gov.dere.domain.periodico;

import java.util.List;

public record D1101(
    String id,
    int operation,
    Integer exclusionReason,
    String processNumber,
    String receiptNumber,
    int environment,
    int application,
    String applicationVersion,
    String cnpjRoot,
    String period,
    List<D1101Conta> accounts) {
  public D1101 {
    accounts = accounts == null ? List.of() : List.copyOf(accounts);
  }
}
