package br.gov.dere.application.transmission;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.springframework.stereotype.Component;

@Component
public class TransmissionArtifactStore {
  private static final int LIMITE = 400_000;
  private final Path raiz = Path.of("data", "transmissoes");

  public String guardar(String chave, String tipo, String conteudo) {
    if (conteudo == null || conteudo.getBytes(StandardCharsets.UTF_8).length <= LIMITE) return conteudo;
    try {
      Files.createDirectories(raiz);
      var arquivo = raiz.resolve(seguro(chave) + "-" + tipo + ".xml");
      Files.writeString(arquivo, conteudo, StandardCharsets.UTF_8);
      return "file:" + arquivo.toAbsolutePath();
    } catch (Exception ex) {
      throw new IllegalStateException("Não foi possível gravar o artefato " + tipo, ex);
    }
  }

  public String ler(String gravado) {
    if (gravado == null || !gravado.startsWith("file:")) return gravado;
    try {
      return Files.readString(Path.of(gravado.substring(5)), StandardCharsets.UTF_8);
    } catch (Exception ex) {
      throw new IllegalStateException("Não foi possível ler o artefato persistido", ex);
    }
  }

  private static String seguro(String chave) {
    return (chave == null ? "evento" : chave).replaceAll("[^0-9A-Za-z._-]", "_");
  }
}
