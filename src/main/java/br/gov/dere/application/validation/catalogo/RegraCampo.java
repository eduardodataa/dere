package br.gov.dere.application.validation.catalogo;

import java.util.Map;

public record RegraCampo(
    String nome,
    boolean obrigatorio,
    boolean data,
    boolean lista,
    String padrao,
    String dicaPadrao,
    Integer tamanhoMaximo,
    Map<String, String> dominio,
    String caminhoXml,
    boolean nivelEvento) {
  public static RegraCampo dominio(String nome, boolean obrigatorio, Map<String, String> dominio, String caminhoXml, boolean nivelEvento) {
    return new RegraCampo(nome, obrigatorio, false, false, null, null, null, dominio, caminhoXml, nivelEvento);
  }

  public static RegraCampo texto(String nome, boolean obrigatorio, String padrao, String dicaPadrao, Integer tamanhoMaximo, String caminhoXml, boolean nivelEvento) {
    return new RegraCampo(nome, obrigatorio, false, false, padrao, dicaPadrao, tamanhoMaximo, Map.of(), caminhoXml, nivelEvento);
  }

  public static RegraCampo data(String nome, boolean obrigatorio, String caminhoXml, boolean nivelEvento) {
    return new RegraCampo(nome, obrigatorio, true, false, null, "dd/MM/yyyy ou AAAA-MM-DD", null, Map.of(), caminhoXml, nivelEvento);
  }

  public static RegraCampo listaDominio(String nome, Map<String, String> dominio, String caminhoXml) {
    return new RegraCampo(nome, false, false, true, null, null, null, dominio, caminhoXml, true);
  }

  public static RegraCampo listaPadrao(String nome, String padrao, String dicaPadrao, String caminhoXml) {
    return new RegraCampo(nome, false, false, true, padrao, dicaPadrao, null, Map.of(), caminhoXml, true);
  }
}
