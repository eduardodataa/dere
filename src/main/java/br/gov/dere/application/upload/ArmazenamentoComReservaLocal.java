package br.gov.dere.application.upload;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class ArmazenamentoComReservaLocal implements ArmazenamentoObjetoUpload {
  private final ArmazenamentoObjetoUpload remoto;
  private final Path raiz;

  public ArmazenamentoComReservaLocal(ArmazenamentoObjetoUpload remoto, Path raiz) {
    if (remoto == null) throw new IllegalArgumentException("Armazenamento remoto obrigatório");
    if (raiz == null) throw new IllegalArgumentException("Diretório local obrigatório");
    this.remoto = remoto;
    this.raiz = raiz.toAbsolutePath().normalize();
  }

  @Override
  public String gravar(String chave, byte[] conteudo, String contentType) {
    var segura = validarChave(chave);
    try {
      return remoto.gravar(segura, conteudo, contentType);
    } catch (RuntimeException ex) {
      return gravarLocal(segura, conteudo);
    }
  }

  @Override
  public byte[] lerBytes(String uri) {
    if (uri != null && uri.startsWith("file:")) return lerArquivo(Path.of(uri.substring("file:".length())));
    try {
      return remoto.lerBytes(uri);
    } catch (RuntimeException ex) {
      var chave = chaveDeUri(uri);
      if (chave == null) throw ex;
      var destino = destino(chave);
      if (!Files.exists(destino)) throw ex;
      return lerArquivo(destino);
    }
  }

  @Override
  public String lerTexto(String uri) {
    return new String(lerBytes(uri), StandardCharsets.UTF_8);
  }

  private String gravarLocal(String chave, byte[] conteudo) {
    try {
      var destino = destino(chave);
      Files.createDirectories(destino.getParent());
      Files.write(destino, conteudo);
      return "file:" + destino;
    } catch (Exception ex) {
      throw new IllegalStateException("Falha ao gravar objeto no diretório local", ex);
    }
  }

  private Path destino(String chave) {
    var destino = raiz.resolve(chave).normalize();
    if (!destino.startsWith(raiz)) throw new IllegalArgumentException("Chave do objeto inválida");
    return destino;
  }

  private static String validarChave(String chave) {
    if (chave == null || chave.isBlank() || chave.contains("..") || chave.startsWith("/") || chave.contains("\\")) {
      throw new IllegalArgumentException("Chave do objeto inválida");
    }
    return chave;
  }

  private static String chaveDeUri(String uri) {
    if (uri == null || !uri.startsWith("s3://")) return null;
    var resto = uri.substring("s3://".length());
    var barra = resto.indexOf('/');
    if (barra < 0 || barra == resto.length() - 1) return null;
    return resto.substring(barra + 1);
  }

  private static byte[] lerArquivo(Path caminho) {
    try {
      return Files.readAllBytes(caminho);
    } catch (Exception ex) {
      throw new IllegalStateException("Falha ao ler objeto local", ex);
    }
  }
}
