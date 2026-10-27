package br.gov.dere.application.receita;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class ClassificacaoSituacaoReceitaTest {
  @Test
  void simuladoNaoConsultaAReceita() {
    var situacao = ClassificacaoSituacaoReceita.simulado();
    assertEquals("SIMULADO", situacao.codigo());
    assertTrue(situacao.envioLiberado());
    assertEquals(0, situacao.segundosProximaConsulta());
  }

  @Test
  void tokenEntregueLiberaOEnvio() {
    var situacao = ClassificacaoSituacaoReceita.disponivel();
    assertEquals("DISPONIVEL", situacao.codigo());
    assertTrue(situacao.envioLiberado());
    assertEquals(60, situacao.segundosProximaConsulta());
    assertNull(situacao.aviso());
  }

  @Test
  void falhaAoGerarTokenBloqueiaOEnvio() {
    var situacao = ClassificacaoSituacaoReceita.falha("500 Internal Server Error: {\"error\":\"timeout\",\"message\":\"Erro ao gerar token\"}");
    assertEquals("TOKEN", situacao.codigo());
    assertFalse(situacao.envioLiberado());
    assertTrue(situacao.mensagem().contains("não entregou o token"));
    assertEquals(ClassificacaoSituacaoReceita.AVISO_SISTEMA_FORA, situacao.aviso());
  }

  @Test
  void falhaDeComunicacaoPermiteIncluirSemAceiteDaReceita() {
    var situacao = ClassificacaoSituacaoReceita.falha("I/O error on POST request");
    assertEquals("COMUNICACAO", situacao.codigo());
    assertEquals(ClassificacaoSituacaoReceita.AVISO_SISTEMA_FORA, situacao.aviso());
  }

  @Test
  void credencialAusenteNaoTrataComoSistemaFora() {
    var situacao = ClassificacaoSituacaoReceita.falha("Credenciais Receita Integra não configuradas");
    assertEquals("CREDENCIAL", situacao.codigo());
    assertNull(situacao.aviso());
  }
}
