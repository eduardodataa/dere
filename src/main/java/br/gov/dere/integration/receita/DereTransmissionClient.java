package br.gov.dere.integration.receita;

import br.gov.dere.application.settings.IntegrationSettingsService;
import java.nio.charset.StandardCharsets;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

public class DereTransmissionClient {
  private final RestClient client;
  private final ReceitaIntegraAuthClient auth;
  private final IntegrationSettingsService settings;

  public DereTransmissionClient(RestClient.Builder builder, ReceitaIntegraAuthClient auth, IntegrationSettingsService settings) {
    this.client = builder.build();
    this.auth = auth;
    this.settings = settings;
  }

  public GovernmentHttpResponse transmit(String batchXml, String idempotencyKey) {
    var base = settings.current().baseUrl();
    var request = client.post()
        .uri(base + "/v1/recepcao/lotes")
        .contentType(MediaType.APPLICATION_XML)
        .header("Authorization", "Bearer " + auth.token());
    if (idempotencyKey != null && !idempotencyKey.isBlank()) {
      request.header("Idempotency-Key", idempotencyKey);
    }
    return request.body(batchXml).exchange((req, response) -> new GovernmentHttpResponse(response.getStatusCode().value(), read(response)));
  }

  public GovernmentHttpResponse query(String protocol) {
    var base = settings.current().baseUrl();
    return client.get()
        .uri(base + "/v1/consulta/lotes/" + protocol)
        .header("Authorization", "Bearer " + auth.token())
        .exchange((request, response) -> new GovernmentHttpResponse(response.getStatusCode().value(), read(response)));
  }

  public void invalidateToken() {
    auth.invalidate();
  }

  private static String read(org.springframework.http.client.ClientHttpResponse response) throws java.io.IOException {
    return response.getBody() == null ? "" : new String(response.getBody().readAllBytes(), StandardCharsets.UTF_8);
  }
}
