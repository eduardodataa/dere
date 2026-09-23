package br.gov.dere.application.layout;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import br.gov.dere.application.periodico.D1101CsvConverter;
import br.gov.dere.application.periodico.D1101XmlGenerator;
import br.gov.dere.application.periodico.D1199CsvConverter;
import br.gov.dere.application.periodico.D1199XmlGenerator;
import br.gov.dere.application.validation.ValidadorCamposCsv;
import br.gov.dere.integration.xml.XmlSupport;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class EnvioCicloPeriodicoTest {
  private final D1101CsvConverter csv1101 = new D1101CsvConverter();
  private final D1101XmlGenerator xml1101 = new D1101XmlGenerator();
  private final D1199CsvConverter csv1199 = new D1199CsvConverter();
  private final D1199XmlGenerator xml1199 = new D1199XmlGenerator();

  @Test
  void ciclo1101GeraXmlValido() throws Exception {
    for (var nome : new String[] { "1101-incluir", "1101-alterar", "1101-excluir" }) {
      var csv = Files.readString(Path.of("examples/envio-ciclo/" + nome + ".csv"));
      var criticas = ValidadorCamposCsv.validar("D-1101", nome + ".csv", csv);
      assertTrue(criticas.isEmpty(), nome + " " + criticas);
      var xml = xml1101.generate(csv1101.fromCsv(csv));
      var xsd = EnvioCicloPeriodicoTest.class.getResource("/dere/schemas/v1_1_0/xsd/06-XSD-D-1101 (v. 0.0.1).xsd");
      var problemas = XmlSupport.validateCollecting(XmlSupport.forSchemaValidation(xml), xsd).stream()
          .filter(item -> !"aviso".equals(item.severity()))
          .toList();
      assertTrue(problemas.isEmpty(), nome + " " + problemas);
      assertEquals(csv1101.fromCsv(csv).id(), csv1101.fromXml(xml).id());
    }
  }

  @Test
  void ciclo1199GeraXmlDeInclusao() throws Exception {
    for (var nome : new String[] { "1199-incluir", "1199-incluir-bcn-peps", "1199-incluir-bcn-manual" }) {
      var csv = Files.readString(Path.of("examples/envio-ciclo/" + nome + ".csv"));
      var criticas = ValidadorCamposCsv.validar("D-1199", nome + ".csv", csv);
      assertTrue(criticas.isEmpty(), nome + " " + criticas);
      var xml = xml1199.generate(csv1199.fromCsv(csv));
      assertEquals(1, csv1199.fromXml(xml).operation());
      assertEquals(csv1199.fromCsv(csv).id(), csv1199.fromXml(xml).id());
    }
  }
}
