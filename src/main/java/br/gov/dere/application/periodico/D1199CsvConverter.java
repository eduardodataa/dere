package br.gov.dere.application.periodico;

import br.gov.dere.application.validation.TabelaCsv;
import br.gov.dere.domain.periodico.D1199;
import br.gov.dere.domain.periodico.D1199Base;
import br.gov.dere.domain.periodico.D1199Detalhe;
import br.gov.dere.integration.xml.XmlSupport;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import org.w3c.dom.Element;

public class D1199CsvConverter {
  public static final String HEADER = "id;tpOper;tpAmb;aplicEmi;verAplic;nrInsc;perApur;codBCNRaiz;usarBCNAcum;metodoAproveit;vUtilBCN;codBCN;vUsarBCN";

  public D1199 fromCsv(String csv) {
    var tabela = TabelaCsv.analisar(csv);
    if (tabela.linhas().isEmpty()) throw new IllegalArgumentException("CSV deve conter cabeçalho e ao menos uma linha");
    var primeira = tabela.linhas().get(0);
    var bases = new LinkedHashMap<String, D1199Base>();
    var detalhes = new ArrayList<D1199Detalhe>();
    for (var linha : tabela.linhas()) {
      var raiz = tabela.valor(linha, "codBCNRaiz");
      if (!raiz.isBlank() && !bases.containsKey(raiz)) {
        bases.put(raiz, new D1199Base(
            raiz,
            CsvPeriodico.vazio(tabela.valor(linha, "usarBCNAcum")),
            CsvPeriodico.vazio(tabela.valor(linha, "metodoAproveit")),
            CsvPeriodico.dinheiroXml(tabela.valor(linha, "vUtilBCN"))));
      }
      var codigo = tabela.valor(linha, "codBCN");
      if (!codigo.isBlank()) {
        detalhes.add(new D1199Detalhe(codigo, CsvPeriodico.dinheiroXml(tabela.valor(linha, "vUsarBCN"))));
      }
    }
    return new D1199(
        tabela.valor(primeira, "id"),
        CsvPeriodico.inteiro(tabela.valor(primeira, "tpOper"), "tpOper"),
        CsvPeriodico.inteiro(tabela.valor(primeira, "tpAmb"), "tpAmb"),
        CsvPeriodico.inteiro(tabela.valor(primeira, "aplicEmi"), "aplicEmi"),
        tabela.valor(primeira, "verAplic"),
        tabela.valor(primeira, "nrInsc"),
        tabela.valor(primeira, "perApur"),
        new ArrayList<>(bases.values()),
        detalhes);
  }

  public String toCsv(D1199 modelo) {
    var saida = new StringBuilder(HEADER).append('\n');
    if (modelo.bases().isEmpty() && modelo.detalhes().isEmpty()) {
      saida.append(linha(modelo, null, null)).append('\n');
      return saida.toString();
    }
    var max = Math.max(modelo.bases().size(), modelo.detalhes().size());
    for (int i = 0; i < max; i++) {
      saida.append(linha(
          modelo,
          i < modelo.bases().size() ? modelo.bases().get(i) : null,
          i < modelo.detalhes().size() ? modelo.detalhes().get(i) : null)).append('\n');
    }
    return saida.toString();
  }

  public D1199 fromXml(String xml) throws Exception {
    var documento = XmlSupport.parse(xml);
    var evento = (Element) documento.getElementsByTagNameNS(D1199XmlGenerator.NS, "evtFechMensal").item(0);
    if (evento == null) throw new IllegalArgumentException("XML não contém evtFechMensal");
    var ide = filho(evento, "ideEvento");
    var contrib = filho(evento, "ideContrib");
    var periodo = filho(evento, "idePeriodo");
    var bases = new ArrayList<D1199Base>();
    var nosBase = evento.getElementsByTagNameNS(D1199XmlGenerator.NS, "infoBCN");
    for (int i = 0; i < nosBase.getLength(); i++) {
      var no = (Element) nosBase.item(i);
      bases.add(new D1199Base(
          texto(no, "codBCNRaiz"),
          texto(no, "usarBCNAcum"),
          CsvPeriodico.vazio(texto(no, "metodoAproveit")),
          CsvPeriodico.vazio(texto(no, "vUtilBCN"))));
    }
    var detalhes = new ArrayList<D1199Detalhe>();
    var nosDet = evento.getElementsByTagNameNS(D1199XmlGenerator.NS, "detBCNeg");
    for (int i = 0; i < nosDet.getLength(); i++) {
      var no = (Element) nosDet.item(i);
      detalhes.add(new D1199Detalhe(texto(no, "codBCN"), CsvPeriodico.vazio(texto(no, "vUsarBCN"))));
    }
    return new D1199(
        evento.getAttribute("id"),
        CsvPeriodico.inteiro(texto(ide, "tpOper"), "tpOper"),
        CsvPeriodico.inteiro(texto(ide, "tpAmb"), "tpAmb"),
        CsvPeriodico.inteiro(texto(ide, "aplicEmi"), "aplicEmi"),
        texto(ide, "verAplic"),
        texto(contrib, "nrInsc"),
        texto(periodo, "perApur"),
        bases,
        detalhes);
  }

  private static String linha(D1199 modelo, D1199Base base, D1199Detalhe detalhe) {
    return String.join(";",
        CsvPeriodico.csv(modelo.id()),
        CsvPeriodico.csv(modelo.operation()),
        CsvPeriodico.csv(modelo.environment()),
        CsvPeriodico.csv(modelo.application()),
        CsvPeriodico.csv(modelo.applicationVersion()),
        CsvPeriodico.csv(modelo.cnpjRoot()),
        CsvPeriodico.csv(modelo.period()),
        CsvPeriodico.csv(base == null ? "" : base.codBCNRaiz()),
        CsvPeriodico.csv(base == null ? "" : base.usarBCNAcum()),
        CsvPeriodico.csv(base == null ? "" : base.metodoAproveit()),
        CsvPeriodico.csv(base == null ? "" : base.vUtilBCN()),
        CsvPeriodico.csv(detalhe == null ? "" : detalhe.codBCN()),
        CsvPeriodico.csv(detalhe == null ? "" : detalhe.vUsarBCN()));
  }

  private static Element filho(Element pai, String nome) {
    if (pai == null) return null;
    var lista = pai.getElementsByTagNameNS(D1199XmlGenerator.NS, nome);
    return lista.getLength() == 0 ? null : (Element) lista.item(0);
  }

  private static String texto(Element pai, String nome) {
    var no = filho(pai, nome);
    return no == null ? "" : no.getTextContent();
  }
}
