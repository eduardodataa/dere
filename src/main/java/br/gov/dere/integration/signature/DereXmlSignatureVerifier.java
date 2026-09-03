package br.gov.dere.integration.signature;
import br.gov.dere.integration.xml.XmlSupport;
import java.security.KeyStore;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.dom.DOMValidateContext;
public class DereXmlSignatureVerifier {
 public boolean verify(String xml,KeyStore ks,String alias)throws Exception{var d=XmlSupport.parse(xml);var sig=(org.w3c.dom.Element)d.getElementsByTagNameNS(javax.xml.crypto.dsig.XMLSignature.XMLNS,"Signature").item(0);if(sig==null)return false;var ctx=new DOMValidateContext(ks.getCertificate(alias).getPublicKey(),sig);var x=XMLSignatureFactory.getInstance("DOM").unmarshalXMLSignature(ctx);return x.validate(ctx);}
}
