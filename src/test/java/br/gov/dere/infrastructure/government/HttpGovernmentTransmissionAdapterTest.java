package br.gov.dere.infrastructure.government;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.gov.dere.domain.transmission.TransmissionState;
import br.gov.dere.integration.receita.DereTransmissionClient;
import br.gov.dere.integration.receita.GovernmentHttpResponse;
import br.gov.dere.integration.receita.GovernmentTransmissionException;
import org.junit.jupiter.api.Test;

class HttpGovernmentTransmissionAdapterTest {
  private final DereTransmissionClient client = mock(DereTransmissionClient.class);
  private final HttpGovernmentTransmissionAdapter adapter = new HttpGovernmentTransmissionAdapter(client);

  @Test
  void submitAccepts201AndExtractsProtocol() {
    when(client.transmit("<lote/>", "k1")).thenReturn(new GovernmentHttpResponse(201, "<r><protocolo>P-1</protocolo><codigo>OK</codigo></r>"));
    var result = adapter.submit("<lote/>", "k1");
    assertEquals("P-1", result.protocol());
    assertEquals("OK", result.governmentCode());
    assertEquals(201, result.httpStatus());
  }

  @Test
  void submitInvalidatesTokenOn401() {
    when(client.transmit(any(), any())).thenReturn(new GovernmentHttpResponse(401, "denied"));
    var ex = assertThrows(GovernmentTransmissionException.class, () -> adapter.submit("<lote/>", "k1"));
    assertEquals(401, ex.statusCode());
    verify(client).invalidateToken();
  }

  @Test
  void submitMarks5xxAsRetryable() {
    when(client.transmit(any(), any())).thenReturn(new GovernmentHttpResponse(503, "busy"));
    var ex = assertThrows(GovernmentTransmissionException.class, () -> adapter.submit("<lote/>", "k1"));
    assertTrue(ex.retryable());
    assertEquals(503, ex.statusCode());
  }

  @Test
  void queryMapsAcceptedStatusAndReceipt() {
    when(client.query("P-1")).thenReturn(new GovernmentHttpResponse(200, "<r><status>ACEITO</status><codigo>OK</codigo><recibo>REC-9</recibo></r>"));
    var result = adapter.query("P-1");
    assertEquals(TransmissionState.ACCEPTED, result.state());
    assertEquals("REC-9", result.receipt());
    assertEquals(200, result.httpStatus());
  }

  @Test
  void queryRejectsNon200() {
    when(client.query("P-1")).thenReturn(new GovernmentHttpResponse(429, "slow"));
    var ex = assertThrows(GovernmentTransmissionException.class, () -> adapter.query("P-1"));
    assertTrue(ex.retryable());
  }
}
