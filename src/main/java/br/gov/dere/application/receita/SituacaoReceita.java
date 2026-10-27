package br.gov.dere.application.receita;

public record SituacaoReceita(
    String codigo,
    boolean envioLiberado,
    String mensagem,
    String proximaAcao,
    int segundosProximaConsulta,
    String aviso,
    DetalheSituacaoReceita detalhe) {

  public SituacaoReceita comSegundos(int segundos) {
    return new SituacaoReceita(codigo, envioLiberado, mensagem, proximaAcao, segundos, aviso, detalhe);
  }

  public SituacaoReceita comDetalhe(DetalheSituacaoReceita novo) {
    return new SituacaoReceita(codigo, envioLiberado, mensagem, proximaAcao, segundosProximaConsulta, aviso, novo);
  }
}
