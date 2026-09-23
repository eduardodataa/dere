package br.gov.dere.domain.periodico;

import java.util.List;

public record D1199(
    String id,
    int operation,
    int environment,
    int application,
    String applicationVersion,
    String cnpjRoot,
    String period,
    List<D1199Base> bases,
    List<D1199Detalhe> detalhes) {
  public D1199 {
    bases = bases == null ? List.of() : List.copyOf(bases);
    detalhes = detalhes == null ? List.of() : List.copyOf(detalhes);
  }
}
