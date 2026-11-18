package br.gov.dere.application.upload;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.ConcurrentHashMap;

public class MemoriaArmazenamentoObjetoUpload implements ArmazenamentoObjetoUpload {
  private final String bucket;
  private final ConcurrentHashMap<String, byte[]> objetos = new ConcurrentHashMap<>();

  public MemoriaArmazenamentoObjetoUpload(String bucket) {
    this.bucket = bucket == null || bucket.isBlank() ? "dataa-dere" : bucket;
  }

  @Override
  public String gravar(String chave, byte[] conteudo, String contentType) {
    if (chave == null || chave.isBlank()) throw new IllegalArgumentException("Chave do objeto obrigatória");
    if (conteudo == null) throw new IllegalArgumentException("Conteúdo do objeto obrigatório");
    objetos.put(chave, conteudo);
    return ChaveObjetoUpload.uri(bucket, chave);
  }

  @Override
  public byte[] lerBytes(String uri) {
    var chave = ChaveObjetoUpload.chaveDeUri(uri, bucket);
    var bytes = objetos.get(chave);
    if (bytes == null) throw new IllegalStateException("Objeto não encontrado: " + uri);
    return bytes;
  }

  @Override
  public String lerTexto(String uri) {
    return new String(lerBytes(uri), StandardCharsets.UTF_8);
  }
}
