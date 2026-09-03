package br.gov.dere.integration.xml;

import java.io.StringReader;
import java.io.StringWriter;
import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public final class XmlSupport {
  private XmlSupport() {}
  public static String serialize(Document document) throws Exception {
    var tf = javax.xml.transform.TransformerFactory.newInstance();
    tf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
    var transformer = tf.newTransformer(); transformer.setOutputProperty(javax.xml.transform.OutputKeys.ENCODING, "UTF-8");
    transformer.setOutputProperty(javax.xml.transform.OutputKeys.INDENT, "yes");
    var out = new StringWriter(); transformer.transform(new javax.xml.transform.dom.DOMSource(document), new javax.xml.transform.stream.StreamResult(out)); return out.toString();
  }
  public static Document parse(String xml) throws Exception {
    var f = javax.xml.parsers.DocumentBuilderFactory.newInstance(); f.setNamespaceAware(true); f.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true); f.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true); return f.newDocumentBuilder().parse(new org.xml.sax.InputSource(new StringReader(xml)));
  }
  public static void validate(String xml, java.io.InputStream xsd) throws SAXException {
    try { var source=new StreamSource(xsd); source.setSystemId("classpath:/dere/schemas/"); validate(xml,source); }
    catch (SAXException e) { throw e; } catch (Exception e) { throw new IllegalArgumentException("Falha ao carregar XSD", e); }
  }
  public static void validate(String xml, java.net.URL xsd) throws SAXException { try { validate(xml,new StreamSource(xsd.toExternalForm())); } catch(Exception e){if(e instanceof SAXException s)throw s;throw new IllegalArgumentException("Falha ao carregar XSD",e);} }
  private static void validate(String xml, StreamSource xsd) throws Exception { var sf=SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI); sf.setProperty(XMLConstants.ACCESS_EXTERNAL_DTD,""); sf.setProperty(XMLConstants.ACCESS_EXTERNAL_SCHEMA,""); sf.newSchema(xsd).newValidator().validate(new StreamSource(new StringReader(xml))); }
}
