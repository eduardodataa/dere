package br.gov.dere.api;

import br.gov.dere.application.configuracao.PoliticaAmbiente;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ambiente")
public class AmbienteController {
  private final String implantacao;

  public AmbienteController(@Value("${dere.deployment:local}") String implantacao) {
    this.implantacao = PoliticaAmbiente.normalizar(implantacao);
  }

  @GetMapping
  public AmbienteResposta atual() {
    return new AmbienteResposta(implantacao, PoliticaAmbiente.rotulo(implantacao), PoliticaAmbiente.rodape(implantacao));
  }

  public record AmbienteResposta(String codigo, String rotulo, String rodape) {}
}
