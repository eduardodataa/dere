package br.gov.dere.infrastructure.governo;

import br.gov.dere.application.configuracao.ConfiguracaoIntegracaoService;
import br.gov.dere.application.transmissao.TransmissaoGovernoPort;
import br.gov.dere.domain.transmissao.ResultadoConsulta;
import br.gov.dere.domain.transmissao.ResultadoEnvio;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
public class RoutingTransmissaoGovernoAdapter implements TransmissaoGovernoPort {
  private final ConfiguracaoIntegracaoService settings;
  private final TransmissaoGovernoPort mock;
  private final TransmissaoGovernoPort http;

  public RoutingTransmissaoGovernoAdapter(
      ConfiguracaoIntegracaoService settings,
      @Qualifier("mockTransmissaoGovernoAdapter") TransmissaoGovernoPort mock,
      @Qualifier("httpTransmissaoGovernoAdapter") TransmissaoGovernoPort http) {
    this.settings = settings;
    this.mock = mock;
    this.http = http;
  }

  @Override
  public ResultadoEnvio submit(String signedBatchXml, String idempotencyKey) {
    return ativo().submit(signedBatchXml, idempotencyKey);
  }

  @Override
  public ResultadoConsulta query(String protocol) {
    return ativo().query(protocol);
  }

  private TransmissaoGovernoPort ativo() {
    return settings.current().realAdapterEnabled() ? http : mock;
  }
}
