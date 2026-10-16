package br.gov.dere.infrastructure.governo;

import br.gov.dere.application.transmissao.TransmissaoGovernoPort;
import br.gov.dere.application.transmissao.PoliticaStatusTransmissao;
import br.gov.dere.domain.transmissao.ResultadoConsulta;
import br.gov.dere.domain.transmissao.ResultadoEnvio;
import br.gov.dere.domain.transmissao.EstadoTransmissao;
import br.gov.dere.integration.receita.ClienteTransmissaoDere;
import br.gov.dere.integration.receita.RespostaHttpGoverno;
import br.gov.dere.integration.receita.ParserRespostaGoverno;
import br.gov.dere.integration.receita.ExcecaoTransmissaoGoverno;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;

@Component("httpTransmissaoGovernoAdapter")
public class HttpTransmissaoGovernoAdapter implements TransmissaoGovernoPort {
  private final ClienteTransmissaoDere client;
  private final int maxAttempts;
  private final long retryDelayMs;

  @Autowired
  public HttpTransmissaoGovernoAdapter(
      ClienteTransmissaoDere client,
      @Value("${dere.transmission.http-max-attempts:3}") int maxAttempts,
      @Value("${dere.transmission.http-retry-delay-ms:200}") long retryDelayMs) {
    this.client = client;
    this.maxAttempts = Math.max(1, maxAttempts);
    this.retryDelayMs = Math.max(0, retryDelayMs);
  }

  public HttpTransmissaoGovernoAdapter(ClienteTransmissaoDere client) {
    this(client, 3, 200);
  }

  @Override
  public ResultadoEnvio submit(String signedBatchXml, String idempotencyKey) {
    return execute("SUBMIT", () -> submitOnce(signedBatchXml, idempotencyKey));
  }

  @Override
  public ResultadoConsulta query(String protocol) {
    return execute("QUERY", () -> queryOnce(protocol));
  }

  private ResultadoEnvio submitOnce(String signedBatchXml, String idempotencyKey) {
    RespostaHttpGoverno response = client.transmit(signedBatchXml, idempotencyKey);
    if (response.statusCode() == 401) {
      client.invalidateToken();
      throw failure(response);
    }
    if (response.statusCode() != 201 && response.statusCode() != 202) throw failure(response);
    return new ResultadoEnvio(
        ParserRespostaGoverno.protocol(response.body()),
        ParserRespostaGoverno.code(response.body()),
        response.body(),
        response.statusCode());
  }

  private ResultadoConsulta queryOnce(String protocol) {
    RespostaHttpGoverno response = client.query(protocol);
    if (response.statusCode() == 401) {
      client.invalidateToken();
      throw failure(response);
    }
    if (response.statusCode() != 200) throw failure(response);
    String codigoGoverno = ParserRespostaGoverno.code(response.body());
    String status = ParserRespostaGoverno.status(response.body());
    EstadoTransmissao estado = PoliticaStatusTransmissao.deCodigoGoverno(status == null ? codigoGoverno : status);
    return new ResultadoConsulta(
        protocol,
        estado,
        codigoGoverno,
        ParserRespostaGoverno.receipt(response.body()),
        response.body(),
        response.statusCode());
  }

  private <T> T execute(String operacao, Attempt<T> attempt) {
    ExcecaoTransmissaoGoverno last = null;
    for (int tentativa = 1; tentativa <= maxAttempts; tentativa++) {
      try {
        return attempt.run();
      } catch (ExcecaoTransmissaoGoverno ex) {
        last = ex;
        if (!ex.retryable() || tentativa == maxAttempts) throw ex;
        sleep(tentativa);
      } catch (ResourceAccessException ex) {
        last = new ExcecaoTransmissaoGoverno(408, true, operacao + " timeout/indisponível: " + ex.getMessage());
        if (tentativa == maxAttempts) throw last;
        sleep(tentativa);
      }
    }
    throw last == null ? new ExcecaoTransmissaoGoverno(500, true, operacao + " sem resposta") : last;
  }

  private void sleep(int tentativa) {
    if (retryDelayMs <= 0) return;
    try {
      Thread.sleep(retryDelayMs * tentativa);
    } catch (InterruptedException ex) {
      Thread.currentThread().interrupt();
      throw new ExcecaoTransmissaoGoverno(408, true, "Retry interrompido");
    }
  }

  private static ExcecaoTransmissaoGoverno failure(RespostaHttpGoverno response) {
    return new ExcecaoTransmissaoGoverno(response.statusCode(), PoliticaStatusTransmissao.retryable(response.statusCode()), response.body());
  }

  @FunctionalInterface
  private interface Attempt<T> {
    T run();
  }
}
