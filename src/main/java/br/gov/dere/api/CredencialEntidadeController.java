package br.gov.dere.api;

import br.gov.dere.application.access.AccessService;
import br.gov.dere.application.credencial.CredenciaisEntidadeService;
import br.gov.dere.application.credencial.CredenciaisEntidadeService.CredencialPublica;
import br.gov.dere.integration.receita.ReceitaIntegraAuthClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/entities/{entityId}/credenciais")
public class CredencialEntidadeController {
  private final AccessService access;
  private final CredenciaisEntidadeService credenciais;
  private final ReceitaIntegraAuthClient auth;

  public CredencialEntidadeController(
      AccessService access,
      CredenciaisEntidadeService credenciais,
      ReceitaIntegraAuthClient auth) {
    this.access = access;
    this.credenciais = credenciais;
    this.auth = auth;
  }

  @GetMapping
  public CredencialPublica atual(
      @PathVariable Long entityId,
      @RequestHeader("X-User-Id") Long userId) {
    access.assertAccess(userId, entityId);
    return credenciais.publica(credenciais.carregar(entityId));
  }

  @PutMapping
  public CredencialPublica salvar(
      @PathVariable Long entityId,
      @RequestHeader("X-User-Id") Long userId,
      @RequestBody AtualizacaoCredencial request) {
    access.assertAccess(userId, entityId);
    var salva = credenciais.salvar(
        entityId,
        request.clientId(),
        request.clientSecret(),
        request.validadeCredencial());
    auth.invalidate(entityId);
    return credenciais.publica(salva);
  }

  public record AtualizacaoCredencial(String clientId, String clientSecret, String validadeCredencial) {}
}
