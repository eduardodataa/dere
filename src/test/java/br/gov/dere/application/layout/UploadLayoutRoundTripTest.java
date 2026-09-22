package br.gov.dere.application.layout;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import br.gov.dere.application.csv.DereCsvConverter;
import br.gov.dere.application.d1001.D1001XmlGenerator;
import br.gov.dere.application.pgcc.D1011CsvConverter;
import br.gov.dere.application.pgcc.D1011XmlGenerator;
import br.gov.dere.integration.xml.XmlSupport;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;

class UploadLayoutRoundTripTest {
  @Test
  void d1001CsvViraXmlValidoEVoltaAoModelo() throws Exception {
    var csv = Files.readString(Path.of("examples/csv/d1001-upload-validado.csv"));
    var converter = new DereCsvConverter();
    var model = converter.fromCsv(csv);
    var xml = new D1001XmlGenerator().generate(model);
    assertXsd(xml, "/dere/schemas/nota_2026_001/xsd/evtInfoContrib-v1_0_1.xsd");
    assertEquals(model, converter.fromXml(xml));
    assertEquals(model, converter.fromCsv(converter.toCsv(model)));
    assertEquals("01914904", model.cnpjRoot());
    assertEquals(List.of("13B"), model.financialActivities());
  }

  @Test
  void d1011CsvViraXmlValidoEVoltaAoModelo() throws Exception {
    var csv = Files.readString(Path.of("examples/csv/d1011-upload-validado.csv"));
    var converter = new D1011CsvConverter();
    var model = converter.fromCsv(csv);
    assertEquals(26, model.accounts().size());
    var xml = new D1011XmlGenerator().generate(model);
    assertXsd(xml, "/dere/schemas/v1_1_0/xsd/06-XSD-D-1011 (v. 1.0.1).xsd");
    assertEquals(model, converter.fromXml(xml));
    assertEquals(model, converter.fromCsv(converter.toCsv(model)));
    assertEquals("112346", model.accounts().get(0).accountCode());
    assertEquals("A", model.accounts().get(0).accountIndicator());
    assertTrue(xml.contains("<cDbrMista>000</cDbrMista>"));
  }

  private static void assertXsd(String xml, String schema) {
    var signed = xml.replace("</DeRE>", "<Signature xmlns=\"http://www.w3.org/2000/09/xmldsig#\"></Signature></DeRE>");
    var xsd = UploadLayoutRoundTripTest.class.getResource(schema);
    var issues = XmlSupport.validateCollecting(signed, xsd).stream().filter(issue -> !"aviso".equals(issue.severity())).toList();
    assertEquals(List.of(), issues, () -> issues.stream().map(XmlSupport.Issue::message).reduce("", (left, right) -> left + "\n" + right));
  }
}
