package br.gov.dere.application.periodico;

import br.gov.dere.application.validation.TabelaCsv;
import br.gov.dere.domain.periodico.D1101;
import br.gov.dere.domain.periodico.D1101Conta;
import br.gov.dere.integration.xml.XmlSupport;
import java.util.ArrayList;
import org.w3c.dom.Element;

public class D1101CsvConverter {
  public static final String HEADER = "id;motExcl;nrProc;nrRecibo;tpOper;tpAmb;aplicEmi;verAplic;nrInsc;perApur;cCta;natSaldoInic;vSaldoInic;vMovDebt;vAjusteDebt;vMovCred;vAjusteCred;natSaldoFinal;vSaldoFinal;natVApur;vApur";

  public D1101 fromCsv(String csv) {
    var tabela = TabelaCsv.analisar(csv);
    if (tabela.linhas().isEmpty()) throw new IllegalArgumentException("CSV deve conter cabeçalho e ao menos uma linha");
    var primeira = tabela.linhas().get(0);
    var operacao = CsvPeriodico.inteiro(tabela.valor(primeira, "tpOper"), "tpOper");
    var contas = new ArrayList<D1101Conta>();
    if (operacao != 3) {
      for (var linha : tabela.linhas()) {
        var conta = tabela.valor(linha, "cCta");
        if (conta.isBlank()) continue;
        contas.add(new D1101Conta(
            conta,
            CsvPeriodico.vazio(tabela.valor(linha, "natSaldoInic")),
            CsvPeriodico.dinheiroXml(tabela.valor(linha, "vSaldoInic")),
            CsvPeriodico.dinheiroXml(tabela.valor(linha, "vMovDebt")),
            CsvPeriodico.dinheiroXml(tabela.valor(linha, "vAjusteDebt")),
            CsvPeriodico.dinheiroXml(tabela.valor(linha, "vMovCred")),
            CsvPeriodico.dinheiroXml(tabela.valor(linha, "vAjusteCred")),
            CsvPeriodico.vazio(tabela.valor(linha, "natSaldoFinal")),
            CsvPeriodico.dinheiroXml(tabela.valor(linha, "vSaldoFinal")),
            CsvPeriodico.vazio(tabela.valor(linha, "natVApur")),
            CsvPeriodico.dinheiroXml(tabela.valor(linha, "vApur"))));
      }
    }
    return new D1101(
        tabela.valor(primeira, "id"),
        operacao,
        CsvPeriodico.inteiroOpcional(tabela.valor(primeira, "motExcl")),
        CsvPeriodico.vazio(tabela.valor(primeira, "nrProc")),
        CsvPeriodico.vazio(tabela.valor(primeira, "nrRecibo")),
        CsvPeriodico.inteiro(tabela.valor(primeira, "tpAmb"), "tpAmb"),
        CsvPeriodico.inteiro(tabela.valor(primeira, "aplicEmi"), "aplicEmi"),
        tabela.valor(primeira, "verAplic"),
        tabela.valor(primeira, "nrInsc"),
        tabela.valor(primeira, "perApur"),
        contas);
  }

  public String toCsv(D1101 modelo) {
    var saida = new StringBuilder(HEADER).append('\n');
    if (modelo.accounts().isEmpty()) {
      saida.append(linha(modelo, null)).append('\n');
      return saida.toString();
    }
    for (var conta : modelo.accounts()) saida.append(linha(modelo, conta)).append('\n');
    return saida.toString();
  }

  public D1101 fromXml(String xml) throws Exception {
    var documento = XmlSupport.parse(xml);
    var evento = (Element) documento.getElementsByTagNameNS(D1101XmlGenerator.NS, "evtBalancete").item(0);
    if (evento == null) throw new IllegalArgumentException("XML não contém evtBalancete");
    var ide = filho(evento, "ideEvento");
    var contrib = filho(evento, "ideContrib");
    var periodo = filho(evento, "idePeriodo");
    var contas = new ArrayList<D1101Conta>();
    var nos = evento.getElementsByTagNameNS(D1101XmlGenerator.NS, "infoConta");
    for (int i = 0; i < nos.getLength(); i++) {
      var conta = (Element) nos.item(i);
      contas.add(new D1101Conta(
          texto(conta, "cCta"),
          texto(conta, "natSaldoInic"),
          texto(conta, "vSaldoInic"),
          texto(conta, "vMovDebt"),
          CsvPeriodico.vazio(texto(conta, "vAjusteDebt")),
          texto(conta, "vMovCred"),
          CsvPeriodico.vazio(texto(conta, "vAjusteCred")),
          texto(conta, "natSaldoFinal"),
          texto(conta, "vSaldoFinal"),
          CsvPeriodico.vazio(texto(conta, "natVApur")),
          texto(conta, "vApur")));
    }
    return new D1101(
        evento.getAttribute("id"),
        CsvPeriodico.inteiro(texto(ide, "tpOper"), "tpOper"),
        CsvPeriodico.inteiroOpcional(texto(ide, "motExcl")),
        CsvPeriodico.vazio(texto(ide, "nrProc")),
        CsvPeriodico.vazio(texto(ide, "nrRecibo")),
        CsvPeriodico.inteiro(texto(ide, "tpAmb"), "tpAmb"),
        CsvPeriodico.inteiro(texto(ide, "aplicEmi"), "aplicEmi"),
        texto(ide, "verAplic"),
        texto(contrib, "nrInsc"),
        texto(periodo, "perApur"),
        contas);
  }

  private static String linha(D1101 modelo, D1101Conta conta) {
    return String.join(";",
        CsvPeriodico.csv(modelo.id()),
        CsvPeriodico.csv(modelo.exclusionReason()),
        CsvPeriodico.csv(modelo.processNumber()),
        CsvPeriodico.csv(modelo.receiptNumber()),
        CsvPeriodico.csv(modelo.operation()),
        CsvPeriodico.csv(modelo.environment()),
        CsvPeriodico.csv(modelo.application()),
        CsvPeriodico.csv(modelo.applicationVersion()),
        CsvPeriodico.csv(modelo.cnpjRoot()),
        CsvPeriodico.csv(modelo.period()),
        CsvPeriodico.csv(conta == null ? "" : conta.cCta()),
        CsvPeriodico.csv(conta == null ? "" : conta.natSaldoInic()),
        CsvPeriodico.csv(conta == null ? "" : conta.vSaldoInic()),
        CsvPeriodico.csv(conta == null ? "" : conta.vMovDebt()),
        CsvPeriodico.csv(conta == null ? "" : conta.vAjusteDebt()),
        CsvPeriodico.csv(conta == null ? "" : conta.vMovCred()),
        CsvPeriodico.csv(conta == null ? "" : conta.vAjusteCred()),
        CsvPeriodico.csv(conta == null ? "" : conta.natSaldoFinal()),
        CsvPeriodico.csv(conta == null ? "" : conta.vSaldoFinal()),
        CsvPeriodico.csv(conta == null ? "" : conta.natVApur()),
        CsvPeriodico.csv(conta == null ? "" : conta.vApur()));
  }

  private static Element filho(Element pai, String nome) {
    if (pai == null) return null;
    var lista = pai.getElementsByTagNameNS(D1101XmlGenerator.NS, nome);
    return lista.getLength() == 0 ? null : (Element) lista.item(0);
  }

  private static String texto(Element pai, String nome) {
    var no = filho(pai, nome);
    return no == null ? "" : no.getTextContent();
  }
}
