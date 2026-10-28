package br.gov.dere.application.receita;

import br.gov.dere.application.configuracao.ConfiguracaoIntegracaoService;
import br.gov.dere.integration.receita.ReceitaIntegraAuthClient;
import java.time.Duration;
import java.time.Instant;
import org.springframework.stereotype.Service;

@Service
public class SituacaoReceitaService {
  private final ConfiguracaoIntegracaoService settings;
  private final ReceitaIntegraAuthClient auth;
  private SituacaoReceita cache;
  private Instant expira = Instant.EPOCH;
  private boolean confirmada;

  public SituacaoReceitaService(ConfiguracaoIntegracaoService settings, ReceitaIntegraAuthClient auth) {
    this.settings = settings;
    this.auth = auth;
  }

  public synchronized SituacaoReceita atual() {
    var cfg = settings.current();
    if (!cfg.realAdapterEnabled()) {
      return ClassificacaoSituacaoReceita.simulado().comDetalhe(MontagemDetalheSituacao.simulado(cfg, Instant.now()));
    }
    var agora = Instant.now();
    if (cache != null && confirmada) return cache.comSegundos(0);
    if (cache != null && expira.isAfter(agora)) {
      var restante = (int) Math.max(1, Duration.between(agora, expira).toSeconds());
      return cache.comSegundos(restante);
    }
    SituacaoReceita situacao;
    Throwable falha = null;
    try {
      auth.renovar();
      situacao = ClassificacaoSituacaoReceita.disponivel();
    } catch (RuntimeException ex) {
      falha = ex;
      situacao = ClassificacaoSituacaoReceita.falha(texto(ex));
    }
    situacao = situacao.comDetalhe(MontagemDetalheSituacao.consultado(cfg, agora, falha));
    cache = situacao;
    confirmada = situacao.envioLiberado() && situacao.segundosProximaConsulta() <= 0;
    expira = confirmada ? agora : agora.plusSeconds(Math.max(1, situacao.segundosProximaConsulta()));
    return confirmada ? situacao.comSegundos(0) : situacao;
  }

  public synchronized void registrarIndisponivel() {
    confirmada = false;
    cache = null;
    expira = Instant.EPOCH;
  }

  private static String texto(Throwable ex) {
    var texto = new StringBuilder();
    for (Throwable atual = ex; atual != null; atual = atual.getCause()) {
      if (atual.getMessage() != null) texto.append(' ').append(atual.getMessage());
    }
    return texto.toString();
  }
}
