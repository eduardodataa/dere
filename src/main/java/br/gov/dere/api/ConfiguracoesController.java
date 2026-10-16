package br.gov.dere.api;

import br.gov.dere.application.configuracao.ConfiguracaoIntegracao;
import br.gov.dere.application.configuracao.ConfiguracaoIntegracaoService;
import br.gov.dere.application.configuracao.AtualizacaoConfiguracaoIntegracao;
import br.gov.dere.persistence.DereUserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/settings")
public class ConfiguracoesController {
  private final ConfiguracaoIntegracaoService settings;
  private final DereUserRepository users;

  public ConfiguracoesController(ConfiguracaoIntegracaoService settings, DereUserRepository users) {
    this.settings = settings;
    this.users = users;
  }

  @GetMapping("/integration")
  public ConfiguracaoIntegracao get(@RequestHeader("X-User-Id") Long actorId) {
    assertMaster(actorId);
    return settings.publicView();
  }

  @PutMapping("/integration")
  public ConfiguracaoIntegracao put(@RequestHeader("X-User-Id") Long actorId, @RequestBody AtualizacaoConfiguracaoIntegracao update) throws Exception {
    assertMaster(actorId);
    return settings.save(update);
  }

  private void assertMaster(Long actorId) {
    if (actorId == null || !users.findById(actorId).orElseThrow().isMaster()) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Apenas usuário master pode alterar a integração");
    }
  }
}
