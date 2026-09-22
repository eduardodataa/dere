package br.gov.dere.application.pgcc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import br.gov.dere.application.layout.D1011LayoutDefinition;
import br.gov.dere.domain.layout.CsvDocument;
import br.gov.dere.integration.xml.XmlSupport;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;

class D1011ExampleCsvXsdTest {
  @Test
  void csvPrevicGeraXmlEApontaCriticasDeFormatoDoXsd() throws Exception {
    var csv = Files.readString(Path.of("examples/csv/d1011-pgcc-previc.csv"));
    var definition = new D1011LayoutDefinition();
    var model = definition.parseCsv(new CsvDocument(csv));
    assertEquals(42, model.id().length());
    assertEquals("5", model.referenceChart());
    var xml = definition.generateXml(model).content()
        .replace("</DeRE>", "<Signature xmlns=\"http://www.w3.org/2000/09/xmldsig#\"></Signature></DeRE>");
    var issues = validate(xml);
    assertFalse(issues.isEmpty());
    assertTrue(issues.stream().anyMatch(issue -> "cCta".equals(issue.field()) || issue.message().contains("cCta")));
    assertEquals("1.1.01", definition.parseXml(definition.generateXml(model)).accounts().get(0).accountCode());
  }

  @Test
  void xmlComFormatosOficiaisPassaNoXsd() {
    var xml = """
        <DeRE xmlns="http://www.dere.gov.br/schemas/evtPGCC/v1_0_1">
          <evtPGCC id="DeRE10111000000123456782026090212000000007">
            <ideEvento><tpOper>1</tpOper><tpAmb>2</tpAmb><aplicEmi>1</aplicEmi><verAplic>dere-poc/0.1</verAplic></ideEvento>
            <ideContrib><nrInsc>12345678</nrInsc></ideContrib>
            <idePeriodo><iniValid>2026-10-01</iniValid></idePeriodo>
            <infoPGCC>
              <planoCtaRef>5</planoCtaRef>
              <freqEncerr>A</freqEncerr>
              <infoContas>
                <infoConta>
                  <cCta>1101000</cCta>
                  <cCtaInterna>1101</cCtaInterna>
                  <cDbrMista>000</cDbrMista>
                  <nomeCta>Disponibilidades</nomeCta>
                  <indCta>A</indCta>
                  <cCtaRef>101000000</cCtaRef>
                  <nivelCta>2</nivelCta>
                  <natCta>D</natCta>
                  <codNat>1</codNat>
                  <iniVig>2026-10-01</iniVig>
                </infoConta>
              </infoContas>
            </infoPGCC>
          </evtPGCC>
          <Signature xmlns="http://www.w3.org/2000/09/xmldsig#"/>
        </DeRE>
        """;
    var issues = validate(xml);
    assertEquals(List.of(), issues, () -> issues.stream().map(XmlSupport.Issue::message).reduce("", (a, b) -> a + "\n" + b));
  }

  @Test
  void rejectsCsvWithoutAccounts() {
    try {
      new D1011CsvConverter().fromCsv("id,cCta\n");
      org.junit.jupiter.api.Assertions.fail("deveria rejeitar CSV sem conta");
    } catch (IllegalArgumentException ex) {
      assertTrue(ex.getMessage().contains("cabeçalho"));
    }
  }

  private static List<XmlSupport.Issue> validate(String xml) {
    var xsd = D1011ExampleCsvXsdTest.class.getResource("/dere/schemas/v1_1_0/xsd/06-XSD-D-1011 (v. 1.0.1).xsd");
    return XmlSupport.validateCollecting(xml, xsd).stream().filter(issue -> !"aviso".equals(issue.severity())).toList();
  }
}
