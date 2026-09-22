package br.gov.dere.integration.receita;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class GovernmentResponseParserTest {
  @Test void extractsNamespacedProtocolAndReceipt() {
    String xml="<r:resultado xmlns:r=\"urn:test\"><r:protocolo>ABC-123</r:protocolo><r:recibo>REC-9</r:recibo><r:status>PROCESSING</r:status></r:resultado>";
    assertEquals("ABC-123", GovernmentResponseParser.protocol(xml));
    assertEquals("REC-9", GovernmentResponseParser.receipt(xml));
    assertEquals("PROCESSING", GovernmentResponseParser.status(xml));
  }

  @Test void acceptsAlternateFieldNames() {
    String xml="<retorno><nrProt>P-88</nrProt><numeroRecibo>R-2</numeroRecibo><situacao>ACEITO</situacao><codigoErro>E1</codigoErro></retorno>";
    assertEquals("P-88", GovernmentResponseParser.protocol(xml));
    assertEquals("R-2", GovernmentResponseParser.receipt(xml));
    assertEquals("ACEITO", GovernmentResponseParser.status(xml));
    assertEquals("E1", GovernmentResponseParser.code(xml));
  }

  @Test void returnsNullForBlankOrInvalidXml() {
    org.junit.jupiter.api.Assertions.assertNull(GovernmentResponseParser.protocol(" "));
    org.junit.jupiter.api.Assertions.assertNull(GovernmentResponseParser.protocol("<nao-xml"));
  }
}
