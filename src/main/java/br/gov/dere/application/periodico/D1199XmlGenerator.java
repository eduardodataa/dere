package br.gov.dere.application.periodico;

import br.gov.dere.domain.periodico.D1199;
import br.gov.dere.integration.xml.XmlSupport;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class D1199XmlGenerator {
  public static final String NS = "http://www.dere.gov.br/schemas/evtFechMensal/v0_0_1";

  public String generate(D1199 modelo) throws Exception {
    var fabrica = DocumentBuilderFactory.newInstance();
    fabrica.setNamespaceAware(true);
    Document dom = fabrica.newDocumentBuilder().newDocument();
    var raiz = dom.createElementNS(NS, "DeRE");
    dom.appendChild(raiz);
    var evento = filho(dom, raiz, "evtFechMensal");
    evento.setAttribute("id", modelo.id());
    var ide = filho(dom, evento, "ideEvento");
    texto(dom, ide, "tpOper", modelo.operation());
    texto(dom, ide, "tpAmb", modelo.environment());
    texto(dom, ide, "aplicEmi", modelo.application());
    texto(dom, ide, "verAplic", modelo.applicationVersion());
    var contrib = filho(dom, evento, "ideContrib");
    texto(dom, contrib, "nrInsc", modelo.cnpjRoot());
    var periodo = filho(dom, evento, "idePeriodo");
    texto(dom, periodo, "perApur", modelo.period());
    if (!modelo.bases().isEmpty() || !modelo.detalhes().isEmpty()) {
      var info = filho(dom, evento, "infoFechamento");
      var grupo = filho(dom, info, "gUtilizBCN");
      for (var base : modelo.bases()) {
        var no = filho(dom, grupo, "infoBCN");
        texto(dom, no, "codBCNRaiz", base.codBCNRaiz());
        texto(dom, no, "usarBCNAcum", base.usarBCNAcum());
        if (base.metodoAproveit() != null) texto(dom, no, "metodoAproveit", base.metodoAproveit());
        if (base.vUtilBCN() != null) texto(dom, no, "vUtilBCN", base.vUtilBCN());
      }
      for (var detalhe : modelo.detalhes()) {
        var no = filho(dom, grupo, "detBCNeg");
        texto(dom, no, "codBCN", detalhe.codBCN());
        if (detalhe.vUsarBCN() != null) texto(dom, no, "vUsarBCN", detalhe.vUsarBCN());
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
