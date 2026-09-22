package br.gov.dere.application.validation;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class TabelaCsv {
  public record Linha(int numero, List<String> valores) {}

  private final List<String> cabecalhos;
  private final Map<String, Integer> indice;
  private final List<Linha> linhas;

  private TabelaCsv(List<String> cabecalhos, Map<String, Integer> indice, List<Linha> linhas) {
    this.cabecalhos = cabecalhos;
    this.indice = indice;
    this.linhas = linhas;
  }

  public static TabelaCsv analisar(String csv) {
    var texto = csv == null ? "" : csv.startsWith("\uFEFF") ? csv.substring(1) : csv;
    List<String> cabecalhos = List.of();
    Map<String, Integer> indice = Map.of();
    var linhas = new ArrayList<Linha>();
    var numeroLinha = 0;
    Character delimitador = null;
    for (var bruta : texto.split("\\R", -1)) {
      numeroLinha++;
      if (bruta.isBlank()) continue;
      if (delimitador == null) {
        delimitador = detectar(bruta);
        cabecalhos = analisarLinha(bruta, delimitador);
        indice = new LinkedHashMap<>();
        for (int i = 0; i < cabecalhos.size(); i++) indice.put(cabecalhos.get(i), i);
        continue;
      }
      linhas.add(new Linha(numeroLinha, analisarLinha(bruta, delimitador)));
    }
    return new TabelaCsv(cabecalhos, indice, List.copyOf(linhas));
  }

  public List<String> cabecalhos() {
    return cabecalhos;
  }

  public List<Linha> linhas() {
    return linhas;
  }

  public boolean temColuna(String nome) {
    return indice.containsKey(nome);
  }

  public String valor(Linha linha, String nome) {
    var posicao = indice.get(nome);
    if (posicao == null || posicao >= linha.valores().size()) return "";
    var valor = linha.valores().get(posicao);
    return valor == null ? "" : valor.trim();
  }

  private static char detectar(String cabecalho) {
    long pontoEVirgula = cabecalho.chars().filter(c -> c == ';').count();
    long virgula = cabecalho.chars().filter(c -> c == ',').count();
    return pontoEVirgula > virgula ? ';' : ',';
  }

  private static List<String> analisarLinha(String linha, char delimitador) {
    var valores = new ArrayList<String>();
    var atual = new StringBuilder();
    boolean entreAspas = false;
    for (int i = 0; i < linha.length(); i++) {
      char c = linha.charAt(i);
      if (c == '"') {
        if (entreAspas && i + 1 < linha.length() && linha.charAt(i + 1) == '"') {
          atual.append('"');
          i++;
        } else entreAspas = !entreAspas;
      } else if (c == delimitador && !entreAspas) {
        valores.add(atual.toString().trim());
        atual.setLength(0);
      } else atual.append(c);
    }
    valores.add(atual.toString().trim());
    return valores;
  }
}
