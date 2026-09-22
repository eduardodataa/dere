package br.gov.dere.domain.pgcc;

import java.time.LocalDate;

public record PGCCAccount(
    String accountCode,
    String internalCode,
    Integer mixedDebitCredit,
    String name,
    String accountIndicator,
    String description,
    String parentCode,
    String referenceCode,
    Integer level,
    String nature,
    String natureCode,
    String taxCode,
    Integer issTaxIndicator,
    String legalBasis,
    LocalDate validFrom,
    LocalDate validTo) {
}
