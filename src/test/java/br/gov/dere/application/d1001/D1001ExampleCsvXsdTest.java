package br.gov.dere.application.d1001;

import br.gov.dere.application.csv.DereCsvConverter;
import br.gov.dere.domain.contributor.D1001;
import br.gov.dere.integration.xml.XmlSupport;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
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

  @Test
  void csvDocumentacaoComPontoEVirgulaGeraXmlValidoNoXsd() throws Exception {
    var model = assertXsd(Path.of("docs/dere/d1001.csv"));
    assertEquals(1, model.operation());
    assertEquals("01914904", model.cnpjRoot());
    assertEquals(LocalDate.of(2026, 10, 1), model.validFrom());
    assertEquals(List.of("13B"), model.financialActivities());
    var exported = new DereCsvConverter().toCsv(model);
    assertTrue(exported.startsWith("id;motExcl;"));
    assertTrue(exported.contains(";01/10/2026;"));
  }

  private static D1001 assertXsd(Path csvPath) throws Exception {
    var csv = Files.readString(csvPath);
    var d = new DereCsvConverter().fromCsv(csv);
    assertEquals(42, d.id().length());
    assertTrue(d.id().matches("[0-9A-Za-z]{42}"));
    assertTrue(d.cnpjRoot().matches("[0-9A-Z]{8}"));
    var xml = new D1001XmlGenerator().generate(d);
    xml = XmlSupport.forSchemaValidation(xml);
    var xsd = D1001ExampleCsvXsdTest.class.getResource("/dere/schemas/nota_2026_001/xsd/evtInfoContrib-v1_0_1.xsd");
    assertNotNull(xsd);
    var issues = XmlSupport.validateCollecting(xml, xsd).stream().filter(i -> !"aviso".equals(i.severity())).toList();
    assertEquals(List.of(), issues, () -> issues.stream().map(XmlSupport.Issue::message).reduce("", (a, b) -> a + "\n" + b));
    return d;
  }

  @Test
  void xmlGeradoPeloSistemaPassaNoXsdComStubDeAssinatura() throws Exception {
    var csv = Files.readString(Path.of("docs/dere/d1001.csv"));
    var xml = new D1001XmlGenerator().generate(new DereCsvConverter().fromCsv(csv));
    assertFalse(xml.contains("<Signature"));
    var xsd = D1001ExampleCsvXsdTest.class.getResource("/dere/schemas/nota_2026_001/xsd/evtInfoContrib-v1_0_1.xsd");
    var rawIssues = XmlSupport.validateCollecting(xml, xsd).stream().filter(i -> !"aviso".equals(i.severity())).toList();
    assertFalse(rawIssues.isEmpty());
    var issues = XmlSupport.validateCollecting(XmlSupport.forSchemaValidation(xml), xsd).stream().filter(i -> !"aviso".equals(i.severity())).toList();
    assertEquals(List.of(), issues, () -> issues.stream().map(XmlSupport.Issue::message).reduce("", (a, b) -> a + "\n" + b));
  }
}
