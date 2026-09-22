package br.gov.dere.application.pgcc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import br.gov.dere.domain.layout.CsvDocument;
import br.gov.dere.domain.layout.ValidationContext;
import br.gov.dere.application.layout.D1011LayoutDefinition;
import org.junit.jupiter.api.Test;

class D1011CsvConverterTest {
  @Test
  void convertsCsvToCanonicalModelAndBackToXml() throws Exception {
    var csv = new String(java.nio.file.Files.readAllBytes(java.nio.file.Path.of("examples/csv/d1011-pgcc-previc.csv")), java.nio.charset.StandardCharsets.UTF_8);
    var definition = new D1011LayoutDefinition();
    var model = definition.parseCsv(new CsvDocument(csv));
    assertEquals("D-1011", definition.layout().displayName());
    assertEquals(1, model.accounts().size());
    assertTrue(definition.validate(model, new ValidationContext(1L, null)).valid());
    var xml = definition.generateXml(model);
    assertTrue(xml.content().contains("evtPGCC"));
    assertTrue(xml.content().contains("1.1.01"));
    assertEquals("1.1.01", definition.parseXml(xml).accounts().get(0).accountCode());
  }

  @Test
  void leCsvComPontoEVirgulaEDataBrasileira() {
    var csv = """
        id;tpOper;tpAmb;aplicEmi;verAplic;nrInsc;iniValid;fimValid;planoCtaRef;freqEncerr;cCta;cCtaInterna;cDbrMista;nomeCta;indCta;descCta;cCtaSup;cCtaRef;nivelCta;natCta;codNat;codTrib;indTribISS;idLeiDisp;iniVig;fimVig
        DeRE10111000000123456782026090212000000007;1;2;1;dere-poc/0.1;12345678;01/10/2026;;5;A;1.1.01;1.1.01;0;Disponibilidades;1;Conta de disponibilidades;;101000000;2;1;01;220110001;0;;01/10/2026;
        """;
    var model = new D1011CsvConverter().fromCsv(csv);
    assertEquals(1, model.accounts().size());
    assertEquals("1.1.01", model.accounts().get(0).accountCode());
    assertEquals(java.time.LocalDate.of(2026, 10, 1), model.validFrom());
  }
}
