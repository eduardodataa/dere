package br.gov.dere.application.configuracao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class PoliticaAmbienteTest {
  private final ConfiguracaoIntegracao base = new ConfiguracaoIntegracao(
      true, false, "https://api.receitafederal.gov.br/dere", "https://api.receitafederal.gov.br/token",
      "id", "secret", true, "production", "nota_2026_001");

  @Test
  void stagingSoSimula() {
    var atual = PoliticaAmbiente.aplicar("staging", base);
    assertFalse(atual.realAdapterEnabled());
    assertTrue(atual.mockEnabled());
    assertEquals("Ambiente de staging. Os envios são simulados.", PoliticaAmbiente.rodape("staging"));
  }

  @Test
  void homologacaoSoPreProducao() {
    var atual = PoliticaAmbiente.aplicar("hml", base);
    assertTrue(atual.realAdapterEnabled());
    assertFalse(atual.mockEnabled());
    assertEquals(PoliticaAmbiente.URL_PRE_PRODUCAO, atual.baseUrl());
    assertEquals("restricted-production", atual.environment());
    assertEquals("Ambiente de homologação. Os envios são de pré-produção.", PoliticaAmbiente.rodape("homologacao"));
  }

  @Test
  void producaoSoProducao() {
    var atual = PoliticaAmbiente.aplicar("producao", new ConfiguracaoIntegracao(
        false, true, PoliticaAmbiente.URL_PRE_PRODUCAO, "https://api.receitafederal.gov.br/token",
        "id", null, false, "restricted-production", "nota_2026_001"));
    assertTrue(atual.realAdapterEnabled());
    assertFalse(atual.mockEnabled());
    assertEquals(PoliticaAmbiente.URL_PRODUCAO, atual.baseUrl());
    assertEquals("production", atual.environment());
    assertNull(PoliticaAmbiente.rodape("producao"));
  }
}
