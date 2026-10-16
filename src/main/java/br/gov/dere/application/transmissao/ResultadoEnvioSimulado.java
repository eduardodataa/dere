package br.gov.dere.application.transmissao;

import br.gov.dere.application.validacao.relatorio.RelatorioValidacao;

public record ResultadoEnvioSimulado(boolean enviado, RelatorioValidacao validacao, VisaoTransmissao transmissao) {}
