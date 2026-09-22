package br.gov.dere.integration.xml;

import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;
import org.w3c.dom.Document;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public final class XmlSupport {
  private static final Pattern FIELD = Pattern.compile("(?:element|elemento) '([^']+)'", Pattern.CASE_INSENSITIVE);
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
  public static String forSchemaValidation(String xml) {
    if (xml == null || xml.isBlank()) return "";
    if (xml.contains("<Signature")) return xml;
    return xml.replace("</DeRE>", "<Signature xmlns=\"http://www.w3.org/2000/09/xmldsig#\"></Signature></DeRE>");
  }
  public static List<Issue> validateCollecting(String xml, java.net.URL xsd) {
    try {
      var source = new StreamSource(xsd.openStream());
      source.setSystemId(xsd.toExternalForm());
      var issues = new ArrayList<Issue>();
      var validator = schemaFactory().newSchema(source).newValidator();
      validator.setErrorHandler(new ErrorHandler() {
        public void warning(SAXParseException e) { issues.add(issue(e, "aviso")); }
        public void error(SAXParseException e) { issues.add(issue(e, "erro")); }
        public void fatalError(SAXParseException e) { issues.add(issue(e, "fatal")); }
      });
      try { validator.validate(new StreamSource(new StringReader(xml))); }
      catch (SAXException e) { if (issues.isEmpty()) issues.add(new Issue(0, 0, "", "fatal", e.getMessage())); }
      return issues;
    } catch (Exception e) {
      return List.of(new Issue(0, 0, "", "fatal", e.getMessage() == null ? e.getClass().getSimpleName() : e.getMessage()));
    }
  }
  private static void validate(String xml, StreamSource xsd) throws Exception {
    schemaFactory().newSchema(xsd).newValidator().validate(new StreamSource(new StringReader(xml)));
  }
  private static SchemaFactory schemaFactory() throws Exception {
    var sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
    sf.setProperty(XMLConstants.ACCESS_EXTERNAL_DTD, "");
    sf.setProperty(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "file");
    sf.setResourceResolver(new SchemaResolver());
    return sf;
  }
  private static final class SchemaResolver implements LSResourceResolver {
    public LSInput resolveResource(String type, String namespaceURI, String publicId, String systemId, String baseURI) {
      if (systemId != null && (systemId.endsWith(".dtd") || systemId.contains("xhtml1-transitional.dtd"))) {
        return new StreamLsInput(InputStream.nullInputStream(), systemId, publicId, baseURI);
      }
      if ((systemId != null && systemId.contains("xmldsig-core-schema.xsd")) || "http://www.w3.org/2000/09/xmldsig#".equals(namespaceURI)) {
        var stream = XmlSupport.class.getResourceAsStream("/dere/schemas/xmldsig-stub.xsd");
        if (stream != null) return new StreamLsInput(stream, "xmldsig-stub.xsd", publicId, baseURI);
      }
      return null;
    }
  }
  private static final class StreamLsInput implements LSInput {
    private InputStream byteStream; private String systemId, publicId, baseURI, encoding, stringData; private Reader characterStream; private boolean certifiedText;
    private StreamLsInput(InputStream byteStream, String systemId, String publicId, String baseURI) {
      this.byteStream = byteStream; this.systemId = systemId; this.publicId = publicId; this.baseURI = baseURI;
    }
    public Reader getCharacterStream() { return characterStream; }
    public void setCharacterStream(Reader characterStream) { this.characterStream = characterStream; }
    public InputStream getByteStream() { return byteStream; }
    public void setByteStream(InputStream byteStream) { this.byteStream = byteStream; }
    public String getStringData() { return stringData; }
    public void setStringData(String stringData) { this.stringData = stringData; }
    public String getSystemId() { return systemId; }
    public void setSystemId(String systemId) { this.systemId = systemId; }
    public String getPublicId() { return publicId; }
    public void setPublicId(String publicId) { this.publicId = publicId; }
    public String getBaseURI() { return baseURI; }
    public void setBaseURI(String baseURI) { this.baseURI = baseURI; }
    public String getEncoding() { return encoding; }
    public void setEncoding(String encoding) { this.encoding = encoding; }
    public boolean getCertifiedText() { return certifiedText; }
    public void setCertifiedText(boolean certifiedText) { this.certifiedText = certifiedText; }
  }
  private static Issue issue(SAXParseException e, String severity) {
    var msg = e.getMessage() == null ? "" : e.getMessage();
    var m = FIELD.matcher(msg);
    return new Issue(Math.max(e.getLineNumber(), 0), Math.max(e.getColumnNumber(), 0), m.find() ? m.group(1) : "", severity, msg);
  }
  public record Issue(int line, int column, String field, String severity, String message) {}
}
