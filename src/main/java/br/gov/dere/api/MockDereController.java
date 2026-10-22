package br.gov.dere.api;

import br.gov.dere.application.access.AccessService;
import br.gov.dere.application.configuracao.ConfiguracaoIntegracaoService;
import br.gov.dere.application.mock.LocalDereFlowService;
import br.gov.dere.domain.contributor.D1001;
import br.gov.dere.persistence.DereBatchRepository;
import br.gov.dere.persistence.DereEventRepository;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/mock/dere")
public class MockDereController {
  private final LocalDereFlowService flow;
  private final AccessService access;
  private final DereBatchRepository batches;
  private final DereEventRepository events;
  private final ConfiguracaoIntegracaoService integration;

  public MockDereController(
      LocalDereFlowService flow,
      AccessService access,
      DereBatchRepository batches,
      DereEventRepository events,
      ConfiguracaoIntegracaoService integration) {
    this.flow = flow;
    this.access = access;
    this.batches = batches;
    this.events = events;
    this.integration = integration;
  }

  @PostMapping("/d1001")
  public LocalDereFlowService.FlowResult submit(
      @RequestBody Request request,
      @RequestHeader("X-User-Id") Long userId,
      @RequestHeader("X-Entity-Id") Long entityId) {
    access.assertAccess(userId, entityId);
    return flow.submit(new D1001(request.id(), 1, 2, "dere-poc/0.1", access.entity(entityId).getCnpjRoot(), request.validFrom(), null, 9, List.of(), 0), userId, entityId);
  }

  @PostMapping("/batches/{id}/query")
  public LocalDereFlowService.FlowResult query(@PathVariable Long id, @RequestHeader("X-User-Id") Long userId) {
    var batch = batches.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Lote não encontrado"));
    if (batch.getEntityId() == null) throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Lote sem vínculo com entidade");
    access.assertAccess(userId, batch.getEntityId());
    var evento = events.findByBatchId(id).stream().max(Comparator.comparing(br.gov.dere.persistence.DereEventEntity::getId)).orElse(null);
    if (integration.current().realAdapterEnabled() || evento == null || !evento.isSimulated()) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, "Consulta simulada não se aplica a lote real.");
    }
    return flow.query(id);
  }

  public record Request(String id, String cnpjRoot, LocalDate validFrom) {}
}
