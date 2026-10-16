package br.gov.dere.infrastructure.governo;

import br.gov.dere.application.transmissao.TransmissaoGovernoPort;
import br.gov.dere.domain.transmissao.ResultadoConsulta;
import br.gov.dere.domain.transmissao.ResultadoEnvio;
import br.gov.dere.domain.transmissao.EstadoTransmissao;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component("mockTransmissaoGovernoAdapter")
public class MockTransmissaoGovernoAdapter implements TransmissaoGovernoPort {
  @Override
  public ResultadoEnvio submit(String signedBatchXml, String idempotencyKey) {
    return new ResultadoEnvio("LOCAL-" + UUID.randomUUID(), "ACCEPTED", "<response status=\"received\"/>", 200);
  }

  @Override
  public ResultadoConsulta query(String protocol) {
    return new ResultadoConsulta(protocol, EstadoTransmissao.ACCEPTED, "ACCEPTED", "LOCAL-RECEIPT-" + protocol, "<response status=\"accepted\"/>", 200);
  }
}
