package br.gov.dere.integration.receita;

public class GovernmentTransmissionException extends RuntimeException {
  private final int statusCode;
  private final boolean retryable;
  private final String responseBody;
  public GovernmentTransmissionException(int statusCode, boolean retryable, String responseBody) { super("Falha na integração governamental HTTP "+statusCode); this.statusCode=statusCode; this.retryable=retryable; this.responseBody=responseBody; }
  public int statusCode(){return statusCode;}
  public boolean retryable(){return retryable;}
  public String responseBody(){return responseBody;}
}
