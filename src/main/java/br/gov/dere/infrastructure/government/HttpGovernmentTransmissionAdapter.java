package br.gov.dere.infrastructure.government;

import br.gov.dere.application.transmission.GovernmentTransmissionPort;
import br.gov.dere.application.transmission.TransmissionStatusPolicy;
import br.gov.dere.domain.transmission.QueryResult;
import br.gov.dere.domain.transmission.SubmissionResult;
import br.gov.dere.domain.transmission.TransmissionState;
import br.gov.dere.integration.receita.DereTransmissionClient;
import br.gov.dere.integration.receita.GovernmentHttpResponse;
import br.gov.dere.integration.receita.GovernmentResponseParser;
import br.gov.dere.integration.receita.GovernmentTransmissionException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;

@Component("httpGovernmentTransmissionAdapter")
public class HttpGovernmentTransmissionAdapter implements GovernmentTransmissionPort {
  private final DereTransmissionClient client;
  private final int maxAttempts;
  private final long retryDelayMs;

  public HttpGovernmentTransmissionAdapter(
      DereTransmissionClient client,
      @Value("${dere.transmission.http-max-attempts:3}") int maxAttempts,
      @Value("${dere.transmission.http-retry-delay-ms:200}") long retryDelayMs) {
    this.client = client;
    this.maxAttempts = Math.max(1, maxAttempts);
    this.retryDelayMs = Math.max(0, retryDelayMs);
  }

  public HttpGovernmentTransmissionAdapter(DereTransmissionClient client) {
    this(client, 3, 200);
  }

  @Override
  public SubmissionResult submit(String signedBatchXml, String idempotencyKey) {
    return execute("SUBMIT", () -> submitOnce(signedBatchXml, idempotencyKey));
  }

  @Override
  public QueryResult query(String protocol) {
    return execute("QUERY", () -> queryOnce(protocol));
  }

  private SubmissionResult submitOnce(String signedBatchXml, String idempotencyKey) {
    GovernmentHttpResponse response = client.transmit(signedBatchXml, idempotencyKey);
    if (response.statusCode() == 401) {
      client.invalidateToken();
      throw failure(response);
    }
    if (response.statusCode() != 201 && response.statusCode() != 202) throw failure(response);
    return new SubmissionResult(
        GovernmentResponseParser.protocol(response.body()),
        GovernmentResponseParser.code(response.body()),
        response.body(),
        response.statusCode());
  }

  private QueryResult queryOnce(String protocol) {
    GovernmentHttpResponse response = client.query(protocol);
    if (response.statusCode() == 401) {
      client.invalidateToken();
      throw failure(response);
    }
    if (response.statusCode() != 200) throw failure(response);
    String governmentCode = GovernmentResponseParser.code(response.body());
    String status = GovernmentResponseParser.status(response.body());
    TransmissionState state = TransmissionStatusPolicy.fromGovernmentCode(status == null ? governmentCode : status);
    return new QueryResult(
        protocol,
        state,
        governmentCode,
        GovernmentResponseParser.receipt(response.body()),
        response.body(),
        response.statusCode());
  }

  private <T> T execute(String operacao, Attempt<T> attempt) {
    GovernmentTransmissionException last = null;
    for (int tentativa = 1; tentativa <= maxAttempts; tentativa++) {
      try {
        return attempt.run();
      } catch (GovernmentTransmissionException ex) {
        last = ex;
        if (!ex.retryable() || tentativa == maxAttempts) throw ex;
        sleep(tentativa);
      } catch (ResourceAccessException ex) {
        last = new GovernmentTransmissionException(408, true, operacao + " timeout/indisponível: " + ex.getMessage());
        if (tentativa == maxAttempts) throw last;
        sleep(tentativa);
      }
    }
    throw last == null ? new GovernmentTransmissionException(500, true, operacao + " sem resposta") : last;
  }

  private void sleep(int tentativa) {
    if (retryDelayMs <= 0) return;
    try {
      Thread.sleep(retryDelayMs * tentativa);
    } catch (InterruptedException ex) {
      Thread.currentThread().interrupt();
      throw new GovernmentTransmissionException(408, true, "Retry interrompido");
    }
  }

  private static GovernmentTransmissionException failure(GovernmentHttpResponse response) {
    return new GovernmentTransmissionException(response.statusCode(), TransmissionStatusPolicy.retryable(response.statusCode()), response.body());
  }

  @FunctionalInterface
  private interface Attempt<T> {
    T run();
  }
}
