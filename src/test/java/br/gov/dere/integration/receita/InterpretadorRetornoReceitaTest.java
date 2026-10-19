package br.gov.dere.integration.receita;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class InterpretadorRetornoReceitaTest {
  @Test
  void leOcorrenciasNoFormatoDaReceita() {
    var xml = """
        <retornoLoteEventos>
          <cdResposta>3</cdResposta>
          <descResposta>Lote processado com sucesso - Possui um ou mais eventos com ocorrências de erro.</descResposta>
          <ocorrencias>
            <codigo>MS1050</codigo>
            <descricao>O campo ID do evento não está no formato esperado.</descricao>
            <tipo>1</tipo>
          </ocorrencias>
        </retornoLoteEventos>
        """;
    var retorno = InterpretadorRetornoReceita.ler(xml);
    assertEquals("3", retorno.codigo());
    assertEquals(1, retorno.ocorrencias().size());
    assertEquals("MS1050", retorno.ocorrencias().get(0).codigo());
    assertEquals("Lote processado com sucesso - Possui um ou mais eventos com ocorrências de erro.", retorno.descricao());
  }

  @Test
  void leMs0015() {
    var xml = """
        <retornoLote versao="1.0.0">
          <codigo>MS0015</codigo>
          <descricao>Lote não recebido</descricao>
          <idTransmissao>EVT-1</idTransmissao>
          <protocolo>2.202610.1</protocolo>
          <status>REJEITADO</status>
        </retornoLote>
        """;
    var retorno = InterpretadorRetornoReceita.ler(xml);
    assertEquals("MS0015", retorno.codigo());
    assertEquals("Lote não recebido", retorno.descricao());
    assertEquals("EVT-1", retorno.identificador());
    assertEquals("2.202610.1", retorno.protocolo());
    assertEquals("REJEITADO", retorno.status());
    assertEquals("1.0.0", retorno.schema());
  }

  @Test
  void leVariasOcorrencias() {
    var xml = """
        <retorno>
          <codigo>MS0015</codigo>
          <ocorrencias>
            <ocorrencia><codigo>100</codigo><descricao>Campo ausente</descricao><tipo>1</tipo><localizacao>/DeRE/evt</localizacao></ocorrencia>
            <ocorrencia><codigo>220</codigo><descricao>Competência inválida</descricao><tipo>1</tipo></ocorrencia>
          </ocorrencias>
        </retorno>
        """;
    var retorno = InterpretadorRetornoReceita.ler(xml);
    assertEquals("MS0015", retorno.codigo());
    assertEquals(2, retorno.ocorrencias().size());
    assertEquals("100", retorno.ocorrencias().get(0).codigo());
    assertEquals("Competência inválida", retorno.ocorrencias().get(1).descricao());
  }

  @Test
  void leNamespaceEVersao() {
    var xml = """
        <r:retorno xmlns:r="http://www.receita.fazenda.gov.br/dere/lote/v1_2_0" r:versao="1.2.0">
          <r:cdResposta>MS0015</r:cdResposta>
          <r:descResposta>Lote não recebido</r:descResposta>
        </r:retorno>
        """;
    var retorno = InterpretadorRetornoReceita.ler(xml);
    assertTrue(retorno.legivel());
    assertEquals("MS0015", retorno.codigo());
    assertEquals("Lote não recebido", retorno.descricao());
    assertTrue(retorno.schema().contains("v1_2_0"));
    assertTrue(retorno.schema().contains("1.2.0"));
  }

  @Test
  void xmlMalformadoNaoQuebra() {
    var retorno = InterpretadorRetornoReceita.ler("<retorno");
    assertFalse(retorno.legivel());
    assertNull(retorno.codigo());
    assertTrue(retorno.ocorrencias().isEmpty());
  }

  @Test
  void semCodigoDeErro() {
    var retorno = InterpretadorRetornoReceita.ler("<retorno><descricao>falha interna</descricao></retorno>");
    assertTrue(retorno.legivel());
    assertNull(retorno.codigo());
    assertEquals("falha interna", retorno.descricao());
  }
}
