package br.gov.dere.application.d1001;

import br.gov.dere.domain.contributor.D1001;
import br.gov.dere.integration.xml.XmlSupport;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;

public class D1001XmlGenerator {
  public static final String NS = "http://www.dere.gov.br/schemas/evtInfoContrib/v1_0_1";
  public String generate(D1001 d) throws Exception {
    var f = DocumentBuilderFactory.newInstance(); f.setNamespaceAware(true); Document doc = f.newDocumentBuilder().newDocument();
    var root = doc.createElementNS(NS, "DeRE"); doc.appendChild(root); var evt = doc.createElementNS(NS, "evtInfoContrib"); evt.setAttribute("id", d.id()); root.appendChild(evt);
    var ide = child(doc, evt, "ideEvento"); text(doc, ide, "tpOper", d.operation()); text(doc, ide, "tpAmb", d.environment()); text(doc, ide, "aplicEmi", 1); text(doc, ide, "verAplic", d.applicationVersion());
    var contrib = child(doc, evt, "ideContrib"); text(doc, contrib, "nrInsc", d.cnpjRoot()); var period = child(doc, evt, "idePeriodo"); text(doc, period, "iniValid", d.validFrom()); if (d.validTo()!=null) text(doc, period, "fimValid", d.validTo());
    var info = child(doc, evt, "infoContrib"); text(doc, info, "regTribPrinc", d.primaryTaxRegime()); for (Integer v : d.secondaryTaxRegimes()) text(doc, info, "regTribSecund", v); text(doc, info, "indNatTrib", d.taxNature());
    return XmlSupport.serialize(doc);
  }
  private static org.w3c.dom.Element child(Document d, org.w3c.dom.Node p, String n) { var e=d.createElementNS(NS,n); p.appendChild(e); return e; }
  private static void text(Document d, org.w3c.dom.Node p, String n, Object v) { var e=child(d,p,n); e.setTextContent(String.valueOf(v)); }
}
