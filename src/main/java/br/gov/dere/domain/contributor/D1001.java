package br.gov.dere.domain.contributor;

import java.time.LocalDate;
import java.util.List;

public record D1001(String id, int operation, int environment, String applicationVersion,
                    String cnpjRoot, LocalDate validFrom, LocalDate validTo,
                    int primaryTaxRegime, List<Integer> secondaryTaxRegimes, int taxNature) {
  public D1001 { secondaryTaxRegimes = secondaryTaxRegimes == null ? List.of() : List.copyOf(secondaryTaxRegimes); }
}
