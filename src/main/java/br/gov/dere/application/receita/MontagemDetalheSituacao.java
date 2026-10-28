package br.gov.dere.application.receita;

import br.gov.dere.application.configuracao.ConfiguracaoIntegracao;
import br.gov.dere.application.configuracao.PoliticaAmbiente;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Locale;
import org.springframework.web.client.RestClientResponseException;

final class MontagemDetalheSituacao {
  private static final int LIMITE = 400;

  private MontagemDetalheSituacao() {}

  static DetalheSituacaoReceita simulado(ConfiguracaoIntegracao cfg, Instant quando) {
    return new DetalheSituacaoReceita(
        false,
        texto(cfg.tokenUrl()),
        texto(cfg.baseUrl()),
        ambiente(cfg),
        "Nenhuma consulta foi feita. Este ambiente não fala com a Receita Federal.",
        null,
        null,
        quando.toString());
  }

  static DetalheSituacaoReceita consultado(ConfiguracaoIntegracao cfg, Instant quando, Throwable falha) {
    var http = http(falha);
    Integer status = null;
    if (http != null) status = http.getStatusCode().value();
    else if (falha == null) status = 200;
    String resposta = falha == null
        ? "Token entregue. O valor do token não é exibido."
        : resumir(http == null ? mensagem(falha) : corpo(http));
    return new DetalheSituacaoReceita(
        true,
        texto(cfg.tokenUrl()),
        texto(cfg.baseUrl()),
        ambiente(cfg),
        "POST grant_type=client_credentials. A verificação pediu somente o token.",
        status,
        resposta,
        quando.toString());
  }

  static String resumir(String texto) {
    if (texto == null || texto.isBlank()) return null;
    var limpo = texto.replaceAll("\\s+", " ").trim();
    var base = limpo.toLowerCase(Locale.ROOT);
    if (base.contains("client_secret") || base.contains("\"access_token\"") || base.contains("bearer ")) {
      return "Resposta omitida porque pode conter credencial.";
    }
    if (limpo.length() > LIMITE) return limpo.substring(0, LIMITE) + "…";
    return limpo;
  }

  private static String ambiente(ConfiguracaoIntegracao cfg) {
    if (PoliticaAmbiente.URL_PRE_PRODUCAO.equals(cfg.baseUrl())) return "Pré-produção";
    if (PoliticaAmbiente.URL_PRODUCAO.equals(cfg.baseUrl())) return "Produção";
    if (cfg.environment() == null || cfg.environment().isBlank()) return "Não informado";
    return cfg.environment();
  }

  private static RestClientResponseException http(Throwable falha) {
    for (Throwable atual = falha; atual != null; atual = atual.getCause()) {
      if (atual instanceof RestClientResponseException resposta) return resposta;
    }
    return null;
  }

  private static String corpo(RestClientResponseException http) {
    var bruto = http.getResponseBodyAsString(StandardCharsets.UTF_8);
    if (bruto == null || bruto.isBlank()) return mensagem(http);
    return bruto;
  }

  private static String mensagem(Throwable falha) {
    var texto = new StringBuilder();
    for (Throwable atual = falha; atual != null; atual = atual.getCause()) {
      if (atual.getMessage() != null && !atual.getMessage().isBlank()) {
        if (!texto.isEmpty()) texto.append(' ');
        texto.append(atual.getMessage());
      }
    }
    return texto.toString();
  }

  private static String texto(String valor) {
    if (valor == null || valor.isBlank()) return null;
    return valor.trim();
  }
}
