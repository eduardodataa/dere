package br.gov.dere.infrastructure.governo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.gov.dere.domain.transmissao.EstadoTransmissao;
import br.gov.dere.integration.receita.ClienteTransmissaoDere;
import br.gov.dere.integration.receita.RespostaHttpGoverno;
import br.gov.dere.integration.receita.ExcecaoTransmissaoGoverno;
import org.springframework.web.client.ResourceAccessException;
import org.junit.jupiter.api.Test;

class HttpTransmissaoGovernoAdapterTest {
  private final ClienteTransmissaoDere client = mock(ClienteTransmissaoDere.class);
  private final HttpTransmissaoGovernoAdapter adapter = new HttpTransmissaoGovernoAdapter(client);

  @Test
  void submitAccepts201AndExtractsProtocol() {
    when(client.transmit("<lote/>", "k1")).thenReturn(new RespostaHttpGoverno(201, "<r><protocolo>P-1</protocolo><codigo>OK</codigo></r>"));
    var result = adapter.submit("<lote/>", "k1");
    assertEquals("P-1", result.protocol());
    assertEquals("OK", result.codigoGoverno());
    assertEquals(201, result.httpStatus());
  }

  @Test
  void submitInvalidatesTokenOn401() {
    when(client.transmit(any(), any())).thenReturn(new RespostaHttpGoverno(401, "denied"));
    var ex = assertThrows(ExcecaoTransmissaoGoverno.class, () -> adapter.submit("<lote/>", "k1"));
    assertEquals(401, ex.statusCode());
    verify(client).invalidateToken();
  }

  @Test
  void submitMarks5xxAsRetryable() {
    when(client.transmit(any(), any())).thenReturn(new RespostaHttpGoverno(503, "busy"));
    var ex = assertThrows(ExcecaoTransmissaoGoverno.class, () -> adapter.submit("<lote/>", "k1"));
    assertTrue(ex.retryable());
    assertEquals(503, ex.statusCode());
    verify(client, org.mockito.Mockito.times(3)).transmit(any(), any());
  }

  @Test
  void submitRecuperaAposRetryEm503() {
    when(client.transmit(any(), any()))
        .thenReturn(new RespostaHttpGoverno(503, "busy"))
        .thenReturn(new RespostaHttpGoverno(201, "<r><protocolo>P-2</protocolo><codigo>OK</codigo></r>"));
    var result = adapter.submit("<lote/>", "k1");
    assertEquals("P-2", result.protocol());
    verify(client, org.mockito.Mockito.times(2)).transmit(any(), any());
  }

  @Test
  void submitNaoRepeteTimeout() {
    when(client.transmit(any(), any())).thenThrow(new ResourceAccessException("Read timed out"));
    var ex = assertThrows(ExcecaoTransmissaoGoverno.class, () -> adapter.submit("<lote/>", "k1"));
    assertEquals(408, ex.statusCode());
    assertEquals(false, ex.retryable());
    verify(client, org.mockito.Mockito.times(1)).transmit(any(), any());
  }

  @Test
  void queryMapsAcceptedStatusAndReceipt() {
    when(client.query("P-1")).thenReturn(new RespostaHttpGoverno(200, "<r><status>ACEITO</status><codigo>OK</codigo><recibo>REC-9</recibo></r>"));
    var result = adapter.query("P-1");
    assertEquals(EstadoTransmissao.ACCEPTED, result.state());
    assertEquals("REC-9", result.receipt());
    assertEquals(200, result.httpStatus());
  }

  @Test
  void queryRejectsNon200() {
    when(client.query("P-1")).thenReturn(new RespostaHttpGoverno(429, "slow"));
    var ex = assertThrows(ExcecaoTransmissaoGoverno.class, () -> adapter.query("P-1"));
    assertTrue(ex.retryable());
  }
}
