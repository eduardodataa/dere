package br.gov.dere.integration.receita;

import br.gov.dere.application.settings.IntegrationSettingsService;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

public class ReceitaIntegraAuthClient {
  private final RestClient client;
  private final IntegrationSettingsService settings;
  private volatile Token cached;
  private volatile String cacheKey;

  public ReceitaIntegraAuthClient(RestClient.Builder builder, IntegrationSettingsService settings) {
    this.client = builder.build();
    this.settings = settings;
  }

  public synchronized String token() {
    var atual = settings.current();
    if (atual.clientId() == null || atual.clientId().isBlank() || atual.clientSecret() == null || atual.clientSecret().isBlank()) {
      throw new IllegalStateException("Credenciais Receita Integra não configuradas");
    }
    var chave = atual.tokenUrl() + "|" + atual.clientId();
    if (cached != null && chave.equals(cacheKey) && cached.expiresAt().isAfter(Instant.now().plusSeconds(30))) {
      return cached.value();
    }
    var basic = Base64.getEncoder().encodeToString((atual.clientId() + ":" + atual.clientSecret()).getBytes(StandardCharsets.UTF_8));
    var resposta = client.post()
        .uri(atual.tokenUrl())
        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        .header("Authorization", "Basic " + basic)
        .body("grant_type=client_credentials")
        .retrieve()
        .body(TokenResponse.class);
    if (resposta == null || resposta.access_token() == null) {
      throw new IllegalStateException("Receita Integra não retornou access_token");
    }
    cached = new Token(resposta.access_token(), Instant.now().plusSeconds(resposta.expires_in()));
    cacheKey = chave;
    return cached.value();
  }

  public synchronized void invalidate() {
    cached = null;
    cacheKey = null;
  }

  public record Token(String value, Instant expiresAt) {}

  private record TokenResponse(String access_token, long expires_in, String token_type) {}
}
