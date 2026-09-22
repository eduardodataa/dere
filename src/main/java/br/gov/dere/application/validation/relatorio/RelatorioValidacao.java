package br.gov.dere.application.validation.relatorio;

import java.util.List;

public record RelatorioValidacao(
    boolean valido,
    String leiaute,
    String origem,
    String arquivo,
    int totalLinhas,
    int linhasComProblema,
    List<Critica> criticas,
    String xml,
    String csv,
    String relatorioXlsx) {
  public RelatorioValidacao comArtefatos(String xml, String csv, String relatorioXlsx) {
    return new RelatorioValidacao(valido, leiaute, origem, arquivo, totalLinhas, linhasComProblema, criticas, xml, csv, relatorioXlsx);
  }

  public static RelatorioValidacao de(String leiaute, String origem, String arquivo, int totalLinhas, List<Critica> criticas) {
    var linhas = criticas.stream().map(Critica::linha).filter(linha -> linha != null && linha > 0).distinct().toList();
    return new RelatorioValidacao(criticas.isEmpty(), leiaute, origem, arquivo, totalLinhas, linhas.size(), List.copyOf(criticas), "", "", "");
  }
}
