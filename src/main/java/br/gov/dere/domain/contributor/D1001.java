package br.gov.dere.domain.contributor;

import java.time.LocalDate;
import java.util.List;

/** Modelo canônico do evento D-1001 conforme evtInfoContrib-v1_0_1.xsd. */
public record D1001(String id, int operation, Integer exclusionReason, String processNumber,
                    int environment, int application, String applicationVersion, String cnpjRoot,
                    LocalDate validFrom, LocalDate validTo, LocalDate newValidFrom, LocalDate newValidTo,
                    int primaryTaxRegime, List<Integer> secondaryTaxRegimes, int taxNature,
                    List<String> financialActivities, List<String> healthActivities,
                    List<String> prognosticActivities, List<Integer> accreditedUfs) {
  public D1001 {
    secondaryTaxRegimes = secondaryTaxRegimes == null ? List.of() : List.copyOf(secondaryTaxRegimes);
    financialActivities = financialActivities == null ? List.of() : List.copyOf(financialActivities);
    healthActivities = healthActivities == null ? List.of() : List.copyOf(healthActivities);
    prognosticActivities = prognosticActivities == null ? List.of() : List.copyOf(prognosticActivities);
    accreditedUfs = accreditedUfs == null ? List.of() : List.copyOf(accreditedUfs);
  }
  public D1001(String id, int operation, int environment, String applicationVersion,
               String cnpjRoot, LocalDate validFrom, LocalDate validTo, int primaryTaxRegime,
               List<Integer> secondaryTaxRegimes, int taxNature) {
    this(id, operation, null, null, environment, 1, applicationVersion, cnpjRoot, validFrom, validTo,
         null, null, primaryTaxRegime, secondaryTaxRegimes, taxNature, List.of(), List.of(), List.of(), List.of());
  }
}
