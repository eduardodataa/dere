package br.gov.dere.application.certificate;

public interface CertificatePasswordStore {
  String guardar(Long entityId, String password) throws Exception;

  String ler(String referencia) throws Exception;

  void apagar(String referencia);
}
