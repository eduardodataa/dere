package br.gov.dere.infrastructure.government;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import br.gov.dere.application.settings.IntegrationSettings;
import br.gov.dere.application.settings.IntegrationSettingsService;
import br.gov.dere.domain.transmission.TransmissionState;
import br.gov.dere.integration.receita.DereTransmissionClient;
import br.gov.dere.integration.receita.GovernmentTransmissionException;
import br.gov.dere.integration.receita.ReceitaIntegraAuthClient;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

class GovernmentHttpIntegrationTest {
  private HttpServer server;
  private String baseUrl;
  private String tokenUrl;
  private IntegrationSettingsService settings;
  private HttpGovernmentTransmissionAdapter adapter;
  private final AtomicInteger transmitHits = new AtomicInteger();
  private final AtomicInteger queryHits = new AtomicInteger();
  private final AtomicInteger tokenHits = new AtomicInteger();
  private volatile int transmitStatus = 201;
  private volatile String transmitBody = "<r><protocolo>P-IT-1</protocolo><codigo>OK</codigo></r>";
  private volatile int queryStatus = 200;
  private volatile String queryBody = "<r><status>ACEITO</status><codigo>OK</codigo><recibo>REC-IT-1</recibo></r>";
  private volatile long transmitDelayMs;
  private volatile int[] transmitStatusSequence = new int[0];
  private volatile int[] queryStatusSequence = new int[0];

  @BeforeEach
  void startServer() throws IOException {
    server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
    server.createContext("/token", this::handleToken);
    server.createContext("/dere/v1/recepcao/lotes", this::handleTransmit);
    server.createContext("/dere/v1/consulta/lotes", this::handleQuery);
    server.start();
    var port = server.getAddress().getPort();
    baseUrl = "http://127.0.0.1:" + port + "/dere";
    tokenUrl = "http://127.0.0.1:" + port + "/token";
    settings = mock(IntegrationSettingsService.class);
    when(settings.current()).thenReturn(new IntegrationSettings(
        true, false, baseUrl, tokenUrl, "client-it", "secret-it", true, "restricted-production", "nota_2026_001"));
    adapter = newAdapter(800);
  }

  @AfterEach
  void stopServer() {
    if (server != null) server.stop(0);
  }

  @Test
  void submitAceita201ComProtocolo() {
    var result = adapter.submit("<lote/>", "idem-1");
    assertEquals("P-IT-1", result.protocol());
    assertEquals(201, result.httpStatus());
    assertEquals(1, transmitHits.get());
    assertEquals(1, tokenHits.get());
  }

  @Test
  void submitAceita202() {
    transmitStatus = 202;
    var result = adapter.submit("<lote/>", "idem-2");
    assertEquals("P-IT-1", result.protocol());
    assertEquals(202, result.httpStatus());
  }

  @Test
  void submit401InvalidaTokenENaoFazRetry() {
    transmitStatus = 401;
    transmitBody = "unauthorized";
    var ex = assertThrows(GovernmentTransmissionException.class, () -> adapter.submit("<lote/>", "idem-401"));
    assertEquals(401, ex.statusCode());
    assertFalse(ex.retryable());
    assertEquals(1, transmitHits.get());
  }

  @Test
  void submit403NaoEhRetryable() {
    transmitStatus = 403;
    var ex = assertThrows(GovernmentTransmissionException.class, () -> adapter.submit("<lote/>", "idem-403"));
    assertEquals(403, ex.statusCode());
    assertFalse(ex.retryable());
    assertEquals(1, transmitHits.get());
  }

  @Test
  void submit404NaoEhRetryable() {
    transmitStatus = 404;
    var ex = assertThrows(GovernmentTransmissionException.class, () -> adapter.submit("<lote/>", "idem-404"));
    assertEquals(404, ex.statusCode());
    assertFalse(ex.retryable());
  }

  @Test
  void submit429FazRetryAteEsgotar() {
    transmitStatus = 429;
    transmitBody = "slow down";
    var ex = assertThrows(GovernmentTransmissionException.class, () -> adapter.submit("<lote/>", "idem-429"));
    assertEquals(429, ex.statusCode());
    assertTrue(ex.retryable());
    assertEquals(3, transmitHits.get());
  }

  @Test
  void submit5xxRecuperaNoRetry() {
    transmitStatusSequence = new int[] {503, 502, 201};
    var result = adapter.submit("<lote/>", "idem-5xx");
    assertEquals("P-IT-1", result.protocol());
    assertEquals(3, transmitHits.get());
  }

  @Test
  void submit504EsgotaRetry() {
    transmitStatus = 504;
    var ex = assertThrows(GovernmentTransmissionException.class, () -> adapter.submit("<lote/>", "idem-504"));
    assertEquals(504, ex.statusCode());
    assertTrue(ex.retryable());
    assertEquals(3, transmitHits.get());
  }

  @Test
  void submitTimeoutEhRetryableEEsgotaTentativas() {
    adapter = newAdapter(150);
    transmitDelayMs = 800;
    var ex = assertThrows(GovernmentTransmissionException.class, () -> adapter.submit("<lote/>", "idem-timeout"));
    assertEquals(408, ex.statusCode());
    assertTrue(ex.retryable());
    assertTrue(transmitHits.get() >= 1);
  }

  @Test
  void queryAceitoComRecibo() {
    var result = adapter.query("P-IT-1");
    assertEquals(TransmissionState.ACCEPTED, result.state());
    assertEquals("REC-IT-1", result.receipt());
    assertEquals(200, result.httpStatus());
  }

  @Test
  void query429FazRetry() {
    queryStatus = 429;
    var ex = assertThrows(GovernmentTransmissionException.class, () -> adapter.query("P-IT-1"));
    assertEquals(429, ex.statusCode());
    assertTrue(ex.retryable());
    assertEquals(3, queryHits.get());
  }

  @Test
  void falhaPosEnvioMantemProtocoloEConsultaFalha() {
    var envio = adapter.submit("<lote/>", "idem-pos");
    assertEquals("P-IT-1", envio.protocol());
    queryStatusSequence = new int[] {500, 503, 502};
    var ex = assertThrows(GovernmentTransmissionException.class, () -> adapter.query(envio.protocol()));
    assertTrue(ex.retryable());
    assertTrue(ex.statusCode() >= 500);
    assertEquals(3, queryHits.get());
  }

  @Test
  void queryRecuperaAposFalhaTemporariaPosEnvio() {
    var envio = adapter.submit("<lote/>", "idem-pos-ok");
    queryStatusSequence = new int[] {503, 200};
    var consulta = adapter.query(envio.protocol());
    assertEquals(TransmissionState.ACCEPTED, consulta.state());
    assertEquals("REC-IT-1", consulta.receipt());
    assertEquals(2, queryHits.get());
  }

  private HttpGovernmentTransmissionAdapter newAdapter(long readTimeoutMs) {
    var httpClient = java.net.http.HttpClient.newBuilder()
        .connectTimeout(Duration.ofMillis(500))
        .build();
    var factory = new JdkClientHttpRequestFactory(httpClient);
    factory.setReadTimeout(Duration.ofMillis(readTimeoutMs));
    var builder = RestClient.builder().requestFactory(factory);
    var auth = new ReceitaIntegraAuthClient(builder, settings);
    var client = new DereTransmissionClient(builder, auth, settings);
    return new HttpGovernmentTransmissionAdapter(client, 3, 20);
  }

  private void handleToken(HttpExchange exchange) throws IOException {
    tokenHits.incrementAndGet();
    if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
      respond(exchange, 405, "");
      return;
    }
    respond(exchange, 200, "{\"access_token\":\"token-it\",\"expires_in\":3600,\"token_type\":\"Bearer\"}");
  }

  private void handleTransmit(HttpExchange exchange) throws IOException {
    transmitHits.incrementAndGet();
    if (transmitDelayMs > 0) {
      try {
        Thread.sleep(transmitDelayMs);
      } catch (InterruptedException ex) {
        Thread.currentThread().interrupt();
      }
    }
    int status = nextStatus(transmitStatusSequence, transmitHits.get(), transmitStatus);
    respond(exchange, status, transmitBody);
  }

  private void handleQuery(HttpExchange exchange) throws IOException {
    queryHits.incrementAndGet();
    int status = nextStatus(queryStatusSequence, queryHits.get(), queryStatus);
    respond(exchange, status, queryBody);
  }

  private static int nextStatus(int[] sequence, int hit, int fallback) {
    if (sequence == null || sequence.length == 0) return fallback;
    int index = Math.min(hit, sequence.length) - 1;
    return sequence[Math.max(0, index)];
  }

  private static void respond(HttpExchange exchange, int status, String body) throws IOException {
    byte[] bytes = body == null ? new byte[0] : body.getBytes(StandardCharsets.UTF_8);
    exchange.getResponseHeaders().add("Content-Type", status == 200 && body != null && body.startsWith("{") ? "application/json" : "application/xml");
    exchange.sendResponseHeaders(status, bytes.length);
    try (OutputStream out = exchange.getResponseBody()) {
      out.write(bytes);
    }
  }
}
