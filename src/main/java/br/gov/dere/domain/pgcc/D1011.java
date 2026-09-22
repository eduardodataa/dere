package br.gov.dere.domain.pgcc;

import java.time.LocalDate;
import java.util.List;

public record D1011(
    String id,
    int operation,
    Integer exclusionReason,
    String processNumber,
    int environment,
    int application,
    String applicationVersion,
    String cnpjRoot,
    LocalDate validFrom,
    LocalDate validTo,
    String referenceChart,
    String closingFrequency,
    List<PGCCAccount> accounts) {
  public D1011 {
    accounts = accounts == null ? List.of() : List.copyOf(accounts);
  }
}
