package br.gov.dere.application.ingestion;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.springframework.web.multipart.MultipartFile;

public final class LeitorXmlUpload {
  public static final int LIMITE_MB = 50;
  public static final int LIMITE_BYTES = LIMITE_MB * 1024 * 1024;
  static final int LIMITE_ENTRADAS = 10_000;

  private LeitorXmlUpload() {}

  public static List<Documento> ler(MultipartFile arquivo, int limiteBytes) throws IOException {
    if (arquivo == null || arquivo.isEmpty()) throw new IllegalArgumentException("Arquivo vazio");
    var nome = arquivo.getOriginalFilename();
    if (nome == null || nome.isBlank()) nome = "upload.xml";
    var limite = limiteBytes < 1 || limiteBytes > LIMITE_BYTES ? LIMITE_BYTES : limiteBytes;
    try (var entrada = arquivo.getInputStream()) {
      return ler(entrada, nome, limite);
    }
  }

  static List<Documento> ler(InputStream entrada, String nomeArquivo, int limite) throws IOException {
    if (nomeArquivo.toLowerCase().endsWith(".zip")) return lerZip(entrada, limite);
    var bytes = lerContando(entrada, limite, 0, "XML excede " + textoLimite(limite));
    return List.of(new Documento(nomeArquivo, new String(bytes, StandardCharsets.UTF_8)));
  }

  private static List<Documento> lerZip(InputStream entrada, int limite) throws IOException {
    var documentos = new ArrayList<Documento>();
    var mensagem = "Lote ZIP excede " + textoLimite(limite) + " descompactados";
    long total = 0;
    var entradas = 0;
    try (var zip = new ZipInputStream(entrada)) {
      ZipEntry entry;
      while ((entry = zip.getNextEntry()) != null) {
        if (++entradas > LIMITE_ENTRADAS) throw new IllegalArgumentException("Lote ZIP excede o limite de entradas");
        if (entry.isDirectory()) continue;
        var nome = entry.getName();
        var aceito = !nomeInseguro(nome) && nome.toLowerCase().endsWith(".xml");
        var bytes = lerContando(zip, limite, total, mensagem);
        total += bytes.length;
        if (aceito) documentos.add(new Documento(nome, new String(bytes, StandardCharsets.UTF_8)));
      }
    }
    return documentos;
  }

  private static byte[] lerContando(InputStream entrada, int limite, long jaLido, String mensagem) throws IOException {
    var saida = new ByteArrayOutputStream();
    var bloco = new byte[8192];
    long lidoNaEntrada = 0;
    int quantidade;
    while ((quantidade = entrada.read(bloco)) >= 0) {
      lidoNaEntrada += quantidade;
      if (lidoNaEntrada > limite || jaLido + lidoNaEntrada > limite) throw new IllegalArgumentException(mensagem);
      saida.write(bloco, 0, quantidade);
    }
    return saida.toByteArray();
  }

  private static String textoLimite(int limite) {
    var mega = 1024 * 1024;
    if (limite % mega == 0) return "o limite de " + (limite / mega) + " MB";
    return "o limite de " + limite + " bytes";
  }

  private static boolean nomeInseguro(String nome) {
    return nome == null || nome.startsWith("/") || nome.startsWith("\\") || nome.contains("..") || nome.matches("^[A-Za-z]:.*");
  }

  public record Documento(String nome, String xml) {}
}
