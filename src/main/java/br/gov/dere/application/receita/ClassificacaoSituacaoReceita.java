package br.gov.dere.application.receita;

import java.util.Locale;

public final class ClassificacaoSituacaoReceita {
  public static final int INTERVALO_SEGUNDOS = 60;

  private ClassificacaoSituacaoReceita() {}

  public static SituacaoReceita simulado() {
    return new SituacaoReceita(
        "SIMULADO",
        true,
        "Este ambiente não consulta a Receita Federal. O envio fica só na simulação.",
        "Nenhuma consulta externa será feita.",
        0);
  }

  public static SituacaoReceita disponivel() {
    return new SituacaoReceita(
        "DISPONIVEL",
        true,
        "A Receita Federal entregou o token de acesso.",
        "A consulta será repetida. O envio não é repetido sozinho.",
        INTERVALO_SEGUNDOS);
  }

  public static SituacaoReceita falha(String texto) {
    var base = texto == null ? "" : texto.toLowerCase(Locale.ROOT);
    if (base.contains("erro ao gerar token") || base.contains("access_token")) {
      return new SituacaoReceita(
          "TOKEN",
          false,
          "A Receita Federal não entregou o token de acesso. Nenhum lote será enviado enquanto isso continuar.",
          "A consulta será repetida. O envio não será repetido sozinho.",
          INTERVALO_SEGUNDOS);
    }
    if (base.contains("credenciais receita integra")) {
      return new SituacaoReceita(
          "CREDENCIAL",
          false,
          "As credenciais da Receita Federal não estão configuradas neste servidor.",
          "O envio real fica bloqueado até a configuração ser corrigida.",
          INTERVALO_SEGUNDOS);
    }
    return new SituacaoReceita(
        "COMUNICACAO",
        false,
        "Não foi possível falar com a Receita Federal a partir deste servidor.",
        "A consulta será repetida. O envio não será repetido sozinho.",
        INTERVALO_SEGUNDOS);
  }
}
