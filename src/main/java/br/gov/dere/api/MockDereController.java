package br.gov.dere.api;
import br.gov.dere.application.mock.LocalDereFlowService; import br.gov.dere.application.access.AccessService; import br.gov.dere.domain.contributor.D1001; import java.time.LocalDate; import java.util.List; import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/mock/dere") public class MockDereController { private final LocalDereFlowService flow; private final AccessService access; public MockDereController(LocalDereFlowService f,AccessService a){flow=f;access=a;}
 @PostMapping("/d1001") public LocalDereFlowService.FlowResult submit(@RequestBody Request r,@RequestHeader("X-User-Id") Long userId,@RequestHeader("X-Entity-Id") Long entityId){access.assertAccess(userId,entityId);return flow.submit(new D1001(r.id(),1,2,"dere-poc/0.1",access.entity(entityId).getCnpjRoot(),r.validFrom(),null,9,List.of(),0),userId,entityId);}
 @PostMapping("/batches/{id}/query") public LocalDereFlowService.FlowResult query(@PathVariable Long id){return flow.query(id);}
 public record Request(String id,String cnpjRoot,LocalDate validFrom){}
}
