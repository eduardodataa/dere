package br.gov.dere.application.receita;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import br.gov.dere.application.configuracao.ConfiguracaoIntegracao;
import br.gov.dere.application.configuracao.PoliticaAmbiente;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestClientResponseException;

class MontagemDetalheSituacaoTest {
  @Test
  void consultaRegistraOEnderecoEOmiteOToken() {
    var quando = Instant.parse("2026-10-27T18:40:00Z");
    var detalhe = MontagemDetalheSituacao.consultado(configuracao(), quando, null);
    assertTrue(detalhe.consultaRealizada());
    assertEquals("https://api.receitafederal.gov.br/token", detalhe.enderecoVerificado());
    assertEquals(PoliticaAmbiente.URL_PRE_PRODUCAO, detalhe.enderecoRecepcao());
    assertEquals("Pré-produção", detalhe.ambiente());
    assertEquals(200, detalhe.httpStatus());
    assertEquals("Token entregue. O valor do token não é exibido.", detalhe.resposta());
    assertEquals("2026-10-27T18:40:00Z", detalhe.verificadoEm());
  }

  @Test
  void falhaHttpGuardaStatusECorpoSemCredencial() {
    var corpo = "{\"error\":\"timeout\",\"message\":\"Erro ao gerar token\"}";
    var falha = new RestClientResponseException(
        "500 Internal Server Error",
        500,
        "Internal Server Error",
        HttpHeaders.EMPTY,
        corpo.getBytes(StandardCharsets.UTF_8),
        StandardCharsets.UTF_8);
    var detalhe = MontagemDetalheSituacao.consultado(configuracao(), Instant.parse("2026-10-27T18:40:00Z"), falha);
    assertEquals(500, detalhe.httpStatus());
    assertEquals(corpo, detalhe.resposta());
    assertTrue(detalhe.operacao().contains("somente o token"));
    assertFalse(detalhe.operacao().contains("lote não foi enviado"));
  }

  @Test
  void respostaComSegredoNaoVaiParaATela() {
    assertEquals(
        "Resposta omitida porque pode conter credencial.",
        MontagemDetalheSituacao.resumir("{\"access_token\":\"segredo\",\"token_type\":\"Bearer \"}"));
  }

  @Test
  void simuladoNaoMarcaConsulta() {
    var detalhe = MontagemDetalheSituacao.simulado(configuracao(), Instant.parse("2026-10-27T18:40:00Z"));
    assertFalse(detalhe.consultaRealizada());
    assertNull(detalhe.httpStatus());
    assertEquals("https://api.receitafederal.gov.br/token", detalhe.enderecoVerificado());
  }

  private static ConfiguracaoIntegracao configuracao() {
    return new ConfiguracaoIntegracao(
        true,
        false,
        PoliticaAmbiente.URL_PRE_PRODUCAO,
        "https://api.receitafederal.gov.br/token",
        "cliente",
        "segredo",
        true,
        "restricted-production",
        "nota_2026_001",
        1);
  }
}
