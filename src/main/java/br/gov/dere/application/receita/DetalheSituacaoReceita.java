package br.gov.dere.application.receita;

public record DetalheSituacaoReceita(
    boolean consultaRealizada,
    String enderecoVerificado,
    String enderecoRecepcao,
    String ambiente,
    String operacao,
    Integer httpStatus,
    String resposta,
    String verificadoEm) {}
