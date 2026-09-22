package br.gov.dere.integration.receita;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

public final class GovernmentResponseParser {
  private GovernmentResponseParser() {}
  public static String protocol(String xml) { return value(xml,"protocolo","protocoloRecepcao","nrProt"); }
  public static String code(String xml) { return value(xml,"codigo","codigoErro","codResposta","statusCode"); }
  public static String receipt(String xml) { return value(xml,"recibo","numeroRecibo","nrRecibo"); }
  public static String status(String xml) { return value(xml,"status","situacao","resultado"); }
  private static String value(String xml,String... names) {
    if (xml==null || xml.isBlank()) return null;
    try {
      var f=DocumentBuilderFactory.newInstance(); f.setNamespaceAware(true); f.setFeature("http://apache.org/xml/features/disallow-doctype-decl",true); f.setFeature("http://xml.org/sax/features/external-general-entities",false); f.setFeature("http://xml.org/sax/features/external-parameter-entities",false); f.setXIncludeAware(false); f.setExpandEntityReferences(false);
      Document d=f.newDocumentBuilder().parse(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));
      for(String name:names){ var nodes=d.getElementsByTagNameNS("*",name); if(nodes.getLength()>0) return text(nodes.item(0)); var plain=d.getElementsByTagName(name); if(plain.getLength()>0) return text(plain.item(0)); }
    } catch(Exception ignored) { }
    return null;
  }
  private static String text(Node n){ var value=n==null?null:n.getTextContent(); return value==null||value.isBlank()?null:value.trim(); }
}
