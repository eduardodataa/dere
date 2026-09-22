package br.gov.dere.domain.transmission;

public record QueryResult(String protocol, TransmissionState state, String governmentCode, String receipt, String responseBody, int httpStatus) {
}
