package br.gov.dere.integration.receita;

import br.gov.dere.application.settings.IntegrationSettingsService;
import java.net.http.HttpClient;
import java.time.Duration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration
public class ReceitaIntegraConfiguration {
  @Bean
  RestClient.Builder restClientBuilder(
      @Value("${dere.transmission.http-connect-timeout-ms:5000}") long connectTimeoutMs,
      @Value("${dere.transmission.http-read-timeout-ms:30000}") long readTimeoutMs) {
    var httpClient = HttpClient.newBuilder()
        .connectTimeout(Duration.ofMillis(Math.max(100, connectTimeoutMs)))
        .build();
    var factory = new JdkClientHttpRequestFactory(httpClient);
    factory.setReadTimeout(Duration.ofMillis(Math.max(100, readTimeoutMs)));
    return RestClient.builder().requestFactory(factory);
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
