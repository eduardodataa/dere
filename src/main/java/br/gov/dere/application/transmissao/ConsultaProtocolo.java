package br.gov.dere.application.transmissao;

public record ConsultaProtocolo(
    String protocol,
    String status,
    String receiptNumber,
    String codigoGoverno,
    String returnXml,
    VisaoTransmissao transmissao,
    String aviso) {
}
