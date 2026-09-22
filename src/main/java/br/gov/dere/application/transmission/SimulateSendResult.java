package br.gov.dere.application.transmission;

import br.gov.dere.application.validation.relatorio.RelatorioValidacao;

public record SimulateSendResult(boolean enviado, RelatorioValidacao validacao, TransmissionView transmissao) {}
