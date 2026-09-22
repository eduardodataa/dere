package br.gov.dere.application.transmission;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import br.gov.dere.integration.signature.DereXmlSigner;
import br.gov.dere.integration.signature.LocalTestCertificate;
import br.gov.dere.integration.xml.XmlSupport;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;

class OfficialReturnXmlBuilderTest {
  @Test
  void recepcaoEConsultaPassamNoXsdOficial() throws Exception {
    var builder = new OfficialReturnXmlBuilder();
    var agora = Instant.parse("2026-09-22T21:34:00Z");
    var protocolo = "1.20260922213400.000001";
    var chave = LocalTestCertificate.create();
    var signer = new DereXmlSigner();
    var recepcao = signer.sign(builder.recepcao("12345678", "12345678", protocolo, agora), chave, "changeit".toCharArray(), "dere-test");
    assertXsd(recepcao, "/dere/schemas/nota_2026_001/xsd/retornoLoteDere-v1_0_1.xsd");
    var tabela = signer.sign(builder.eventoTabela(
        "DeRE10011234567820261001120000000000000001", "12345678", "D-1001", protocolo,
        "1.1.2026092221340000001", "abcdefghijklmnopqrstuvwxyz0123456789ABCD", agora, agora,
        LocalDate.of(2026, 10, 1), null), chave, "changeit".toCharArray(), "dere-test");
    assertXsd(tabela, "/dere/schemas/nota_2026_001/xsd/evtRetornoTabela-v1_0_1.xsd");
    var consulta = signer.sign(builder.consulta("12345678", "12345678", protocolo, agora, agora, tabela), chave, "changeit".toCharArray(), "dere-test");
    assertXsd(consulta, "/dere/schemas/nota_2026_001/xsd/retornoLoteDere-v1_0_1.xsd");
    assertTrue(consulta.contains("<protocolo>1.20260922213400.000001</protocolo>"));
    assertTrue(consulta.contains("<nrRecibo>1.1.2026092221340000001</nrRecibo>"));
  }

  private static void assertXsd(String xml, String xsd) {
    var url = OfficialReturnXmlBuilderTest.class.getResource(xsd);
    var issues = XmlSupport.validateCollecting(xml, url).stream().filter(issue -> !"aviso".equals(issue.severity())).toList();
    assertEquals(List.of(), issues, () -> issues.stream().map(XmlSupport.Issue::message).reduce("", (a, b) -> a + "\n" + b));
  }
}
