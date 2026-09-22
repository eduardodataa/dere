package br.gov.dere.api;

import br.gov.dere.persistence.DereTransmissionAttemptEntity;
import br.gov.dere.persistence.DereTransmissionAttemptRepository;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transmissions")
public class TransmissionController {
  private final DereTransmissionAttemptRepository repository;
  public TransmissionController(DereTransmissionAttemptRepository repository) { this.repository = repository; }
  @GetMapping("/events/{eventId}/attempts") public List<DereTransmissionAttemptEntity> attempts(@PathVariable Long eventId) { return repository.findByEventIdOrderByAttemptAsc(eventId); }
}
