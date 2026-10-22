package br.gov.dere.application.transmissao;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.gov.dere.domain.EventStatus;
import br.gov.dere.domain.transmissao.EstadoTransmissao;
import br.gov.dere.domain.transmissao.ResultadoConsulta;
import br.gov.dere.persistence.DereBatchEntity;
import br.gov.dere.persistence.DereBatchRepository;
import java.util.List;
import org.junit.jupiter.api.Test;

class TransmissaoPollingServiceTest {
  @Test
  void consultaCadaLoteForaDaTransacaoDoLaco() {
    var batches = mock(DereBatchRepository.class);
    var consulta = mock(ConsultaLoteAgendada.class);
    var batch = dueBatch();
    when(batches.findTop100ByStatusAndNextQueryAtLessThanEqualOrderByNextQueryAtAsc(eq(EventStatus.PROCESSING), any()))
        .thenReturn(List.of(batch));
    new TransmissaoPollingService(batches, consulta).pollDueBatches();
    verify(consulta).consultar(batch.getId());
    verify(consulta).reconsultarRecibosFalsos();
    verify(consulta, never()).adiar(batch.getId());
  }

  @Test
  void adiaSomenteOLoteQueFalhou() {
    var batches = mock(DereBatchRepository.class);
    var consulta = mock(ConsultaLoteAgendada.class);
    var batch = dueBatch();
    when(batches.findTop100ByStatusAndNextQueryAtLessThanEqualOrderByNextQueryAtAsc(eq(EventStatus.PROCESSING), any()))
        .thenReturn(List.of(batch));
    doThrow(new IllegalStateException("falha temporária")).when(consulta).consultar(batch.getId());
    new TransmissaoPollingService(batches, consulta).pollDueBatches();
    verify(consulta).adiar(batch.getId());
    verify(consulta).reconsultarRecibosFalsos();
  }

  @Test
  void loteRealConsultaAReceitaELoteSimuladoFicaNoMock() {
    var persistencia = mock(PersistenciaConsultaLote.class);
    var government = mock(TransmissaoGovernoPort.class);
    var consulta = new ConsultaLoteAgendada(persistencia, government, null, null, null);
    var retorno = new ResultadoConsulta("2.202610.1", EstadoTransmissao.PROCESSING, "1", null, "<ok/>", 200);
    when(persistencia.reservar(7L)).thenReturn(new PersistenciaConsultaLote.Reserva(false, "2.202610.1"));
    when(government.query("2.202610.1")).thenReturn(retorno);
    when(persistencia.reservar(8L)).thenReturn(new PersistenciaConsultaLote.Reserva(true, "1.20260101010101.000001"));
    consulta.consultar(7L);
    consulta.consultar(8L);
    verify(government).query("2.202610.1");
    verify(persistencia).gravarRetorno(7L, retorno);
    verify(persistencia).consultarLocal(8L);
    verify(government, never()).query("1.20260101010101.000001");
  }

  private static DereBatchEntity dueBatch() {
    var batch = new DereBatchEntity("<lote/>");
    batch.persistId(4L);
    batch.received("MOCK-1");
    batch.queryFailed(0);
    return batch;
  }
}
