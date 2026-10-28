package br.gov.dere.integration.receita;

import br.gov.dere.application.configuracao.ConfiguracaoIntegracaoService;
import java.nio.charset.StandardCharsets;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

public class ClienteTransmissaoDere {
  private final RestClient client;
  private final ReceitaIntegraAuthClient auth;
  private final ConfiguracaoIntegracaoService settings;

  public ClienteTransmissaoDere(RestClient.Builder builder, ReceitaIntegraAuthClient auth, ConfiguracaoIntegracaoService settings) {
    this.client = builder.build();
    this.auth = auth;
    this.settings = settings;
  }

  public RespostaHttpGoverno transmit(String batchXml, String idempotencyKey) {
    var acesso = tokenAntesDoPost();
    var base = settings.current().baseUrl();
    var request = client.post()
        .uri(base + "/v1/recepcao/lotes")
        .contentType(MediaType.APPLICATION_XML)
        .header("Authorization", "Bearer " + acesso);
    if (idempotencyKey != null && !idempotencyKey.isBlank()) {
      request.header("Idempotency-Key", idempotencyKey);
    }
    return request.body(batchXml).exchange((req, response) -> new RespostaHttpGoverno(response.getStatusCode().value(), read(response)));
  }

  public RespostaHttpGoverno query(String protocol) {
    var base = settings.current().baseUrl();
    return client.get()
        .uri(base + "/v1/consulta/lotes/" + protocol)
        .header("Authorization", "Bearer " + auth.token())
        .exchange((request, response) -> new RespostaHttpGoverno(response.getStatusCode().value(), read(response)));
  }

  public void invalidateToken() {
    auth.invalidate();
  }

  private String tokenAntesDoPost() {
    try {
      return auth.token();
    } catch (RuntimeException ex) {
      throw new FalhaAntesDaTransmissao(ex);
    }
  }

  private static String read(org.springframework.http.client.ClientHttpResponse response) throws java.io.IOException {
    return response.getBody() == null ? "" : new String(response.getBody().readAllBytes(), StandardCharsets.UTF_8);
  }
}
