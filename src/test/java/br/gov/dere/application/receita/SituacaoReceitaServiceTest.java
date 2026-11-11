package br.gov.dere.application.receita;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.gov.dere.application.configuracao.ConfiguracaoIntegracao;
import br.gov.dere.application.configuracao.ConfiguracaoIntegracaoService;
import br.gov.dere.application.credencial.CredencialEntidade;
import br.gov.dere.application.credencial.CredenciaisEntidadeService;
import br.gov.dere.integration.receita.ReceitaIntegraAuthClient;
import org.junit.jupiter.api.Test;

class SituacaoReceitaServiceTest {
  @Test
  void confirmadaNaoPedeOutroToken() {
    var auth = mock(ReceitaIntegraAuthClient.class);
    var service = servico(auth, credCompleta());
    when(auth.renovar()).thenReturn("tok");
    var primeira = service.atual(1L);
    var segunda = service.atual(1L);
    verify(auth, times(1)).renovar();
    assertEquals("DISPONIVEL", primeira.codigo());
    assertEquals(0, segunda.segundosProximaConsulta());
    assertTrue(segunda.envioLiberado());
  }

  @Test
  void limiteNaoRepeteAConsultaAntesDaEspera() {
    var auth = mock(ReceitaIntegraAuthClient.class);
    var service = servico(auth, credCompleta());
    when(auth.renovar()).thenThrow(new IllegalStateException("429 Too Many Requests: API rate limit exceeded"));
    var limitada = service.atual(1L);
    service.atual(1L);
    verify(auth, times(1)).renovar();
    assertEquals("LIMITE", limitada.codigo());
    assertEquals(ClassificacaoSituacaoReceita.INTERVALO_LIMITE_SEGUNDOS, limitada.segundosProximaConsulta());
  }

  @Test
  void indisponivelVoltaAConsultar() {
    var auth = mock(ReceitaIntegraAuthClient.class);
    var service = servico(auth, credCompleta());
    when(auth.renovar()).thenReturn("tok");
    service.atual(1L);
    service.registrarIndisponivel(1L);
    when(auth.renovar()).thenThrow(new IllegalStateException("I/O error on POST request"));
    var queda = service.atual(1L);
    verify(auth, times(2)).renovar();
    assertEquals("COMUNICACAO", queda.codigo());
    assertEquals(ClassificacaoSituacaoReceita.INTERVALO_FORA_SEGUNDOS, queda.segundosProximaConsulta());
  }

  @Test
  void credencialVencidaNaoPedeToken() {
    var auth = mock(ReceitaIntegraAuthClient.class);
    var settings = mock(ConfiguracaoIntegracaoService.class);
    var credenciais = mock(CredenciaisEntidadeService.class);
    when(settings.current()).thenReturn(new ConfiguracaoIntegracao(
        true, false, "https://api.receitafederal.gov.br/dere", "https://api.receitafederal.gov.br/token",
        "id", "segredo", true, "production", "nota_2026_001", 50, null));
    when(credenciais.carregar(1L)).thenReturn(new CredencialEntidade(1L, "id", "segredo", true, "2020-01-01"));
    var situacao = new SituacaoReceitaService(settings, credenciais, auth).atual(1L);
    verify(auth, never()).renovar();
    assertEquals("VALIDADE", situacao.codigo());
    assertFalse(situacao.envioLiberado());
    assertEquals("2020-01-01", situacao.validadeCredencial());
    assertTrue(situacao.mensagem().contains("01/01/2020"));
    assertFalse(situacao.detalhe().consultaRealizada());
  }

  @Test
  void semCredencialNaEntidadeBloqueia() {
    var auth = mock(ReceitaIntegraAuthClient.class);
    var settings = mock(ConfiguracaoIntegracaoService.class);
    var credenciais = mock(CredenciaisEntidadeService.class);
    when(settings.current()).thenReturn(new ConfiguracaoIntegracao(
        true, false, "https://api.receitafederal.gov.br/dere", "https://api.receitafederal.gov.br/token",
        "", "", false, "production", "nota_2026_001", 50, null));
    when(credenciais.carregar(1L)).thenReturn(new CredencialEntidade(1L, null, null, false, null));
    var situacao = new SituacaoReceitaService(settings, credenciais, auth).atual(1L);
    verify(auth, never()).renovar();
    assertEquals("CREDENCIAL", situacao.codigo());
    assertFalse(situacao.envioLiberado());
  }

  private static SituacaoReceitaService servico(ReceitaIntegraAuthClient auth, CredencialEntidade cred) {
    var settings = mock(ConfiguracaoIntegracaoService.class);
    var credenciais = mock(CredenciaisEntidadeService.class);
    when(settings.current()).thenReturn(new ConfiguracaoIntegracao(
        true, false, "https://api.receitafederal.gov.br/prr-dere", "https://api.receitafederal.gov.br/token",
        "id", "segredo", true, "restricted-production", "nota_2026_001", 50, null));
    when(credenciais.carregar(1L)).thenReturn(cred);
    return new SituacaoReceitaService(settings, credenciais, auth);
  }

  private static CredencialEntidade credCompleta() {
    return new CredencialEntidade(1L, "id", "segredo", true, null);
  }
}
