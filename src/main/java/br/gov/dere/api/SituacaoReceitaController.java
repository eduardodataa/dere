package br.gov.dere.api;

import br.gov.dere.application.access.AccessService;
import br.gov.dere.application.receita.SituacaoReceita;
import br.gov.dere.application.receita.SituacaoReceitaService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/receita")
public class SituacaoReceitaController {
  private final SituacaoReceitaService situacao;
  private final AccessService access;

  public SituacaoReceitaController(SituacaoReceitaService situacao, AccessService access) {
    this.situacao = situacao;
    this.access = access;
  }

  @GetMapping("/situacao")
  public SituacaoReceita atual(
      @RequestHeader(value = "X-User-Id", required = false) Long userId,
      @RequestHeader(value = "X-Entity-Id", required = false) Long entityId) {
    if (userId != null && entityId != null) access.assertAccess(userId, entityId);
    return situacao.atual(entityId);
  }
}
