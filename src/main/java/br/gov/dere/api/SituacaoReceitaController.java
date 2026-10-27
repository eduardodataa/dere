package br.gov.dere.api;

import br.gov.dere.application.receita.SituacaoReceita;
import br.gov.dere.application.receita.SituacaoReceitaService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/receita")
public class SituacaoReceitaController {
  private final SituacaoReceitaService situacao;

  public SituacaoReceitaController(SituacaoReceitaService situacao) {
    this.situacao = situacao;
  }

  @GetMapping("/situacao")
  public SituacaoReceita atual() {
    return situacao.atual();
  }
}
