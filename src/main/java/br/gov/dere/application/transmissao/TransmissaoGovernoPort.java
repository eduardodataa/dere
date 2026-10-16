package br.gov.dere.application.transmissao;

import br.gov.dere.domain.transmissao.ResultadoConsulta;
import br.gov.dere.domain.transmissao.ResultadoEnvio;

public interface TransmissaoGovernoPort {
  ResultadoEnvio submit(String signedBatchXml, String idempotencyKey);
  ResultadoConsulta query(String protocol);
}
