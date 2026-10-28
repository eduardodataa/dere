package br.gov.dere.application.transmissao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.gov.dere.application.configuracao.ConfiguracaoIntegracao;
import br.gov.dere.application.configuracao.ConfiguracaoIntegracaoService;
import br.gov.dere.application.d1001.D1001ImportService;
import br.gov.dere.application.receita.ClassificacaoSituacaoReceita;
import br.gov.dere.application.receita.SituacaoReceitaService;
import br.gov.dere.domain.EventStatus;
import br.gov.dere.domain.transmissao.ResultadoEnvio;
import br.gov.dere.integration.receita.ExcecaoTransmissaoGoverno;
import org.springframework.web.server.ResponseStatusException;
import br.gov.dere.persistence.DereBatchEntity;
import br.gov.dere.persistence.DereBatchRepository;
import br.gov.dere.persistence.DereEventEntity;
import br.gov.dere.persistence.DereEventRepository;
import br.gov.dere.persistence.DereTentativaTransmissaoRepository;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class ReenvioLoteNaoTransmitidoTest {
  @Test
  void reenviaOMesmoXmlEMarcaOProtocoloQuandoOTokenVolta() {
    var cenario = cenario(true, true);
    when(cenario.government.submit("<lote/>", "EVT-1")).thenReturn(new ResultadoEnvio("2.202610.1", "1", "<ok/>", 201));
    cenario.job.reenviarPendentes();
    verify(cenario.government).submit("<lote/>", "EVT-1");
    verify(cenario.importacao).persistValidatedXml(1L, 8L, "1001.xml", "<evt/>");
    assertEquals(EventStatus.PROCESSING, cenario.batch.getStatus());
    assertEquals("2.202610.1", cenario.batch.getProtocol());
    assertEquals(EventStatus.PROTOCOL_RECEIVED, cenario.evento.getStatus());
    cenario.job.reenviarPendentes();
    verify(cenario.government, times(1)).submit(any(), any());
  }

  @Test
  void naoEnviaEnquantoAReceitaNaoEntregaOToken() {
    var cenario = cenario(true, false);
    cenario.job.reenviarPendentes();
    verify(cenario.government, never()).submit(any(), any());
    assertEquals(EventStatus.AGUARDANDO_ENVIO, cenario.batch.getStatus());
  }

  @Test
  void naoEnviaNoAdaptadorSimulado() {
    var cenario = cenario(false, true);
    cenario.job.reenviarPendentes();
    verify(cenario.situacao, never()).atual();
    verify(cenario.government, never()).submit(any(), any());
  }

  @Test
  void falhaAntesDoPostAdiaERepeteOMesmoId() {
    var cenario = cenario(true, true);
    when(cenario.government.submit(any(), any())).thenThrow(new EnvioNaoIniciadoException(new IllegalStateException("token")));
    cenario.job.reenviarPendentes();
    assertEquals(EventStatus.AGUARDANDO_ENVIO, cenario.batch.getStatus());
    assertTrue(cenario.batch.getNextQueryAt().isAfter(Instant.now()));
    cenario.job.reenviarPendentes();
    verify(cenario.government, times(1)).submit("<lote/>", "EVT-1");
  }

  @Test
  void timeoutDepoisDoPostParaENaoCriaOutroEnvio() {
    var cenario = cenario(true, true);
    when(cenario.government.submit(any(), any())).thenThrow(new ExcecaoTransmissaoGoverno(408, false, "timeout"));
    cenario.job.reenviarPendentes();
    assertEquals(EventStatus.RESULTADO_DESCONHECIDO, cenario.batch.getStatus());
    assertEquals(EventStatus.RESULTADO_DESCONHECIDO, cenario.evento.getStatus());
    cenario.job.reenviarPendentes();
    verify(cenario.government, times(1)).submit(any(), any());
    verify(cenario.importacao, never()).persistValidatedXml(any(), any(), any(), any());
  }

  @Test
  void recusaDefinitivaGravaRejeicao() {
    var cenario = cenario(true, true);
    when(cenario.government.submit(any(), any())).thenThrow(new ExcecaoTransmissaoGoverno(400, false, "schema"));
    cenario.job.reenviarPendentes();
    assertEquals(EventStatus.REJECTED, cenario.batch.getStatus());
    assertEquals(EventStatus.REJECTED, cenario.evento.getStatus());
    cenario.job.reenviarPendentes();
    verify(cenario.government, times(1)).submit(any(), any());
  }

  @Test
  void duplicidadeParaParaConciliacaoSemIdNovo() {
    var cenario = cenario(true, true);
    when(cenario.government.submit(any(), any())).thenThrow(new ExcecaoTransmissaoGoverno(400, false, "Evento em duplicidade"));
    cenario.job.reenviarPendentes();
    assertEquals(EventStatus.RESULTADO_DESCONHECIDO, cenario.batch.getStatus());
    assertEquals(EventStatus.RESULTADO_DESCONHECIDO, cenario.evento.getStatus());
    cenario.job.reenviarPendentes();
    verify(cenario.government, times(1)).submit("<lote/>", "EVT-1");
  }

  @Test
  void respostaSemProtocoloNaoRepeteOPost() {
    var cenario = cenario(true, true);
    when(cenario.government.submit(any(), any())).thenReturn(new ResultadoEnvio(" ", null, "<retorno/>", 200));
    cenario.job.reenviarPendentes();
    assertEquals(EventStatus.RESULTADO_DESCONHECIDO, cenario.batch.getStatus());
    cenario.job.reenviarPendentes();
    verify(cenario.government, times(1)).submit(any(), any());
  }

  @Test
  void botaoReenviaResultadoDesconhecidoComOMesmoId() {
    var cenario = cenario(true, true);
    cenario.batch.resultadoDesconhecido();
    cenario.evento.resultadoDesconhecido();
    when(cenario.government.submit("<lote/>", "EVT-1")).thenReturn(new ResultadoEnvio("2.202610.9", "1", "<ok/>", 201));
    var mensagem = cenario.job.reenviarSolicitado(1L, 8L, 4L);
    assertEquals(EventStatus.PROCESSING, cenario.batch.getStatus());
    assertEquals("2.202610.9", cenario.batch.getProtocol());
    assertTrue(mensagem.contains("mesmo identificador"));
    verify(cenario.government).submit("<lote/>", "EVT-1");
  }

  @Test
  void botaoComReceitaForaEntraNaFilaSemPost() {
    var cenario = cenario(true, false);
    cenario.batch.resultadoDesconhecido();
    cenario.evento.resultadoDesconhecido();
    var mensagem = cenario.job.reenviarSolicitado(1L, 8L, 4L);
    assertEquals(EventStatus.AGUARDANDO_ENVIO, cenario.batch.getStatus());
    assertEquals(EventStatus.AGUARDANDO_ENVIO, cenario.evento.getStatus());
    assertTrue(mensagem.contains("aguardando"));
    verify(cenario.government, never()).submit(any(), any());
  }

  @Test
  void botaoRecusaLoteQueJaTemProtocolo() {
    var cenario = cenario(true, true);
    cenario.batch.received("2.202610.1");
    var erro = assertThrows(ResponseStatusException.class, () -> cenario.job.reenviarSolicitado(1L, 8L, 4L));
    assertEquals(409, erro.getStatusCode().value());
    verify(cenario.government, never()).submit(any(), any());
  }

  private static Cenario cenario(boolean adaptadorReal, boolean tokenLiberado) {
    var batch = new DereBatchEntity("<lote/>");
    batch.persistId(4L);
    batch.aguardandoEnvio();
    var evento = new DereEventEntity("D-1001", "EVT-1", "restricted-production", "nota_2026_001", null);
    evento.persistId(3L);
    evento.ownedBy(1L, 8L);
    evento.source("1001.xml", null, false);
    evento.unsigned("<evt/>");
    evento.attachBatch(4L);
    evento.aguardandoEnvio();
    var batches = mock(DereBatchRepository.class);
    var events = mock(DereEventRepository.class);
    var attempts = mock(DereTentativaTransmissaoRepository.class);
    var integracao = mock(ConfiguracaoIntegracaoService.class);
    var situacao = mock(SituacaoReceitaService.class);
    var government = mock(TransmissaoGovernoPort.class);
    var importacao = mock(D1001ImportService.class);
    when(integracao.current()).thenReturn(new ConfiguracaoIntegracao(
        adaptadorReal, !adaptadorReal, "https://api.receitafederal.gov.br/prr-dere", "https://api.receitafederal.gov.br/token",
        "id", "segredo", true, "restricted-production", "nota_2026_001", 50));
    when(situacao.atual()).thenReturn(tokenLiberado ? ClassificacaoSituacaoReceita.disponivel() : ClassificacaoSituacaoReceita.falha("erro ao gerar token"));
    when(batches.findTop20ByStatusAndNextQueryAtLessThanEqualOrderByNextQueryAtAsc(eq(EventStatus.AGUARDANDO_ENVIO), any()))
        .thenAnswer(inv -> {
          Instant agora = inv.getArgument(1);
          if (batch.getStatus() != EventStatus.AGUARDANDO_ENVIO) return List.of();
          if (batch.getNextQueryAt() != null && batch.getNextQueryAt().isAfter(agora)) return List.of();
          return List.of(batch);
        });
    when(batches.findById(4L)).thenReturn(Optional.of(batch));
    when(batches.save(any())).thenAnswer(inv -> inv.getArgument(0));
    when(batches.saveAndFlush(any())).thenAnswer(inv -> inv.getArgument(0));
    when(events.findByBatchId(4L)).thenReturn(List.of(evento));
    when(events.save(any())).thenAnswer(inv -> inv.getArgument(0));
    when(attempts.findTopByEventIdAndOperationOrderByAttemptDesc(any(), any())).thenReturn(Optional.empty());
    when(attempts.save(any())).thenAnswer(inv -> inv.getArgument(0));
    var job = new ReenvioLoteNaoTransmitido(batches, events, attempts, integracao, situacao, government,
        new ArmazenamentoArtefatoTransmissao(), importacao, mock(br.gov.dere.application.access.AccessService.class), TransacaoIsolada.direta());
    return new Cenario(job, batch, evento, government, situacao, importacao);
  }

  private record Cenario(
      ReenvioLoteNaoTransmitido job,
      DereBatchEntity batch,
      DereEventEntity evento,
      TransmissaoGovernoPort government,
      SituacaoReceitaService situacao,
      D1001ImportService importacao) {}
}
