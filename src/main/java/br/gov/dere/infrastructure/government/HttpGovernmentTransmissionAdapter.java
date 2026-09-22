package br.gov.dere.infrastructure.government;

import br.gov.dere.application.transmission.GovernmentTransmissionPort;
import br.gov.dere.domain.transmission.QueryResult;
import br.gov.dere.domain.transmission.SubmissionResult;
import br.gov.dere.domain.transmission.TransmissionState;
import br.gov.dere.application.transmission.TransmissionStatusPolicy;
import br.gov.dere.integration.receita.DereTransmissionClient;
import br.gov.dere.integration.receita.GovernmentHttpResponse;
import br.gov.dere.integration.receita.GovernmentResponseParser;
import br.gov.dere.integration.receita.GovernmentTransmissionException;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("!local")
public class HttpGovernmentTransmissionAdapter implements GovernmentTransmissionPort {
  private final DereTransmissionClient client;
  public HttpGovernmentTransmissionAdapter(DereTransmissionClient client) { this.client = client; }
  @Override public SubmissionResult submit(String signedBatchXml, String idempotencyKey) {
    GovernmentHttpResponse response=client.transmit(signedBatchXml,idempotencyKey);
    if (response.statusCode()==401) { client.invalidateToken(); throw failure(response); }
    if (response.statusCode()!=201 && response.statusCode()!=202) throw failure(response);
    return new SubmissionResult(GovernmentResponseParser.protocol(response.body()), GovernmentResponseParser.code(response.body()), response.body(), response.statusCode());
  }
  @Override public QueryResult query(String protocol) {
    GovernmentHttpResponse response=client.query(protocol);
    if (response.statusCode()==401) { client.invalidateToken(); throw failure(response); }
    if (response.statusCode()!=200) throw failure(response);
    String governmentCode=GovernmentResponseParser.code(response.body());
    String status=GovernmentResponseParser.status(response.body());
    TransmissionState state=TransmissionStatusPolicy.fromGovernmentCode(status==null?governmentCode:status);
    return new QueryResult(protocol,state,governmentCode,GovernmentResponseParser.receipt(response.body()),response.body(),response.statusCode());
  }
  private static GovernmentTransmissionException failure(GovernmentHttpResponse response){ return new GovernmentTransmissionException(response.statusCode(), TransmissionStatusPolicy.retryable(response.statusCode()), response.body()); }
}
