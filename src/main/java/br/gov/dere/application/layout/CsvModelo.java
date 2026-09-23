package br.gov.dere.application.layout;

import br.gov.dere.application.csv.DereCsvConverter;
import br.gov.dere.application.periodico.D1101CsvConverter;
import br.gov.dere.application.periodico.D1199CsvConverter;
import br.gov.dere.application.pgcc.D1011CsvConverter;

public final class CsvModelo {
  private CsvModelo() {}

  public static String cabecalho(String leiaute) {
    if ("D-1011".equals(leiaute)) return D1011CsvConverter.HEADER;
    if ("D-1101".equals(leiaute)) return D1101CsvConverter.HEADER;
    if ("D-1199".equals(leiaute)) return D1199CsvConverter.HEADER;
    return DereCsvConverter.HEADER;
  }

  public static String normalizar(String codigo) {
    if (codigo == null) return "D-1001";
    var valor = codigo.trim().toUpperCase().replace('_', '-');
    if (valor.startsWith("D") && !valor.startsWith("D-")) valor = "D-" + valor.substring(1);
    return valor;
  }
}
