package br.gov.dere.domain.transmission;

public record SubmissionResult(String protocol, String governmentCode, String responseBody, int httpStatus) {
}
