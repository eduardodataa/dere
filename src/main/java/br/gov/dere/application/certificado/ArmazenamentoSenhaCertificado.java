package br.gov.dere.application.certificado;

public interface ArmazenamentoSenhaCertificado {
  String guardar(Long entityId, String password) throws Exception;

  String ler(String referencia) throws Exception;

  void apagar(String referencia);
}
