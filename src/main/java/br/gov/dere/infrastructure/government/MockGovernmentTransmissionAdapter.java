package br.gov.dere.infrastructure.government;

import br.gov.dere.application.transmission.GovernmentTransmissionPort;
import br.gov.dere.domain.transmission.QueryResult;
import br.gov.dere.domain.transmission.SubmissionResult;
import br.gov.dere.domain.transmission.TransmissionState;
import java.util.UUID;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("local")
public class MockGovernmentTransmissionAdapter implements GovernmentTransmissionPort {
  @Override public SubmissionResult submit(String signedBatchXml, String idempotencyKey) { return new SubmissionResult("LOCAL-" + UUID.randomUUID(), "ACCEPTED", "<response status=\"received\"/>", 200); }
  @Override public QueryResult query(String protocol) { return new QueryResult(protocol, TransmissionState.ACCEPTED, "ACCEPTED", "LOCAL-RECEIPT-" + protocol, "<response status=\"accepted\"/>", 200); }
}
