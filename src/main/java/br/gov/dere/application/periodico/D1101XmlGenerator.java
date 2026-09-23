package br.gov.dere.application.periodico;

import br.gov.dere.domain.periodico.D1101;
import br.gov.dere.integration.xml.XmlSupport;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class D1101XmlGenerator {
  public static final String NS = "http://www.dere.gov.br/schemas/evtBalancete/v0_0_1";

  public String generate(D1101 modelo) throws Exception {
    var fabrica = DocumentBuilderFactory.newInstance();
    fabrica.setNamespaceAware(true);
    Document dom = fabrica.newDocumentBuilder().newDocument();
    var raiz = dom.createElementNS(NS, "DeRE");
    dom.appendChild(raiz);
    var evento = filho(dom, raiz, "evtBalancete");
    evento.setAttribute("id", modelo.id());
    var ide = filho(dom, evento, "ideEvento");
    texto(dom, ide, "tpOper", modelo.operation());
    if (modelo.exclusionReason() != null) texto(dom, ide, "motExcl", modelo.exclusionReason());
    if (modelo.processNumber() != null) texto(dom, ide, "nrProc", modelo.processNumber());
    if (modelo.receiptNumber() != null) texto(dom, ide, "nrRecibo", modelo.receiptNumber());
    texto(dom, ide, "tpAmb", modelo.environment());
    texto(dom, ide, "aplicEmi", modelo.application());
    texto(dom, ide, "verAplic", modelo.applicationVersion());
    var contrib = filho(dom, evento, "ideContrib");
    texto(dom, contrib, "nrInsc", modelo.cnpjRoot());
    var periodo = filho(dom, evento, "idePeriodo");
    texto(dom, periodo, "perApur", modelo.period());
    if (modelo.operation() != 3) {
      var info = filho(dom, evento, "infoBalancete");
      var lista = filho(dom, info, "infoContas");
      for (var conta : modelo.accounts()) {
        var no = filho(dom, lista, "infoConta");
        texto(dom, no, "cCta", conta.cCta());
        texto(dom, no, "natSaldoInic", conta.natSaldoInic());
        texto(dom, no, "vSaldoInic", conta.vSaldoInic());
        texto(dom, no, "vMovDebt", conta.vMovDebt());
        if (conta.vAjusteDebt() != null) texto(dom, no, "vAjusteDebt", conta.vAjusteDebt());
        texto(dom, no, "vMovCred", conta.vMovCred());
        if (conta.vAjusteCred() != null) texto(dom, no, "vAjusteCred", conta.vAjusteCred());
        texto(dom, no, "natSaldoFinal", conta.natSaldoFinal());
        texto(dom, no, "vSaldoFinal", conta.vSaldoFinal());
        if (conta.natVApur() != null) texto(dom, no, "natVApur", conta.natVApur());
        texto(dom, no, "vApur", conta.vApur());
      }
    }
    return XmlSupport.serialize(dom);
  }

  private static org.w3c.dom.Element filho(Document dom, Node pai, String nome) {
    var no = dom.createElementNS(NS, nome);
    pai.appendChild(no);
    return no;
  }

  private static void texto(Document dom, Node pai, String nome, Object valor) {
    if (valor == null) throw new IllegalArgumentException("Campo obrigatório ausente: " + nome);
    var no = filho(dom, pai, nome);
    no.setTextContent(String.valueOf(valor));
  }
}
