package br.gov.dere.application.pgcc;

import br.gov.dere.domain.pgcc.D1011;
import br.gov.dere.domain.pgcc.PGCCAccount;
import br.gov.dere.integration.xml.XmlSupport;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.w3c.dom.Element;

public class D1011CsvConverter {
  public static final String HEADER = "id;motExcl;nrProc;tpOper;tpAmb;aplicEmi;verAplic;nrInsc;iniValid;fimValid;planoCtaRef;freqEncerr;cCta;cCtaInterna;cDbrMista;nomeCta;indCta;descCta;cCtaSup;cCtaRef;nivelCta;natCta;codNat;codTrib;indTribISS;idLeiDisp;iniVig;fimVig";

  public D1011 fromCsv(String csv) {
    var text = csv == null ? "" : csv.startsWith("\uFEFF") ? csv.substring(1) : csv;
    var lines = text.lines().filter(line -> !line.isBlank()).toList();
    if (lines.size() < 2) throw new IllegalArgumentException("CSV deve conter cabeçalho e ao menos uma linha");
    var delimiter = delimiter(lines.get(0));
    var rows = lines.stream().map(line -> parseLine(line, delimiter)).toList();
    var headers = index(rows.get(0));
    var firstGet = getter(headers, rows.get(1));
    var operation = integer(firstGet, "tpOper");
    if (operation == 3) {
      return new D1011(required(firstGet, "id"), operation, optionalInteger(firstGet, "motExcl"),
          firstGet.apply("nrProc"), integer(firstGet, "tpAmb"), integer(firstGet, "aplicEmi"), required(firstGet, "verAplic"),
          required(firstGet, "nrInsc"), date(firstGet, "iniValid"), date(firstGet, "fimValid"),
          blankToNull(firstGet.apply("planoCtaRef")), blankToNull(firstGet.apply("freqEncerr")), List.of());
    }
    var accounts = new ArrayList<PGCCAccount>();
    D1011 first = null;
    for (int rowIndex = 1; rowIndex < rows.size(); rowIndex++) {
      var values = rows.get(rowIndex);
      var get = getter(headers, values);
      var account = new PGCCAccount(
          required(get, "cCta"), get.apply("cCtaInterna"), integer(get, "cDbrMista"), required(get, "nomeCta"),
          required(get, "indCta"), blankToNull(get.apply("descCta")), blankToNull(get.apply("cCtaSup")), required(get, "cCtaRef"),
          integer(get, "nivelCta"), required(get, "natCta"), required(get, "codNat"), blankToNull(get.apply("codTrib")),
          optionalInteger(get, "indTribISS"), blankToNull(get.apply("idLeiDisp")), date(get, "iniVig"), date(get, "fimVig"));
      accounts.add(account);
      if (first == null) {
        first = new D1011(required(get, "id"), integer(get, "tpOper"), optionalInteger(get, "motExcl"),
            get.apply("nrProc"), integer(get, "tpAmb"), integer(get, "aplicEmi"), required(get, "verAplic"),
            required(get, "nrInsc"), date(get, "iniValid"), date(get, "fimValid"), required(get, "planoCtaRef"),
            required(get, "freqEncerr"), List.of());
      }
    }
    return new D1011(first.id(), first.operation(), first.exclusionReason(), first.processNumber(), first.environment(),
        first.application(), first.applicationVersion(), first.cnpjRoot(), first.validFrom(), first.validTo(),
        first.referenceChart(), first.closingFrequency(), accounts);
  }

  public String toCsv(D1011 document) {
    var out = new StringBuilder(HEADER).append('\n');
    if (document.accounts().isEmpty()) {
      out.append(String.join(";", csv(document.id()), csv(document.exclusionReason()), csv(document.processNumber()),
          csv(document.operation()), csv(document.environment()), csv(document.application()), csv(document.applicationVersion()),
          csv(document.cnpjRoot()), csv(document.validFrom()), csv(document.validTo()), csv(document.referenceChart()),
          csv(document.closingFrequency()), "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "")).append('\n');
      return out.toString();
    }
    for (var account : document.accounts()) {
      out.append(String.join(";", csv(document.id()), csv(document.exclusionReason()), csv(document.processNumber()),
          csv(document.operation()), csv(document.environment()), csv(document.application()), csv(document.applicationVersion()),
          csv(document.cnpjRoot()), csv(document.validFrom()), csv(document.validTo()), csv(document.referenceChart()),
          csv(document.closingFrequency()), csv(account.accountCode()), csv(account.internalCode()), csv(debitCredit(account.mixedDebitCredit())),
          csv(account.name()), csv(account.accountIndicator()), csv(account.description()), csv(account.parentCode()),
          csv(account.referenceCode()), csv(account.level()), csv(account.nature()), csv(account.natureCode()), csv(account.taxCode()),
          csv(account.issTaxIndicator()), csv(account.legalBasis()), csv(account.validFrom()), csv(account.validTo()))).append('\n');
    }
    return out.toString();
  }

  public D1011 fromXml(String xml) throws Exception {
    var document = XmlSupport.parse(xml);
    var event = (Element) document.getElementsByTagNameNS(D1011XmlGenerator.NS, "evtPGCC").item(0);
    if (event == null) throw new IllegalArgumentException("XML não contém evtPGCC");
    var ide = child(event, "ideEvento");
    var contributor = child(event, "ideContrib");
    var period = child(event, "idePeriodo");
    var info = child(event, "infoPGCC");
    var accounts = new ArrayList<PGCCAccount>();
    var nodes = info == null ? null : info.getElementsByTagNameNS(D1011XmlGenerator.NS, "infoConta");
    if (nodes != null) for (int i = 0; i < nodes.getLength(); i++) {
      var account = (Element) nodes.item(i);
      accounts.add(new PGCCAccount(text(account, "cCta"), text(account, "cCtaInterna"), number(account, "cDbrMista"),
          text(account, "nomeCta"), text(account, "indCta"), blankToNull(text(account, "descCta")), blankToNull(text(account, "cCtaSup")),
          text(account, "cCtaRef"), number(account, "nivelCta"), text(account, "natCta"), text(account, "codNat"),
          blankToNull(text(account, "codTrib")), number(account, "indTribISS"), blankToNull(text(account, "idLeiDisp")), date(account, "iniVig"), date(account, "fimVig")));
    }
    return new D1011(event.getAttribute("id"), number(ide, "tpOper"), number(ide, "motExcl"), text(ide, "nrProc"),
        number(ide, "tpAmb"), number(ide, "aplicEmi"), text(ide, "verAplic"), text(contributor, "nrInsc"),
        date(period, "iniValid"), date(period, "fimValid"), text(info, "planoCtaRef"), text(info, "freqEncerr"), accounts);
  }

  private static char delimiter(String header) {
    long semi = header.chars().filter(c -> c == ';').count();
    long comma = header.chars().filter(c -> c == ',').count();
    return semi > comma ? ';' : ',';
  }

  private List<String> parseLine(String line, char delimiter) {
    var values = new ArrayList<String>();
    var current = new StringBuilder();
    boolean quoted = false;
    for (int i = 0; i < line.length(); i++) {
      char c = line.charAt(i);
      if (c == '"') {
        if (quoted && i + 1 < line.length() && line.charAt(i + 1) == '"') { current.append('"'); i++; }
        else quoted = !quoted;
      } else if (c == delimiter && !quoted) { values.add(current.toString().trim()); current.setLength(0); }
      else current.append(c);
    }
    values.add(current.toString().trim());
    return values;
  }

  private static Map<String, Integer> index(List<String> headers) {
    var index = new LinkedHashMap<String, Integer>();
    for (int i = 0; i < headers.size(); i++) index.put(headers.get(i), i);
    return index;
  }

  private static java.util.function.Function<String, String> getter(Map<String, Integer> headers, List<String> values) {
    return name -> { var index = headers.get(name); return index == null || index >= values.size() ? "" : values.get(index); };
  }

  private static String required(java.util.function.Function<String, String> get, String name) {
    var value = get.apply(name);
    if (value == null || value.isBlank()) throw new IllegalArgumentException("Campo obrigatório ausente: " + name);
    return value;
  }

  private static Integer integer(java.util.function.Function<String, String> get, String name) {
    return Integer.valueOf(required(get, name));
  }

  private static Integer optionalInteger(java.util.function.Function<String, String> get, String name) {
    var value = get.apply(name);
    return value == null || value.isBlank() ? null : Integer.valueOf(value);
  }

  private static LocalDate date(java.util.function.Function<String, String> get, String name) {
    var value = get.apply(name);
    if (value == null || value.isBlank()) return null;
    if (value.length() == 10 && value.charAt(2) == '/') return LocalDate.parse(value, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    return LocalDate.parse(value);
  }

  private static String debitCredit(Integer value) { return value == null ? "" : String.format("%03d", value); }
  private static String blankToNull(String value) { return value == null || value.isBlank() ? null : value; }
  private static String csv(Object value) {
    if (value == null) return "";
    if (value instanceof LocalDate date) return date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    if (value instanceof Number && !(value instanceof Byte || value instanceof Short || value instanceof Integer || value instanceof Long)) {
      return String.valueOf(value).replace('.', ',');
    }
    var text = String.valueOf(value);
    if (text.matches("-?\\d+\\.\\d+")) return text.replace('.', ',');
    return text.contains(";") || text.contains("\"") || text.contains("\n")
        ? "\"" + text.replace("\"", "\"\"") + "\"" : text;
  }

  private static Element child(Element parent, String name) { if (parent == null) return null; var node = parent.getElementsByTagNameNS(D1011XmlGenerator.NS, name); return node.getLength() == 0 ? null : (Element) node.item(0); }
  private static String text(Element parent, String name) { var child = child(parent, name); return child == null ? "" : child.getTextContent(); }
  private static Integer number(Element parent, String name) { var value = text(parent, name); return value.isBlank() ? null : Integer.valueOf(value); }
  private static LocalDate date(Element parent, String name) { var value = text(parent, name); return value.isBlank() ? null : LocalDate.parse(value); }
}
