package br.gov.dere.application.receita;

import java.util.Locale;

public final class ClassificacaoSituacaoReceita {
  public static final int INTERVALO_SEGUNDOS = 60;
  static final String AVISO_SISTEMA_FORA =
      "Enquanto o sistema da DeRE estiver fora, a declaração pode ser incluída e validada neste sistema. O lote não será aceito pela Receita Federal.";

  private ClassificacaoSituacaoReceita() {}

  public static SituacaoReceita simulado() {
    return criar(
        "SIMULADO",
        true,
        "Este ambiente não consulta a Receita Federal. O envio fica só na simulação.",
        "Nenhuma consulta externa será feita.",
        0,
        null);
  }

  public static SituacaoReceita disponivel() {
    return criar(
        "DISPONIVEL",
        true,
        "A Receita Federal entregou o token de acesso.",
        "A consulta será repetida. O envio não é repetido sozinho.",
        INTERVALO_SEGUNDOS,
        null);
  }

  public static SituacaoReceita falha(String texto) {
    var base = texto == null ? "" : texto.toLowerCase(Locale.ROOT);
    if (base.contains("erro ao gerar token") || base.contains("access_token")) {
      return criar(
          "TOKEN",
          false,
          "A Receita Federal não entregou o token de acesso.",
          "A consulta será repetida. O envio não será repetido sozinho.",
          INTERVALO_SEGUNDOS,
          AVISO_SISTEMA_FORA);
    }
    if (base.contains("credenciais receita integra")) {
      return criar(
          "CREDENCIAL",
          false,
          "As credenciais da Receita Federal não estão configuradas neste servidor.",
          "O envio real fica bloqueado até a configuração ser corrigida.",
          INTERVALO_SEGUNDOS,
          null);
    }
    return criar(
        "COMUNICACAO",
        false,
        "Não foi possível falar com a Receita Federal a partir deste servidor.",
        "A consulta será repetida. O envio não será repetido sozinho.",
        INTERVALO_SEGUNDOS,
        AVISO_SISTEMA_FORA);
  }

  private static SituacaoReceita criar(String codigo, boolean liberado, String mensagem, String proximaAcao, int segundos, String aviso) {
    return new SituacaoReceita(codigo, liberado, mensagem, proximaAcao, segundos, aviso, null);
  }
}
