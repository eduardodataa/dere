package br.gov.dere.application.upload;

public interface ArmazenamentoObjetoUpload {
  String gravar(String chave, byte[] conteudo, String contentType);

  byte[] lerBytes(String uri);

  String lerTexto(String uri);
}
