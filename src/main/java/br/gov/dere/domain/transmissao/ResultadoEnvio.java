package br.gov.dere.domain.transmissao;

public record ResultadoEnvio(String protocol, String codigoGoverno, String responseBody, int httpStatus) {
}
