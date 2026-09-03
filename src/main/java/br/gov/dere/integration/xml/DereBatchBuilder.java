package br.gov.dere.integration.xml;

import java.util.List;
import javax.xml.parsers.DocumentBuilderFactory;

public class DereBatchBuilder {
  public static final String NS = "http://www.dere.gov.br/schemas/envioLoteDere/v1_0_1";
  public String build(String cnpjRoot, List<String> signedEvents) throws Exception {
    var f=DocumentBuilderFactory.newInstance(); f.setNamespaceAware(true); var d=f.newDocumentBuilder().newDocument(); var root=d.createElementNS(NS,"DeRE"); d.appendChild(root); var lote=e(d,root,"loteEventos"); var ic=e(d,lote,"ideContrib"); t(d,ic,"nrInsc",cnpjRoot); var eventos=e(d,lote,"eventos");
    for (String xml : signedEvents) { var item=e(d,eventos,"evento"); var eventDoc=XmlSupport.parse(xml); var event=eventDoc.getDocumentElement().getFirstChild(); while(event.getNodeType()!=org.w3c.dom.Node.ELEMENT_NODE) event=event.getNextSibling(); item.setAttribute("id",((org.w3c.dom.Element)event).getAttribute("id")); item.appendChild(d.importNode(eventDoc.getDocumentElement(),true)); }
    return XmlSupport.serialize(d);
  }
  private static org.w3c.dom.Element e(org.w3c.dom.Document d, org.w3c.dom.Node p,String n){var x=d.createElementNS(NS,n);p.appendChild(x);return x;}
  private static void t(org.w3c.dom.Document d,org.w3c.dom.Node p,String n,Object v){e(d,p,n).setTextContent(String.valueOf(v));}
}
