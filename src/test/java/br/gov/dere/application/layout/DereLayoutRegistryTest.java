package br.gov.dere.application.layout;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import br.gov.dere.domain.layout.CsvDocument;
import br.gov.dere.domain.layout.LayoutCode;
import br.gov.dere.domain.layout.ValidationContext;
import java.util.List;
import org.junit.jupiter.api.Test;

class DereLayoutRegistryTest {
  private final D1001LayoutDefinition d1001 = new D1001LayoutDefinition();
  private final D1011LayoutDefinition d1011 = new D1011LayoutDefinition();
  private final DereLayoutRegistry registry = new DereLayoutRegistry(List.of(d1001, d1011));

  @Test
  void exposesRegisteredLayoutAndVersion() {
    assertEquals(LayoutCode.D1001, registry.get(LayoutCode.D1001).layout());
    assertEquals("1.0.1", registry.get(LayoutCode.D1001).version().value());
  }

  @Test
  void rejectsUnknownLayout() {
    var empty = new DereLayoutRegistry(List.of());
    assertThrows(IllegalArgumentException.class, () -> empty.get(LayoutCode.D1001));
    assertEquals(List.of(LayoutCode.D1001, LayoutCode.D1011), registry.availableLayouts());
  }

  @Test
  void usesCanonicalModelForCsvAndValidation() throws Exception {
    var model = d1001.parseCsv(new CsvDocument(
        "id,motExcl,nrProc,tpOper,tpAmb,aplicEmi,verAplic,nrInsc,iniValid,fimValid,novaValidadeIniValid,novaValidadeFimValid,regTribPrinc,regTribSecund,indNatTrib,tpAtividadeServFinanc,tpAtividadePlAssistSaude,tpAtividadePrognosticos,UFCredenc\n"
            + "ID1,,,1,2,1,1.0,12345678,2026-01-01,,,,1,,1,,,,"));

    assertEquals("ID1", model.id());
    assertEquals("ID1", d1001.parseXml(d1001.generateXml(model)).id());
    assertEquals(true, d1001.validate(model, new ValidationContext(1L, null)).valid());
    assertEquals(LayoutCode.D1001, d1001.layout());
  }
}
