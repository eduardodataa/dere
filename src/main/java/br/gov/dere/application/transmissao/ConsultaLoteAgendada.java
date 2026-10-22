package br.gov.dere.application.transmissao;

import br.gov.dere.application.configuracao.ConfiguracaoIntegracaoService;
import br.gov.dere.persistence.DereBatchRepository;
import br.gov.dere.persistence.DereEventRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ConsultaLoteAgendada {
  private static final Logger log = LoggerFactory.getLogger(ConsultaLoteAgendada.class);
  private final PersistenciaConsultaLote persistencia;
  private final TransmissaoGovernoPort government;
  private final ConfiguracaoIntegracaoService integration;
  private final DereEventRepository events;
  private final DereBatchRepository batches;

  public ConsultaLoteAgendada(
      PersistenciaConsultaLote persistencia,
      TransmissaoGovernoPort government,
      ConfiguracaoIntegracaoService integration,
      DereEventRepository events,
      DereBatchRepository batches) {
    this.persistencia = persistencia;
    this.government = government;
    this.integration = integration;
    this.events = events;
    this.batches = batches;
  }

  public void consultar(Long batchId) {
    var reserva = persistencia.reservar(batchId);
    if (reserva == null) return;
    if (reserva.consultaLocal()) {
      persistencia.consultarLocal(batchId);
      return;
    }
    if (reserva.protocolo() == null || reserva.protocolo().isBlank()) {
      throw new IllegalStateException("Lote real sem protocolo");
    }
    persistencia.gravarRetorno(batchId, government.query(reserva.protocolo()));
  }

  public void adiar(Long batchId) {
    persistencia.adiar(batchId);
  }

  public void reconsultarRecibosFalsos() {
    if (!integration.current().realAdapterEnabled()) return;
    for (var evento : events.findBySimulatedFalseAndReceiptNumberStartingWith("REC-")) {
      if (!Recibos.falso(evento.getReceiptNumber()) || evento.getBatchId() == null) continue;
      var lote = batches.findById(evento.getBatchId()).orElse(null);
      if (lote == null || lote.getProtocol() == null || lote.getProtocol().isBlank()) continue;
      try {
        persistencia.gravarRetorno(lote.getId(), government.query(lote.getProtocol()));
      } catch (RuntimeException ex) {
        log.warn("Consulta do recibo provisório do lote {} falhou", lote.getId());
      }
    }
  }
}
