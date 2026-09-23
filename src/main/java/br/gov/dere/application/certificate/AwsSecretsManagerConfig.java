package br.gov.dere.application.certificate;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;

@Configuration
@ConditionalOnProperty(name = "dere.certificate.password-store", havingValue = "secrets-manager")
public class AwsSecretsManagerConfig {
  @Bean
  public SecretsManagerClient secretsManagerClient() {
    return SecretsManagerClient.create();
  }
}
