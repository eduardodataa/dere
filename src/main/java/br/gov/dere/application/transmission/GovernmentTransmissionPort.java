package br.gov.dere.application.transmission;

import br.gov.dere.domain.transmission.QueryResult;
import br.gov.dere.domain.transmission.SubmissionResult;

public interface GovernmentTransmissionPort {
  SubmissionResult submit(String signedBatchXml, String idempotencyKey);
  QueryResult query(String protocol);
}
