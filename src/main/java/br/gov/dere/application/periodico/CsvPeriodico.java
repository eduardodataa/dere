package br.gov.dere.application.periodico;

public final class CsvPeriodico {
  private CsvPeriodico() {}

  public static String dinheiroXml(String valor) {
    if (valor == null || valor.isBlank()) return null;
    var texto = valor.trim().replace(" ", "");
    if (texto.contains(",") && texto.contains(".")) texto = texto.replace(".", "").replace(",", ".");
    else texto = texto.replace(",", ".");
    if (!texto.contains(".")) texto = texto + ".00";
    var partes = texto.split("\\.", 2);
    var inteiro = partes[0].replaceFirst("^0+(?!$)", "");
    if (inteiro.isEmpty()) inteiro = "0";
    var decimal = partes.length > 1 ? (partes[1] + "00").substring(0, 2) : "00";
    return inteiro + "." + decimal;
  }

  public static String csv(Object valor) {
    if (valor == null) return "";
    var texto = String.valueOf(valor);
    if (texto.matches("-?\\d+\\.\\d+")) texto = texto.replace('.', ',');
    return texto.contains(";") || texto.contains("\"") || texto.contains("\n")
        ? "\"" + texto.replace("\"", "\"\"") + "\""
        : texto;
  }

  public static String vazio(String valor) {
    return valor == null || valor.isBlank() ? null : valor.trim();
  }

  public static int inteiro(String valor, String campo) {
    if (valor == null || valor.isBlank()) throw new IllegalArgumentException("Campo obrigatório ausente: " + campo);
    return Integer.parseInt(valor.trim());
  }

  public static Integer inteiroOpcional(String valor) {
    return valor == null || valor.isBlank() ? null : Integer.valueOf(valor.trim());
  }
}
