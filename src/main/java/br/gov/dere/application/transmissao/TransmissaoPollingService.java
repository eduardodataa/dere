package br.gov.dere.application.transmissao;

import br.gov.dere.domain.EventStatus;
import br.gov.dere.persistence.DereBatchRepository;
import java.time.Instant;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class TransmissaoPollingService {
  private final DereBatchRepository batches;
  private final ConsultaLoteAgendada consulta;

  public TransmissaoPollingService(DereBatchRepository batches, ConsultaLoteAgendada consulta) {
    this.batches = batches;
    this.consulta = consulta;
  }

  @Scheduled(fixedDelayString = "${dere.transmission.poll-delay-ms:30000}")
  public void pollDueBatches() {
    var agora = Instant.now();
    for (var lote : batches.findTop100ByStatusAndNextQueryAtLessThanEqualOrderByNextQueryAtAsc(EventStatus.PROCESSING, agora)) {
      try {
        consulta.consultar(lote.getId());
      } catch (RuntimeException ex) {
        consulta.adiar(lote.getId());
      }
    }
    consulta.reconsultarRecibosFalsos();
  }
}
