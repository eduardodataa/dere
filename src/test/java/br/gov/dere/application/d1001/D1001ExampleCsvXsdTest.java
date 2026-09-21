package br.gov.dere.application.d1001;

import br.gov.dere.application.csv.DereCsvConverter;
import br.gov.dere.integration.xml.XmlSupport;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class D1001ExampleCsvXsdTest {
  @Test
  void csvDeInclusaoGeraXmlValidoNoXsd() throws Exception {
    assertXsd(Path.of("examples/csv/d1001-informacoes-contribuinte.csv"));
  }

  @Test
  void csvDeServicosFinanceirosGeraXmlValidoNoXsd() throws Exception {
    assertXsd(Path.of("examples/csv/d1001-servicos-financeiros.csv"));
  }

  private static void assertXsd(Path csvPath) throws Exception {
    var csv = Files.readString(csvPath);
    var d = new DereCsvConverter().fromCsv(csv);
    assertEquals(42, d.id().length());
    assertTrue(d.id().matches("[0-9A-Za-z]{42}"));
    assertTrue(d.cnpjRoot().matches("[0-9A-Z]{8}"));
    var xml = new D1001XmlGenerator().generate(d);
    xml = xml.replace("</DeRE>", "<Signature xmlns=\"http://www.w3.org/2000/09/xmldsig#\"></Signature></DeRE>");
    var xsd = D1001ExampleCsvXsdTest.class.getResource("/dere/schemas/nota_2026_001/xsd/evtInfoContrib-v1_0_1.xsd");
    assertNotNull(xsd);
    var issues = XmlSupport.validateCollecting(xml, xsd).stream().filter(i -> !"aviso".equals(i.severity())).toList();
    assertEquals(List.of(), issues, () -> issues.stream().map(XmlSupport.Issue::message).reduce("", (a, b) -> a + "\n" + b));
  }
}
