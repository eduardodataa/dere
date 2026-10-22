package br.gov.dere.application.transmissao;

import br.gov.dere.application.configuracao.ConfiguracaoIntegracaoService;
import br.gov.dere.application.mock.LocalDereFlowService;
import br.gov.dere.domain.EventStatus;
import br.gov.dere.domain.transmissao.ResultadoConsulta;
import br.gov.dere.persistence.DereBatchRepository;
import br.gov.dere.persistence.DereEventEntity;
import br.gov.dere.persistence.DereEventRepository;
import br.gov.dere.persistence.DereTentativaTransmissaoEntity;
import br.gov.dere.persistence.DereTentativaTransmissaoRepository;
import java.time.Instant;
import java.util.Comparator;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PersistenciaConsultaLote {
  private final DereBatchRepository batches;
  private final DereEventRepository events;
  private final DereTentativaTransmissaoRepository attempts;
  private final ConfiguracaoIntegracaoService integration;
  private final LocalDereFlowService localFlow;
  private final ArmazenamentoArtefatoTransmissao artefatos;

  public PersistenciaConsultaLote(
      DereBatchRepository batches,
      DereEventRepository events,
      DereTentativaTransmissaoRepository attempts,
      ConfiguracaoIntegracaoService integration,
      LocalDereFlowService localFlow,
      ArmazenamentoArtefatoTransmissao artefatos) {
    this.batches = batches;
    this.events = events;
    this.attempts = attempts;
    this.integration = integration;
    this.localFlow = localFlow;
    this.artefatos = artefatos;
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public Reserva reservar(Long batchId) {
    var batch = batches.findById(batchId).orElse(null);
    if (batch == null || !batch.claimQuery(Instant.now(), 60)) return null;
    batches.saveAndFlush(batch);
    var evento = evento(batchId);
    var simulado = evento == null || evento.isSimulated();
    var local = simulado || !integration.current().realAdapterEnabled();
    return new Reserva(local, batch.getProtocol());
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void consultarLocal(Long batchId) {
    localFlow.query(batchId);
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void gravarRetorno(Long batchId, ResultadoConsulta consulta) {
    var batch = batches.findById(batchId).orElse(null);
    var evento = evento(batchId);
    if (batch == null || evento == null || consulta == null) return;
    var retorno = consulta.responseBody() == null ? "" : consulta.responseBody();
    var recibo = consulta.receipt() == null || consulta.receipt().isBlank() ? null : consulta.receipt();
    var status = switch (consulta.state()) {
      case ACCEPTED, ACCEPTED_WITH_WARNINGS -> EventStatus.ACCEPTED;
      case REJECTED -> EventStatus.REJECTED;
      default -> EventStatus.PROCESSING;
    };
    var xml = artefatos.guardar(evento.getEventIdentifier(), "retorno", retorno);
    if (status == EventStatus.PROCESSING) {
      if (!evento.isSimulated() && Recibos.falso(evento.getReceiptNumber())) evento.limparReciboFalso();
      batch.aindaProcessando(xml);
    } else {
      batch.completed(status, xml);
      evento.processed(status, recibo);
    }
    batches.save(batch);
    events.save(evento);
    registrarTentativa(evento.getId(), batch.getProtocol(), retorno, consulta.httpStatus());
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void adiar(Long batchId) {
    var batch = batches.findById(batchId).orElse(null);
    if (batch == null || batch.getStatus() != EventStatus.PROCESSING) return;
    batch.queryFailed(Math.min(900, 30L * Math.max(1, batch.getQueryAttempts())));
    batches.save(batch);
  }

  private DereEventEntity evento(Long batchId) {
    return events.findByBatchId(batchId).stream().max(Comparator.comparing(DereEventEntity::getId)).orElse(null);
  }

  private void registrarTentativa(Long eventId, String protocolo, String retorno, int httpStatus) {
    var proxima = attempts.findTopByEventIdAndOperationOrderByAttemptDesc(eventId, "QUERY")
        .map(item -> (item.getAttempt() == null ? 0 : item.getAttempt()) + 1)
        .orElse(1);
    var tentativa = new DereTentativaTransmissaoEntity(eventId, "QUERY", proxima, UUID.randomUUID().toString());
    var referencia = protocolo == null ? "" : protocolo;
    tentativa.complete("SUCCESS", httpStatus, "GOV_ACCEPTED", 0, "receita://query/" + referencia, "receita://retorno/" + referencia, null);
    attempts.save(tentativa);
  }

  public record Reserva(boolean consultaLocal, String protocolo) {}
}
