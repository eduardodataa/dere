package br.gov.dere.application.transmission;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.gov.dere.application.mock.LocalDereFlowService;
import br.gov.dere.domain.EventStatus;
import br.gov.dere.persistence.DereBatchEntity;
import br.gov.dere.persistence.DereBatchRepository;
import java.util.List;
import org.junit.jupiter.api.Test;

class TransmissionPollingServiceTest {
  @Test
  void claimsDueBatchAndQueriesLocalFlow() {
    var batches = mock(DereBatchRepository.class);
    var flow = mock(LocalDereFlowService.class);
    var batch = dueBatch();
    when(batches.findTop100ByStatusAndNextQueryAtLessThanEqualOrderByNextQueryAtAsc(eq(EventStatus.PROCESSING), any()))
        .thenReturn(List.of(batch));
    new TransmissionPollingService(batches, flow).pollDueBatches();
    verify(flow).query(batch.getId());
    verify(batches).saveAndFlush(batch);
  }

  @Test
  void schedulesBackoffWhenQueryFails() {
    var batches = mock(DereBatchRepository.class);
    var flow = mock(LocalDereFlowService.class);
    var batch = dueBatch();
    when(batches.findTop100ByStatusAndNextQueryAtLessThanEqualOrderByNextQueryAtAsc(eq(EventStatus.PROCESSING), any()))
        .thenReturn(List.of(batch));
    when(flow.query(batch.getId())).thenThrow(new IllegalStateException("falha temporária"));
    new TransmissionPollingService(batches, flow).pollDueBatches();
    verify(batches).save(batch);
  }

  private static DereBatchEntity dueBatch() {
    var batch = new DereBatchEntity("<lote/>");
    batch.received("MOCK-1");
    batch.queryFailed(0);
    return batch;
  }
}
