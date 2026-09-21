package br.gov.dere.integration.xml;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class XmlSupportValidationTest {
  @Test
  void coletaCriticasDoXsdD1001() throws Exception {
    var xml = """
        <DeRE xmlns="http://www.dere.gov.br/schemas/evtInfoContrib/v1_0_1">
          <evtInfoContrib id="curto">
            <ideEvento><tpOper>9</tpOper><tpAmb>2</tpAmb><aplicEmi>1</aplicEmi><verAplic>x</verAplic></ideEvento>
            <ideContrib><nrInsc>12345678</nrInsc></ideContrib>
            <idePeriodo><iniValid>2026-10-01</iniValid></idePeriodo>
            <infoContrib><regTribPrinc>9</regTribPrinc><indNatTrib>0</indNatTrib></infoContrib>
          </evtInfoContrib>
        </DeRE>
        """;
    var xsd = getClass().getResource("/dere/schemas/nota_2026_001/xsd/evtInfoContrib-v1_0_1.xsd");
    assertNotNull(xsd);
    var issues = XmlSupport.validateCollecting(xml, xsd);
    assertFalse(issues.isEmpty());
    var joined = issues.stream().map(XmlSupport.Issue::message).reduce("", (a,b)->a+" "+b);
    assertTrue(joined.contains("id") || joined.contains("tpOper") || joined.contains("Signature") || joined.contains("evtInfoContrib"), joined);
  }
}
