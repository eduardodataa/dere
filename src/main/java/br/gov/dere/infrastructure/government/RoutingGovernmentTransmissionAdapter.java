package br.gov.dere.infrastructure.government;

import br.gov.dere.application.settings.IntegrationSettingsService;
import br.gov.dere.application.transmission.GovernmentTransmissionPort;
import br.gov.dere.domain.transmission.QueryResult;
import br.gov.dere.domain.transmission.SubmissionResult;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
public class RoutingGovernmentTransmissionAdapter implements GovernmentTransmissionPort {
  private final IntegrationSettingsService settings;
  private final GovernmentTransmissionPort mock;
  private final GovernmentTransmissionPort http;

  public RoutingGovernmentTransmissionAdapter(
      IntegrationSettingsService settings,
      @Qualifier("mockGovernmentTransmissionAdapter") GovernmentTransmissionPort mock,
      @Qualifier("httpGovernmentTransmissionAdapter") GovernmentTransmissionPort http) {
    this.settings = settings;
    this.mock = mock;
    this.http = http;
  }

  @Override
  public SubmissionResult submit(String signedBatchXml, String idempotencyKey) {
    return ativo().submit(signedBatchXml, idempotencyKey);
  }

  @Override
  public QueryResult query(String protocol) {
    return ativo().query(protocol);
  }

  private GovernmentTransmissionPort ativo() {
    return settings.current().realAdapterEnabled() ? http : mock;
  }
}
