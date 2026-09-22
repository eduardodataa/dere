package br.gov.dere.application.transmission;

import br.gov.dere.application.mock.LocalDereFlowService;
import br.gov.dere.domain.EventStatus;
import br.gov.dere.persistence.DereBatchRepository;
import java.time.Instant;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransmissionPollingService {
  private final DereBatchRepository batches; private final LocalDereFlowService localFlow;
  public TransmissionPollingService(DereBatchRepository batches, LocalDereFlowService localFlow) { this.batches = batches; this.localFlow = localFlow; }
  @Scheduled(fixedDelayString = "${dere.transmission.poll-delay-ms:30000}") @Transactional
  public void pollDueBatches() { var now = Instant.now(); for (var batch : batches.findTop100ByStatusAndNextQueryAtLessThanEqualOrderByNextQueryAtAsc(EventStatus.PROCESSING, now)) { if (!batch.claimQuery(now, 60)) continue; batches.saveAndFlush(batch); try { localFlow.query(batch.getId()); } catch (RuntimeException ex) { batch.queryFailed(Math.min(900, 30L * Math.max(1, batch.getQueryAttempts()))); batches.save(batch); } } }
}
