package br.gov.dere.api;

import br.gov.dere.application.transmission.SimulateSendRequest;
import br.gov.dere.application.transmission.SimulateSendResult;
import br.gov.dere.application.transmission.SimulatedTransmissionService;
import br.gov.dere.application.transmission.TransmissionView;
import br.gov.dere.persistence.DereTransmissionAttemptEntity;
import br.gov.dere.persistence.DereTransmissionAttemptRepository;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transmissions")
public class TransmissionController {
  private final DereTransmissionAttemptRepository repository;
  private final SimulatedTransmissionService simulated;

  public TransmissionController(DereTransmissionAttemptRepository repository, SimulatedTransmissionService simulated) {
    this.repository = repository;
    this.simulated = simulated;
  }

  @PostMapping("/simulate")
  public SimulateSendResult simulate(
      @RequestBody SimulateSendRequest request,
      @RequestHeader("X-User-Id") Long userId,
      @RequestHeader("X-Entity-Id") Long entityId) {
    return simulated.simulate(userId, entityId, request);
  }

  @PostMapping("/{batchId}/query")
  public TransmissionView query(
      @PathVariable Long batchId,
      @RequestHeader("X-User-Id") Long userId,
      @RequestHeader("X-Entity-Id") Long entityId) {
    return simulated.query(userId, entityId, batchId);
  }

  @GetMapping
  public List<TransmissionView> listar(
      @RequestHeader("X-User-Id") Long userId,
      @RequestHeader("X-Entity-Id") Long entityId) {
    return simulated.listar(userId, entityId);
  }

  @GetMapping("/{batchId}")
  public TransmissionView detalhe(
      @PathVariable Long batchId,
      @RequestHeader("X-User-Id") Long userId,
      @RequestHeader("X-Entity-Id") Long entityId) {
    return simulated.detalhe(userId, entityId, batchId);
  }

  @GetMapping("/events/{eventId}/attempts")
  public List<DereTransmissionAttemptEntity> attempts(@PathVariable Long eventId) {
    return repository.findByEventIdOrderByAttemptAsc(eventId);
  }
}
