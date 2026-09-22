package br.gov.dere.application.validation;

import br.gov.dere.application.validation.catalogo.CatalogosCampo;
import br.gov.dere.application.validation.catalogo.RegraCampo;
import br.gov.dere.application.validation.relatorio.Critica;
import br.gov.dere.application.validation.relatorio.TipoCritica;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public final class ValidadorCamposCsv {
  private ValidadorCamposCsv() {}

  public static List<Critica> validar(String leiaute, String arquivo, String csv) {
    var catalogo = "D-1011".equals(leiaute) ? CatalogosCampo.d1011() : CatalogosCampo.d1001();
    var tabela = TabelaCsv.analisar(csv);
    var criticas = new ArrayList<Critica>();
    if (tabela.cabecalhos().isEmpty()) {
      criticas.add(new Critica(arquivo, 1, "Documento", "", TipoCritica.ESTRUTURA, "cabeçalho + ao menos uma linha de dados",
          "Arquivo sem cabeçalho. Exporte novamente no formato do leiaute " + leiaute + ".", ""));
      return criticas;
    }
    for (var regra : catalogo) {
      if (regra.obrigatorio() && !tabela.temColuna(regra.nome())) {
        criticas.add(new Critica(arquivo, 1, regra.nome(), "", TipoCritica.ESTRUTURA, colunaEsperada(regra),
            "Coluna obrigatória ausente no cabeçalho.", regra.caminhoXml()));
      }
    }
    if (tabela.linhas().isEmpty()) {
      criticas.add(new Critica(arquivo, 1, "Documento", "", TipoCritica.ESTRUTURA, "ao menos uma linha de dados",
          "CSV contém só o cabeçalho. Inclua os registros do evento.", ""));
      return criticas;
    }
    if ("D-1001".equals(leiaute) && tabela.linhas().size() > 1) {
      for (int i = 1; i < tabela.linhas().size(); i++) {
        var extra = tabela.linhas().get(i);
        criticas.add(new Critica(arquivo, extra.numero(), "Documento", "", TipoCritica.ESTRUTURA, "uma única linha de dados",
            "D-1001 processa só a primeira linha de dados. Esta linha será ignorada na conversão.", ""));
      }
    }
    var primeira = tabela.linhas().get(0);
    var contasVistas = new HashSet<String>();
    var limite = "D-1001".equals(leiaute) ? 1 : tabela.linhas().size();
    for (int i = 0; i < limite; i++) {
      var linha = tabela.linhas().get(i);
      for (var regra : catalogo) {
        if (!tabela.temColuna(regra.nome())) continue;
        if (i > 0 && regra.nivelEvento()) {
          var atual = tabela.valor(linha, regra.nome());
          var original = tabela.valor(primeira, regra.nome());
          if (!atual.equals(original)) {
            criticas.add(new Critica(arquivo, linha.numero(), regra.nome(), atual, TipoCritica.NEGOCIO, original,
                "Valor diverge do evento da linha " + primeira.numero() + " (" + original + ").", regra.caminhoXml()));
          }
          continue;
        }
        var caminho = regra.nivelEvento() ? regra.caminhoXml() : caminhoConta(regra.caminhoXml(), i + 1);
        criticas.addAll(ValidadorCelula.validar(arquivo, linha.numero(), regra, tabela.valor(linha, regra.nome()), caminho));
      }
      if ("D-1011".equals(leiaute) && tabela.temColuna("cCta")) {
        var codigo = tabela.valor(linha, "cCta");
        if (!codigo.isBlank() && !contasVistas.add(codigo)) {
          criticas.add(new Critica(arquivo, linha.numero(), "cCta", codigo, TipoCritica.NEGOCIO, "código único por conta",
              "Código de conta duplicado neste arquivo.", caminhoConta("/DeRE/evtPGCC/infoPGCC/infoContas/infoConta/cCta", i + 1)));
        }
      }
      adicionarOrdemDatas(criticas, arquivo, linha.numero(), tabela, "iniValid", "fimValid",
          "/DeRE/" + ("D-1011".equals(leiaute) ? "evtPGCC" : "evtInfoContrib") + "/idePeriodo/fimValid");
      if ("D-1001".equals(leiaute)) {
        adicionarOrdemDatas(criticas, arquivo, linha.numero(), tabela, "novaValidadeIniValid", "novaValidadeFimValid",
            "/DeRE/evtInfoContrib/idePeriodo/novaValidade/fimValid");
      }
      if ("D-1011".equals(leiaute)) {
        adicionarOrdemDatas(criticas, arquivo, linha.numero(), tabela, "iniVig", "fimVig",
            caminhoConta("/DeRE/evtPGCC/infoPGCC/infoContas/infoConta/fimVig", i + 1));
      }
      if ("3".equals(tabela.valor(linha, "tpOper")) && tabela.valor(linha, "motExcl").isBlank()) {
        var caminho = "D-1011".equals(leiaute) ? "/DeRE/evtPGCC/ideEvento/motExcl" : "/DeRE/evtInfoContrib/ideEvento/motExcl";
        criticas.add(new Critica(arquivo, linha.numero(), "motExcl", "", TipoCritica.OBRIGATORIO,
            CatalogosCampo.formatarDominio(CatalogosCampo.MOT_EXCL),
            "Obrigatório quando tpOper = 3 (Exclusão).", caminho));
      }
    }
    return criticas;
  }

  public static void adicionarDivergenciaCnpj(List<Critica> criticas, String arquivo, Integer linha, String encontrado, String esperado, String caminhoXml) {
    if (esperado == null || esperado.isBlank() || encontrado == null || encontrado.isBlank() || esperado.equals(encontrado)) return;
    criticas.add(new Critica(arquivo, linha, "nrInsc", encontrado, TipoCritica.NEGOCIO, esperado,
        "CNPJ raiz do arquivo (" + encontrado + ") diverge da entidade selecionada (" + esperado + "). Confira a entidade ou o nrInsc.",
        caminhoXml));
  }

  private static void adicionarOrdemDatas(List<Critica> criticas, String arquivo, int numeroLinha, TabelaCsv tabela, String inicio, String fim, String caminhoXml) {
    if (!tabela.temColuna(inicio) || !tabela.temColuna(fim)) return;
    var linha = tabela.linhas().stream().filter(item -> item.numero() == numeroLinha).findFirst().orElse(null);
    if (linha == null) return;
    var dataInicio = ValidadorCelula.analisarData(tabela.valor(linha, inicio));
    var dataFim = ValidadorCelula.analisarData(tabela.valor(linha, fim));
    if (dataInicio != null && dataFim != null && dataFim.isBefore(dataInicio)) {
      criticas.add(new Critica(arquivo, numeroLinha, fim, tabela.valor(linha, fim), TipoCritica.NEGOCIO,
          "igual ou posterior a " + inicio + " (" + tabela.valor(linha, inicio) + ")",
          fim + " é anterior a " + inicio + ". Inverta as datas ou deixe o fim em branco.", caminhoXml));
    }
  }

  private static String caminhoConta(String caminho, int indice) {
    if (caminho == null) return "";
    return caminho.replace("/infoConta/", "/infoConta[" + indice + "]/");
  }

  private static String colunaEsperada(RegraCampo regra) {
    return "coluna " + regra.nome() + " no cabeçalho";
  }
}
