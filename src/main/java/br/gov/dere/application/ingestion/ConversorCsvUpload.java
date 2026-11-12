package br.gov.dere.application.ingestion;

import br.gov.dere.application.csv.DereCsvConverter;
import br.gov.dere.application.d1001.D1001XmlGenerator;
import br.gov.dere.application.periodico.D1101CsvConverter;
import br.gov.dere.application.periodico.D1101XmlGenerator;
import br.gov.dere.application.periodico.D1199CsvConverter;
import br.gov.dere.application.periodico.D1199XmlGenerator;
import br.gov.dere.application.pgcc.D1011CsvConverter;
import br.gov.dere.application.pgcc.D1011XmlGenerator;

public final class ConversorCsvUpload {
  private static final DereCsvConverter CSV_D1001 = new DereCsvConverter();
  private static final D1001XmlGenerator XML_D1001 = new D1001XmlGenerator();
  private static final D1011CsvConverter CSV_D1011 = new D1011CsvConverter();
  private static final D1011XmlGenerator XML_D1011 = new D1011XmlGenerator();
  private static final D1101CsvConverter CSV_D1101 = new D1101CsvConverter();
  private static final D1101XmlGenerator XML_D1101 = new D1101XmlGenerator();
  private static final D1199CsvConverter CSV_D1199 = new D1199CsvConverter();
  private static final D1199XmlGenerator XML_D1199 = new D1199XmlGenerator();

  private ConversorCsvUpload() {}

  public static String paraXml(String csv) throws Exception {
    var leiaute = detectar(csv);
    return switch (leiaute) {
      case "D-1011" -> XML_D1011.generate(CSV_D1011.fromCsv(csv));
      case "D-1101" -> XML_D1101.generate(CSV_D1101.fromCsv(csv));
      case "D-1199" -> XML_D1199.generate(CSV_D1199.fromCsv(csv));
      default -> XML_D1001.generate(CSV_D1001.fromCsv(csv));
    };
  }

  static String detectar(String csv) {
    var texto = csv == null ? "" : csv.startsWith("\uFEFF") ? csv.substring(1) : csv;
    var primeira = texto.lines().filter(l -> !l.isBlank()).findFirst().orElse("");
    var cabecalho = primeira.toLowerCase();
    if (cabecalho.contains("planctaref") || cabecalho.contains("freqencerr") || cabecalho.contains("nomecta")) {
      return "D-1011";
    }
    if (cabecalho.contains("codbcn") || cabecalho.contains("usarbcnacum")) return "D-1199";
    if (cabecalho.contains("perapur") && cabecalho.contains("ccta")) return "D-1101";
    return "D-1001";
  }
}
