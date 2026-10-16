package br.gov.dere.domain.transmissao;

public record ResultadoConsulta(String protocol, EstadoTransmissao state, String codigoGoverno, String receipt, String responseBody, int httpStatus) {
}
