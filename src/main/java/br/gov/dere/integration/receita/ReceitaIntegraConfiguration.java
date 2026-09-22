package br.gov.dere.integration.receita;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class ReceitaIntegraConfiguration {
  @Bean
  RestClient.Builder restClientBuilder() {
    return RestClient.builder();
  }

  @Bean
  ReceitaIntegraAuthClient receitaIntegraAuthClient(RestClient.Builder builder,
      @Value("${dere.api.token-url}") String tokenUrl,
      @Value("${dere.auth.client-id}") String clientId,
      @Value("${dere.auth.client-secret}") String clientSecret) {
    return new ReceitaIntegraAuthClient(builder, tokenUrl, clientId, clientSecret);
  }

  @Bean
  DereTransmissionClient dereTransmissionClient(RestClient.Builder builder,
      ReceitaIntegraAuthClient auth,
      @Value("${dere.api.base-url}") String baseUrl) {
    return new DereTransmissionClient(builder, auth, baseUrl);
  }
}
