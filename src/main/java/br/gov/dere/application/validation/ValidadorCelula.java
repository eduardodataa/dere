package br.gov.dere.application.validation;

import br.gov.dere.application.validation.catalogo.CatalogosCampo;
import br.gov.dere.application.validation.catalogo.RegraCampo;
import br.gov.dere.application.validation.relatorio.Critica;
import br.gov.dere.application.validation.relatorio.TipoCritica;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class ValidadorCelula {
  private static final DateTimeFormatter BR = DateTimeFormatter.ofPattern("dd/MM/yyyy");

  private ValidadorCelula() {}

  public static List<Critica> validar(String arquivo, int linha, RegraCampo regra, String bruto, String caminhoXml) {
    var valor = bruto == null ? "" : bruto.trim();
    var caminho = caminhoXml == null || caminhoXml.isBlank() ? regra.caminhoXml() : caminhoXml;
    if (valor.isEmpty()) {
      if (!regra.obrigatorio()) return List.of();
      return List.of(new Critica(arquivo, linha, regra.nome(), "", TipoCritica.OBRIGATORIO, esperado(regra),
          "Campo obrigatório vazio. Preencha " + regra.nome() + ".", caminho));
    }
    if (regra.lista()) {
      var itens = Arrays.stream(valor.split("[;|]")).map(String::trim).filter(item -> !item.isEmpty()).toList();
      var criticas = new ArrayList<Critica>();
      for (var item : itens) criticas.addAll(validarItem(arquivo, linha, regra, item, caminho));
      return criticas;
    }
    return validarItem(arquivo, linha, regra, valor, caminho);
  }

  public static String esperado(RegraCampo regra) {
    if (regra.dominio() != null && !regra.dominio().isEmpty()) return CatalogosCampo.formatarDominio(regra.dominio());
    if (regra.data()) return "dd/MM/yyyy ou AAAA-MM-DD";
    if (regra.dicaPadrao() != null && !regra.dicaPadrao().isBlank()) return regra.dicaPadrao();
    if (regra.tamanhoMaximo() != null) return "até " + regra.tamanhoMaximo() + " caracteres";
    return "";
  }

  public static LocalDate analisarData(String valor) {
    if (valor == null || valor.isBlank()) return null;
    var texto = valor.trim();
    try {
      if (texto.length() == 10 && texto.charAt(2) == '/') return LocalDate.parse(texto, BR);
      if (texto.length() == 10 && texto.charAt(4) == '-') return LocalDate.parse(texto);
    } catch (DateTimeParseException ignorado) {
      return null;
    }
    return null;
  }

  public static boolean ehData(String valor) {
    return valor != null && !valor.isBlank() && analisarData(valor) != null;
  }

  private static List<Critica> validarItem(String arquivo, Integer linha, RegraCampo regra, String valor, String caminho) {
    if (regra.data() && analisarData(valor) == null) {
      return List.of(new Critica(arquivo, linha, regra.nome(), valor, TipoCritica.FORA_DO_PADRAO, esperado(regra),
          "Data inválida. Use " + esperado(regra) + ".", caminho));
    }
    if (regra.dominio() != null && !regra.dominio().isEmpty() && !regra.dominio().containsKey(valor)) {
      return List.of(new Critica(arquivo, linha, regra.nome(), valor, TipoCritica.FORA_DO_DOMINIO, esperado(regra),
          "Valor fora de domínio. Aceitos: " + esperado(regra) + ".", caminho));
    }
    if (regra.tamanhoMaximo() != null && valor.length() > regra.tamanhoMaximo()) {
      return List.of(new Critica(arquivo, linha, regra.nome(), valor, TipoCritica.FORA_DO_PADRAO, esperado(regra),
          "Tamanho " + valor.length() + " acima do máximo (" + regra.tamanhoMaximo() + ").", caminho));
    }
    if (regra.padrao() != null && !valor.matches(regra.padrao())) {
      return List.of(new Critica(arquivo, linha, regra.nome(), valor, TipoCritica.FORA_DO_PADRAO, esperado(regra),
          "Fora do padrão. Esperado: " + esperado(regra) + ".", caminho));
    }
    return List.of();
  }
}
