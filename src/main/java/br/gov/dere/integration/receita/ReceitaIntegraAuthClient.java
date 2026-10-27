package br.gov.dere.integration.receita;

import br.gov.dere.application.configuracao.ConfiguracaoIntegracao;
import br.gov.dere.application.configuracao.ConfiguracaoIntegracaoService;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

public class ReceitaIntegraAuthClient {
  private final RestClient client;
  private final ConfiguracaoIntegracaoService settings;
  private volatile Token cached;
  private volatile String cacheKey;

  public ReceitaIntegraAuthClient(RestClient.Builder builder, ConfiguracaoIntegracaoService settings) {
    this.client = builder.build();
    this.settings = settings;
  }

  public synchronized String token() {
    var atual = settings.current();
    exigirCredencial(atual);
    var chave = chave(atual);
    if (cached != null && chave.equals(cacheKey) && cached.expiresAt().isAfter(Instant.now().plusSeconds(30))) {
      return cached.value();
    }
    return guardar(atual, chave);
  }

  public synchronized String renovar() {
    var atual = settings.current();
    exigirCredencial(atual);
    return guardar(atual, chave(atual));
  }

  public synchronized void invalidate() {
    cached = null;
    cacheKey = null;
  }

  private String guardar(ConfiguracaoIntegracao atual, String chave) {
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

  private static void exigirCredencial(ConfiguracaoIntegracao atual) {
    if (atual.clientId() == null || atual.clientId().isBlank() || atual.clientSecret() == null || atual.clientSecret().isBlank()) {
      throw new IllegalStateException("Credenciais Receita Integra não configuradas");
    }
  }

  private static String chave(ConfiguracaoIntegracao atual) {
    return atual.tokenUrl() + "|" + atual.clientId();
  }

  public record Token(String value, Instant expiresAt) {}

  private record TokenResponse(String access_token, long expires_in, String token_type) {}
}
