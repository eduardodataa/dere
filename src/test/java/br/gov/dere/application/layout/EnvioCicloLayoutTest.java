package br.gov.dere.application.layout;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import br.gov.dere.application.csv.DereCsvConverter;
import br.gov.dere.application.d1001.D1001XmlGenerator;
import br.gov.dere.application.pgcc.D1011CsvConverter;
import br.gov.dere.application.pgcc.D1011XmlGenerator;
import br.gov.dere.application.validation.ValidadorCamposCsv;
import br.gov.dere.integration.xml.XmlSupport;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;

class EnvioCicloLayoutTest {
  private static final Path CICLO = Path.of("examples/envio-ciclo");

  @Test
  void cicloD1001IncluirAlterarExcluir() throws Exception {
    var converter = new DereCsvConverter();
    var generator = new D1001XmlGenerator();
    var incluir = converter.fromCsv(Files.readString(CICLO.resolve("1001-incluir.csv")));
    var alterar = converter.fromCsv(Files.readString(CICLO.resolve("1001-alterar.csv")));
    var excluir = converter.fromCsv(Files.readString(CICLO.resolve("1001-excluir.csv")));
    assertEquals(1, incluir.operation());
    assertEquals(2, alterar.operation());
    assertEquals(3, excluir.operation());
    assertEquals(2, excluir.exclusionReason());
    assertEquals(incluir.cnpjRoot(), alterar.cnpjRoot());
    assertEquals(incluir.validFrom(), alterar.validFrom());
    assertEquals(incluir.cnpjRoot(), excluir.cnpjRoot());
    assertEquals(incluir.validFrom(), excluir.validFrom());
    var xmlIncluir = escrever(CICLO.resolve("1001-incluir.xml"), generator.generate(incluir));
    var xmlAlterar = escrever(CICLO.resolve("1001-alterar.xml"), generator.generate(alterar));
    var xmlExcluir = escrever(CICLO.resolve("1001-excluir.xml"), generator.generate(excluir));
    assertXsd(xmlIncluir, "/dere/schemas/nota_2026_001/xsd/evtInfoContrib-v1_0_1.xsd");
    assertXsd(xmlAlterar, "/dere/schemas/nota_2026_001/xsd/evtInfoContrib-v1_0_1.xsd");
    assertXsd(xmlExcluir, "/dere/schemas/nota_2026_001/xsd/evtInfoContrib-v1_0_1.xsd");
    assertTrue(xmlIncluir.contains("<tpOper>1</tpOper>"));
    assertTrue(xmlAlterar.contains("<tpOper>2</tpOper>"));
    assertTrue(xmlExcluir.contains("<tpOper>3</tpOper>"));
    assertTrue(xmlExcluir.contains("<motExcl>2</motExcl>"));
    assertFalse(xmlExcluir.contains("<infoContrib"));
    var csvExcluir = converter.toCsv(converter.fromXml(xmlExcluir));
    var criticas = ValidadorCamposCsv.validar("D-1001", "1001-excluir.xml", csvExcluir);
    assertEquals(List.of(), criticas, () -> criticas.toString());
  }

  @Test
  void cicloD1011IncluirAlterarExcluir() throws Exception {
    var converter = new D1011CsvConverter();
    var generator = new D1011XmlGenerator();
    var incluir = converter.fromCsv(Files.readString(CICLO.resolve("1011-incluir.csv")));
    var alterar = converter.fromCsv(Files.readString(CICLO.resolve("1011-alterar.csv")));
    var excluir = converter.fromCsv(Files.readString(CICLO.resolve("1011-excluir.csv")));
    assertEquals(1, incluir.operation());
    assertEquals(2, alterar.operation());
    assertEquals(3, excluir.operation());
    assertEquals(2, excluir.exclusionReason());
    assertFalse(incluir.accounts().isEmpty());
    assertEquals(incluir.accounts().size(), alterar.accounts().size());
    assertTrue(excluir.accounts().isEmpty());
    assertEquals(incluir.cnpjRoot(), alterar.cnpjRoot());
    assertEquals(incluir.validFrom(), excluir.validFrom());
    var xmlIncluir = escrever(CICLO.resolve("1011-incluir.xml"), generator.generate(incluir));
    var xmlAlterar = escrever(CICLO.resolve("1011-alterar.xml"), generator.generate(alterar));
    var xmlExcluir = escrever(CICLO.resolve("1011-excluir.xml"), generator.generate(excluir));
    assertXsd(xmlIncluir, "/dere/schemas/v1_1_0/xsd/06-XSD-D-1011 (v. 1.0.1).xsd");
    assertXsd(xmlAlterar, "/dere/schemas/v1_1_0/xsd/06-XSD-D-1011 (v. 1.0.1).xsd");
    assertXsd(xmlExcluir, "/dere/schemas/v1_1_0/xsd/06-XSD-D-1011 (v. 1.0.1).xsd");
    assertTrue(xmlIncluir.contains("<tpOper>1</tpOper>"));
    assertTrue(xmlAlterar.contains("<tpOper>2</tpOper>"));
    assertTrue(xmlExcluir.contains("<tpOper>3</tpOper>"));
    assertFalse(xmlExcluir.contains("<infoPGCC"));
  }

  private static String escrever(Path path, String xml) throws Exception {
    Files.createDirectories(path.getParent());
    Files.writeString(path, xml);
    return xml;
  }

  private static void assertXsd(String xml, String xsdPath) {
    var xsd = EnvioCicloLayoutTest.class.getResource(xsdPath);
    var issues = XmlSupport.validateCollecting(XmlSupport.forSchemaValidation(xml), xsd).stream()
        .filter(issue -> !"aviso".equals(issue.severity())).toList();
    assertEquals(List.of(), issues, () -> issues.stream().map(XmlSupport.Issue::message).reduce("", (a, b) -> a + "\n" + b));
  }
}
