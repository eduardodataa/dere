package br.gov.dere.integration.signature;

import br.gov.dere.integration.xml.XmlSupport;
import java.security.KeyStore;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.dom.DOMValidateContext;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class DereXmlSignatureVerifier {
  public boolean verify(String xml, KeyStore ks, String alias) throws Exception {
    var d = XmlSupport.parse(xml);
    marcarId(d.getDocumentElement());
    var assinaturas = d.getElementsByTagNameNS(XMLSignature.XMLNS, "Signature");
    Element sig = null;
    for (int i = 0; i < assinaturas.getLength(); i++) {
      var candidata = (Element) assinaturas.item(i);
      if (candidata.getParentNode() == d.getDocumentElement()) {
        sig = candidata;
        break;
      }
    }
    if (sig == null) return false;
    var ctx = new DOMValidateContext(ks.getCertificate(alias).getPublicKey(), sig);
    var x = XMLSignatureFactory.getInstance("DOM").unmarshalXMLSignature(ctx);
    return x.validate(ctx);
  }

  private static void marcarId(Element raiz) {
    for (Node no = raiz.getFirstChild(); no != null; no = no.getNextSibling()) {
      if (no.getNodeType() != Node.ELEMENT_NODE) continue;
      var elemento = (Element) no;
      if (!elemento.getAttribute("id").isBlank()) elemento.setIdAttribute("id", true);
      return;
    }
  }
}
