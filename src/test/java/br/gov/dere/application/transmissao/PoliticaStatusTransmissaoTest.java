package br.gov.dere.application.transmissao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import br.gov.dere.domain.transmissao.EstadoTransmissao;
import org.junit.jupiter.api.Test;

class PoliticaStatusTransmissaoTest {
  @Test void mapsGovernmentStatesToApplicationStates() {
    assertEquals(EstadoTransmissao.ACCEPTED, PoliticaStatusTransmissao.deCodigoGoverno("ACEITO"));
    assertEquals(EstadoTransmissao.ACCEPTED, PoliticaStatusTransmissao.deCodigoGoverno("accepted"));
    assertEquals(EstadoTransmissao.ACCEPTED_WITH_WARNINGS, PoliticaStatusTransmissao.deCodigoGoverno("ACEITO_COM_OCORRENCIAS"));
    assertEquals(EstadoTransmissao.REJECTED, PoliticaStatusTransmissao.deCodigoGoverno("REJEITADO"));
    assertEquals(EstadoTransmissao.PROCESSING, PoliticaStatusTransmissao.deCodigoGoverno(" "));
    assertEquals(EstadoTransmissao.PROCESSING, PoliticaStatusTransmissao.deCodigoGoverno("DESCONHECIDO"));
  }
  @Test void identifiesTransientResponses() {
    assertTrue(PoliticaStatusTransmissao.retryable(408));
    assertTrue(PoliticaStatusTransmissao.retryable(429));
    assertTrue(PoliticaStatusTransmissao.retryable(503));
    assertEquals(false, PoliticaStatusTransmissao.retryable(400));
  }
}
