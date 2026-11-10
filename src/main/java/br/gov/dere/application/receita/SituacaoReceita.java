package br.gov.dere.application.receita;

public record SituacaoReceita(
    String codigo,
    boolean envioLiberado,
    String mensagem,
    String proximaAcao,
    int segundosProximaConsulta,
    String aviso,
    DetalheSituacaoReceita detalhe,
    String validadeCredencial) {

  public SituacaoReceita comSegundos(int segundos) {
    return new SituacaoReceita(codigo, envioLiberado, mensagem, proximaAcao, segundos, aviso, detalhe, validadeCredencial);
  }

  public SituacaoReceita comDetalhe(DetalheSituacaoReceita novo) {
    return new SituacaoReceita(codigo, envioLiberado, mensagem, proximaAcao, segundosProximaConsulta, aviso, novo, validadeCredencial);
  }

  public SituacaoReceita comValidade(String validade) {
    return new SituacaoReceita(codigo, envioLiberado, mensagem, proximaAcao, segundosProximaConsulta, aviso, detalhe, validade);
  }
}
