package br.gov.dere.application.receita;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.gov.dere.application.configuracao.ConfiguracaoIntegracao;
import br.gov.dere.application.configuracao.ConfiguracaoIntegracaoService;
import br.gov.dere.integration.receita.ReceitaIntegraAuthClient;
import org.junit.jupiter.api.Test;

class SituacaoReceitaServiceTest {
  @Test
  void confirmadaNaoPedeOutroToken() {
    var auth = mock(ReceitaIntegraAuthClient.class);
    var service = servico(auth);
    when(auth.renovar()).thenReturn("tok");
    var primeira = service.atual();
    var segunda = service.atual();
    verify(auth, times(1)).renovar();
    assertEquals("DISPONIVEL", primeira.codigo());
    assertEquals(0, segunda.segundosProximaConsulta());
    assertTrue(segunda.envioLiberado());
  }

  @Test
  void limiteNaoRepeteAConsultaAntesDaEspera() {
    var auth = mock(ReceitaIntegraAuthClient.class);
    var service = servico(auth);
    when(auth.renovar()).thenThrow(new IllegalStateException("429 Too Many Requests: API rate limit exceeded"));
    var limitada = service.atual();
    service.atual();
    verify(auth, times(1)).renovar();
    assertEquals("LIMITE", limitada.codigo());
    assertEquals(ClassificacaoSituacaoReceita.INTERVALO_LIMITE_SEGUNDOS, limitada.segundosProximaConsulta());
  }

  @Test
  void indisponivelVoltaAConsultar() {
    var auth = mock(ReceitaIntegraAuthClient.class);
    var service = servico(auth);
    when(auth.renovar()).thenReturn("tok");
    service.atual();
    service.registrarIndisponivel();
    when(auth.renovar()).thenThrow(new IllegalStateException("I/O error on POST request"));
    var queda = service.atual();
    verify(auth, times(2)).renovar();
    assertEquals("COMUNICACAO", queda.codigo());
    assertEquals(ClassificacaoSituacaoReceita.INTERVALO_FORA_SEGUNDOS, queda.segundosProximaConsulta());
  }

  private static SituacaoReceitaService servico(ReceitaIntegraAuthClient auth) {
    var settings = mock(ConfiguracaoIntegracaoService.class);
    when(settings.current()).thenReturn(new ConfiguracaoIntegracao(
        true, false, "https://api.receitafederal.gov.br/prr-dere", "https://api.receitafederal.gov.br/token",
        "id", "segredo", true, "restricted-production", "nota_2026_001", 50));
    return new SituacaoReceitaService(settings, auth);
  }
}
