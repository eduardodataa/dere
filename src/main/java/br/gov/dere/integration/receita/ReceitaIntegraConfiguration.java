package br.gov.dere.integration.receita;

import br.gov.dere.application.settings.IntegrationSettingsService;
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
  ReceitaIntegraAuthClient receitaIntegraAuthClient(RestClient.Builder builder, IntegrationSettingsService settings) {
    return new ReceitaIntegraAuthClient(builder, settings);
  }

  @Bean
  DereTransmissionClient dereTransmissionClient(
      RestClient.Builder builder, ReceitaIntegraAuthClient auth, IntegrationSettingsService settings) {
    return new DereTransmissionClient(builder, auth, settings);
  }
}
