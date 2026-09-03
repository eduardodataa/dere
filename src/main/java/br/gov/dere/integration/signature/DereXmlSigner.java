package br.gov.dere.integration.signature;

import br.gov.dere.integration.xml.XmlSupport;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import javax.xml.crypto.dsig.*;
import javax.xml.crypto.dsig.dom.DOMSignContext;
import javax.xml.crypto.dsig.keyinfo.*;
import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec;
import javax.xml.crypto.dsig.spec.TransformParameterSpec;

public class DereXmlSigner {
  public String sign(String xml, KeyStore keyStore, char[] password, String alias) throws Exception {
    var doc=XmlSupport.parse(xml); var element=doc.getDocumentElement().getFirstChild(); while (element.getNodeType()!=org.w3c.dom.Node.ELEMENT_NODE) element=element.getNextSibling();
    String id=((org.w3c.dom.Element)element).getAttribute("id"); if (id.isBlank()) throw new IllegalArgumentException("Evento sem atributo id"); ((org.w3c.dom.Element)element).setIdAttribute("id", true);
    PrivateKey key=(PrivateKey)keyStore.getKey(alias,password); X509Certificate cert=(X509Certificate)keyStore.getCertificate(alias); var fac=XMLSignatureFactory.getInstance("DOM");
    var ref=fac.newReference("#"+id, fac.newDigestMethod(DigestMethod.SHA256,null), java.util.List.of(fac.newTransform(Transform.ENVELOPED,(TransformParameterSpec)null),fac.newTransform(CanonicalizationMethod.INCLUSIVE,(TransformParameterSpec)null)),null,null);
    var ki=fac.getKeyInfoFactory().newKeyInfo(java.util.List.of(fac.getKeyInfoFactory().newX509Data(java.util.List.of(cert))));
    var sig=fac.newXMLSignature(fac.newSignedInfo(fac.newCanonicalizationMethod(CanonicalizationMethod.INCLUSIVE,(C14NMethodParameterSpec)null),fac.newSignatureMethod(SignatureMethod.RSA_SHA256,null),java.util.List.of(ref)),ki);
    sig.sign(new DOMSignContext(key, doc.getDocumentElement())); return XmlSupport.serialize(doc);
  }
}
