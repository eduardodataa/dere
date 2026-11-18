package br.gov.dere.application.upload;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.UUID;

public final class ChaveObjetoUpload {
  private static final DateTimeFormatter MES = DateTimeFormatter.ofPattern("yyyy-MM");
  private static final ZoneId FUSO = ZoneId.of("America/Sao_Paulo");

  private ChaveObjetoUpload() {}

  public static String montar(String ambiente, Long entityId, String nomeEntidade, String idUpload, String nomeArquivo) {
    var pasta = PastaAmbienteUpload.de(ambiente);
    var entidade = PastaEntidadeUpload.de(entityId, nomeEntidade);
    var mes = LocalDate.now(FUSO).format(MES);
    var id = sanitizarTrecho(idUpload);
    var nome = sanitizarNome(nomeArquivo);
    return pasta + "/" + entidade + "/" + mes + "/" + id + "-" + nome;
  }

  private static String sanitizarTrecho(String valor) {
    if (valor == null || valor.isBlank()) return UUID.randomUUID().toString().replace("-", "");
    var limpo = valor.replaceAll("[^0-9A-Za-z._-]", "_");
    if (limpo.isBlank()) return UUID.randomUUID().toString().replace("-", "");
    return limpo;
  }

  public static String sanitizarNome(String nomeArquivo) {
    if (nomeArquivo == null || nomeArquivo.isBlank()) return "arquivo.bin";
    var original = nomeArquivo.replace('\\', '/');
    if (original.contains("..") || original.startsWith("/") || original.matches("^[A-Za-z]:.*")) {
      throw new IllegalArgumentException("Nome de arquivo inválido");
    }
    var barra = original.lastIndexOf('/');
    var nome = barra >= 0 ? original.substring(barra + 1) : original;
    if (nome.isBlank()) throw new IllegalArgumentException("Nome de arquivo inválido");
    var limpo = nome.replaceAll("[^0-9A-Za-z._-]", "_");
    if (limpo.isBlank()) throw new IllegalArgumentException("Nome de arquivo inválido");
    return limpo.toLowerCase(Locale.ROOT);
  }

  public static String uri(String bucket, String chave) {
    return "s3://" + bucket + "/" + chave;
  }

  public static String chaveDeUri(String uri, String bucket) {
    var prefixo = "s3://" + bucket + "/";
    if (uri == null || !uri.startsWith(prefixo)) {
      throw new IllegalArgumentException("URI S3 inválida");
    }
    return uri.substring(prefixo.length());
  }
}
