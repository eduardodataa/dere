package br.gov.dere.integration.receita;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class ParserRespostaGovernoTest {
  @Test void extractsNamespacedProtocolAndReceipt() {
    String xml="<r:resultado xmlns:r=\"urn:test\"><r:protocolo>ABC-123</r:protocolo><r:recibo>REC-9</r:recibo><r:status>PROCESSING</r:status></r:resultado>";
    assertEquals("ABC-123", ParserRespostaGoverno.protocol(xml));
    assertEquals("REC-9", ParserRespostaGoverno.receipt(xml));
    assertEquals("PROCESSING", ParserRespostaGoverno.status(xml));
  }

  @Test void acceptsAlternateFieldNames() {
    String xml="<retorno><nrProt>P-88</nrProt><numeroRecibo>R-2</numeroRecibo><situacao>ACEITO</situacao><codigoErro>E1</codigoErro></retorno>";
    assertEquals("P-88", ParserRespostaGoverno.protocol(xml));
    assertEquals("R-2", ParserRespostaGoverno.receipt(xml));
    assertEquals("ACEITO", ParserRespostaGoverno.status(xml));
    assertEquals("E1", ParserRespostaGoverno.code(xml));
  }

  @Test void returnsNullForBlankOrInvalidXml() {
    org.junit.jupiter.api.Assertions.assertNull(ParserRespostaGoverno.protocol(" "));
    org.junit.jupiter.api.Assertions.assertNull(ParserRespostaGoverno.protocol("<nao-xml"));
  }
}
