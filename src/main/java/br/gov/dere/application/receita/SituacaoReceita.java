package br.gov.dere.application.receita;

public record SituacaoReceita(
    String codigo,
    boolean envioLiberado,
    String mensagem,
    String proximaAcao,
    int segundosProximaConsulta) {

  public SituacaoReceita comSegundos(int segundos) {
    return new SituacaoReceita(codigo, envioLiberado, mensagem, proximaAcao, segundos);
  }
}
