package br.gov.dere.integration.receita;

import br.gov.dere.application.configuracao.ConfiguracaoIntegracao;
import br.gov.dere.application.configuracao.ConfiguracaoIntegracaoService;
import br.gov.dere.application.credencial.ContextoCredencialEntidade;
import br.gov.dere.application.credencial.CredenciaisEntidadeService;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

public class ReceitaIntegraAuthClient {
  private final RestClient client;
  private final ConfiguracaoIntegracaoService settings;
  private final CredenciaisEntidadeService credenciais;
  private final ConcurrentHashMap<String, Token> cached = new ConcurrentHashMap<>();

  public ReceitaIntegraAuthClient(
      RestClient.Builder builder,
      ConfiguracaoIntegracaoService settings,
      CredenciaisEntidadeService credenciais) {
    this.client = builder.build();
    this.settings = settings;
    this.credenciais = credenciais;
  }

  public synchronized String token() {
    var atual = settings.current();
    var acesso = acesso(atual);
    exigirCredencial(acesso.clientId(), acesso.clientSecret());
    var chave = chave(atual.tokenUrl(), acesso.cacheKey());
    var vigente = cached.get(chave);
    if (vigente != null && vigente.expiresAt().isAfter(Instant.now().plusSeconds(30))) {
      return vigente.value();
    }
    return guardar(atual.tokenUrl(), acesso.clientId(), acesso.clientSecret(), chave);
  }

  public synchronized String renovar() {
    var atual = settings.current();
    var acesso = acesso(atual);
    exigirCredencial(acesso.clientId(), acesso.clientSecret());
    return guardar(atual.tokenUrl(), acesso.clientId(), acesso.clientSecret(), chave(atual.tokenUrl(), acesso.cacheKey()));
  }

  public synchronized void invalidate() {
    cached.clear();
  }

  public synchronized void invalidate(Long entityId) {
    if (entityId == null) {
      invalidate();
      return;
    }
    cached.keySet().removeIf(chave -> chave.contains("|" + entityId + "|"));
  }

  private String guardar(String tokenUrl, String clientId, String clientSecret, String chave) {
    var basic = Base64.getEncoder().encodeToString((clientId + ":" + clientSecret).getBytes(StandardCharsets.UTF_8));
    var resposta = client.post()
        .uri(tokenUrl)
        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        .header("Authorization", "Basic " + basic)
        .body("grant_type=client_credentials")
        .retrieve()
        .body(TokenResponse.class);
    if (resposta == null || resposta.access_token() == null) {
      throw new IllegalStateException("Receita Integra não retornou access_token");
    }
    var token = new Token(resposta.access_token(), Instant.now().plusSeconds(resposta.expires_in()));
    cached.put(chave, token);
    return token.value();
  }

  private Acesso acesso(ConfiguracaoIntegracao atual) {
    var entityId = ContextoCredencialEntidade.atual();
    if (entityId != null) {
      var cred = credenciais.carregar(entityId);
      return new Acesso(cred.clientId(), cred.clientSecret(), entityId + "|" + texto(cred.clientId()));
    }
    return new Acesso(atual.clientId(), atual.clientSecret(), "global|" + texto(atual.clientId()));
  }

  private static void exigirCredencial(String clientId, String clientSecret) {
    if (clientId == null || clientId.isBlank() || clientSecret == null || clientSecret.isBlank()) {
      throw new IllegalStateException("Credenciais Receita Integra não configuradas");
    }
  }

  private static String chave(String tokenUrl, String cacheKey) {
    return tokenUrl + "|" + cacheKey;
  }

  private static String texto(String valor) {
    return valor == null ? "" : valor;
  }

  public record Token(String value, Instant expiresAt) {}

  private record Acesso(String clientId, String clientSecret, String cacheKey) {}

  private record TokenResponse(String access_token, long expires_in, String token_type) {}
}
