package br.gov.dere.application.transmissao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import br.gov.dere.integration.receita.ExcecaoTransmissaoGoverno;
import org.junit.jupiter.api.Test;

class ApresentacaoErroTransmissaoTest {
  @Test
  void http422ComMs0015NaoExpoeXml() {
    var xml = """
        <retorno xmlns="http://www.receita.fazenda.gov.br/dere/lote/v1_0_0" versao="1.0.0">
          <codigo>MS0015</codigo>
          <descricao>Lote não recebido</descricao>
          <idTransmissao>EVT-9</idTransmissao>
        </retorno>
        """;
    var diagnostico = ApresentacaoErroTransmissao.deGoverno(new ExcecaoTransmissaoGoverno(422, false, xml), "EVT-LOCAL");
    assertEquals(422, diagnostico.httpAplicacao());
    assertEquals(422, diagnostico.http());
    assertEquals("MS0015", diagnostico.codigo());
    assertEquals("Não foi possível transmitir a declaração", diagnostico.titulo());
    assertEquals("A Receita Federal não recebeu o lote.", diagnostico.mensagem());
    assertEquals("O arquivo passou pela validação local, mas o envio foi recusado. Consulte os detalhes do retorno antes de tentar novamente.", diagnostico.orientacao());
    assertEquals("REJEICAO_RECEITA", diagnostico.categoria());
    assertFalse(diagnostico.mensagem().contains("<"));
    assertFalse(diagnostico.corpo().get("message").toString().contains("MS0015") && diagnostico.mensagem().contains("<retorno"));
    assertFalse(diagnostico.repetir());
    assertEquals("EVT-9", diagnostico.identificador());
  }

  @Test
  void codigoDesconhecidoFicaGenerico() {
    var xml = "<retorno><codigo>MS9999</codigo><descricao>Regra não catalogada</descricao></retorno>";
    var diagnostico = ApresentacaoErroTransmissao.deGoverno(new ExcecaoTransmissaoGoverno(422, false, xml), null);
    assertEquals("A Receita Federal recusou o envio.", diagnostico.mensagem());
    assertEquals("MS9999", diagnostico.codigo());
    assertEquals("Regra não catalogada", diagnostico.descricaoOriginal());
    assertFalse(diagnostico.mensagem().contains("MS9999"));
  }

  @Test
  void xmlMalformadoNaoVazaParaMensagem() {
    var diagnostico = ApresentacaoErroTransmissao.deGoverno(new ExcecaoTransmissaoGoverno(422, false, "<retorno"), null);
    assertEquals("A Receita Federal recusou o envio.", diagnostico.mensagem());
    assertEquals("O retorno não pôde ser interpretado.", diagnostico.descricaoOriginal());
    assertFalse(diagnostico.mensagem().contains("<"));
  }

  @Test
  void comunicacaoSemResposta() {
    var diagnostico = ApresentacaoErroTransmissao.deGoverno(new ExcecaoTransmissaoGoverno(502, false, ""), null);
    assertEquals("COMUNICACAO", diagnostico.categoria());
    assertTrue(diagnostico.inconclusivo());
    assertFalse(diagnostico.repetir());
    assertEquals(504, diagnostico.httpAplicacao());
  }

  @Test
  void timeoutNaoAutorizaReenvio() {
    var diagnostico = ApresentacaoErroTransmissao.deGoverno(
        new ExcecaoTransmissaoGoverno(408, false, "SUBMIT timeout/indisponível: Read timed out"), null);
    assertTrue(diagnostico.inconclusivo());
    assertFalse(diagnostico.repetir());
    assertFalse(diagnostico.mensagem().toLowerCase().contains("tente novamente"));
  }

  @Test
  void duplicidadeLocalNaoAutorizaReenvio() {
    var diagnostico = ApresentacaoErroTransmissao.deFalhaLocal(new IllegalStateException("Duplicate entry uk_transmission_attempt"), "EVT-1");
    assertTrue(diagnostico.inconclusivo());
    assertFalse(diagnostico.repetir());
    assertEquals("EVT-1", diagnostico.identificador());
  }

  @Test
  void logOmiteSegredo() {
    var limpo = ApresentacaoErroTransmissao.sanitizar("<retorno><senha>secreta</senha><codigo>MS0015</codigo></retorno>");
    assertFalse(limpo.contains("secreta"));
    assertTrue(limpo.contains("MS0015"));
  }
}
