package br.gov.dere.application.receita;

import br.gov.dere.application.configuracao.ConfiguracaoIntegracao;
import br.gov.dere.application.configuracao.ConfiguracaoIntegracaoService;
import br.gov.dere.application.configuracao.ValidadeCredencial;
import br.gov.dere.application.credencial.ContextoCredencialEntidade;
import br.gov.dere.application.credencial.CredencialEntidade;
import br.gov.dere.application.credencial.CredenciaisEntidadeService;
import br.gov.dere.integration.receita.ReceitaIntegraAuthClient;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class SituacaoReceitaService {
  private final ConfiguracaoIntegracaoService settings;
  private final CredenciaisEntidadeService credenciais;
  private final ReceitaIntegraAuthClient auth;
  private final Map<Long, CacheEntidade> caches = new HashMap<>();

  public SituacaoReceitaService(
      ConfiguracaoIntegracaoService settings,
      CredenciaisEntidadeService credenciais,
      ReceitaIntegraAuthClient auth) {
    this.settings = settings;
    this.credenciais = credenciais;
    this.auth = auth;
  }

  public synchronized SituacaoReceita atual(Long entityId) {
    var cfg = settings.current();
    var agora = Instant.now();
    if (!cfg.realAdapterEnabled()) {
      return comData(ClassificacaoSituacaoReceita.simulado().comDetalhe(MontagemDetalheSituacao.simulado(cfg, agora)), null);
    }
    if (entityId == null) {
      return comData(ClassificacaoSituacaoReceita.semCredencialEntidade()
          .comDetalhe(MontagemDetalheSituacao.credencialVencida(cfg, agora)), null);
    }
    var cred = credenciais.carregar(entityId);
    if (!cred.completa()) {
      var situacao = comData(ClassificacaoSituacaoReceita.semCredencialEntidade()
          .comDetalhe(MontagemDetalheSituacao.credencialVencida(cfg, agora)), cred);
      caches.put(entityId, new CacheEntidade(situacao, agora.plusSeconds(Math.max(1, situacao.segundosProximaConsulta())), false));
      return situacao;
    }
    if (ValidadeCredencial.vencida(cred.validadeCredencial(), LocalDate.now(ZoneId.of("America/Sao_Paulo")))) {
      var situacao = comData(ClassificacaoSituacaoReceita.vencida(cred.validadeCredencial())
          .comDetalhe(MontagemDetalheSituacao.credencialVencida(cfg, agora)), cred);
      caches.put(entityId, new CacheEntidade(situacao, agora.plusSeconds(Math.max(1, situacao.segundosProximaConsulta())), false));
      return situacao;
    }
    var cache = caches.get(entityId);
    if (cache != null && "VALIDADE".equals(cache.situacao.codigo())) {
      caches.remove(entityId);
      cache = null;
    }
    if (cache != null && cache.confirmada) return comData(cache.situacao, cred).comSegundos(0);
    if (cache != null && cache.expira.isAfter(agora)) {
      var restante = (int) Math.max(1, Duration.between(agora, cache.expira).toSeconds());
      return comData(cache.situacao, cred).comSegundos(restante);
    }
    SituacaoReceita situacao;
    Throwable falha = null;
    try {
      ContextoCredencialEntidade.executar(entityId, auth::renovar);
      situacao = ClassificacaoSituacaoReceita.disponivel();
    } catch (RuntimeException ex) {
      falha = ex;
      situacao = ClassificacaoSituacaoReceita.falha(texto(ex));
    }
    situacao = comData(situacao.comDetalhe(MontagemDetalheSituacao.consultado(cfg, agora, falha)), cred);
    var confirmada = situacao.envioLiberado() && situacao.segundosProximaConsulta() <= 0;
    caches.put(entityId, new CacheEntidade(situacao, confirmada ? agora : agora.plusSeconds(Math.max(1, situacao.segundosProximaConsulta())), confirmada));
    return confirmada ? situacao.comSegundos(0) : situacao;
  }

  public synchronized void registrarIndisponivel(Long entityId) {
    if (entityId == null) {
      caches.clear();
      return;
    }
    caches.remove(entityId);
  }

  private static SituacaoReceita comData(SituacaoReceita situacao, CredencialEntidade cred) {
    return situacao.comValidade(cred == null ? null : cred.validadeCredencial());
  }

  private static String texto(Throwable ex) {
    var texto = new StringBuilder();
    for (Throwable atual = ex; atual != null; atual = atual.getCause()) {
      if (atual.getMessage() != null) texto.append(' ').append(atual.getMessage());
    }
    return texto.toString();
  }

  private record CacheEntidade(SituacaoReceita situacao, Instant expira, boolean confirmada) {}
}
