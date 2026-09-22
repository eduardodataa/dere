package br.gov.dere.application.validation.relatorio;

public enum TipoCritica {
  OBRIGATORIO("Obrigatório"),
  FORA_DO_PADRAO("Fora do padrão"),
  FORA_DO_DOMINIO("Fora de domínio"),
  NEGOCIO("Regra de negócio"),
  ESTRUTURA("Estrutura do arquivo");

  private final String rotulo;

  TipoCritica(String rotulo) {
    this.rotulo = rotulo;
  }

  public String rotulo() {
    return rotulo;
  }
}
