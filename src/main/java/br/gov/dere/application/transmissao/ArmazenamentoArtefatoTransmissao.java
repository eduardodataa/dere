package br.gov.dere.application.transmissao;

import br.gov.dere.application.upload.ArmazenamentoObjetoUpload;
import br.gov.dere.application.upload.ChaveObjetoUpload;
import br.gov.dere.application.upload.NomeEntidadeUpload;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class ArmazenamentoArtefatoTransmissao {
  private final ArmazenamentoObjetoUpload objetos;
  private final JdbcTemplate jdbc;
  private final String deployment;

  @Autowired
  public ArmazenamentoArtefatoTransmissao(
      ArmazenamentoObjetoUpload objetos,
      JdbcTemplate jdbc,
      @Value("${dere.deployment:local}") String deployment) {
    this.objetos = objetos;
    this.jdbc = jdbc;
    this.deployment = deployment;
  }

  public ArmazenamentoArtefatoTransmissao(ArmazenamentoObjetoUpload objetos) {
    this(objetos, null, "local");
  }

  public String guardar(Long entityId, String identificador, String tipo, String conteudo) {
    if (conteudo == null || conteudo.isBlank()) return conteudo;
    if (conteudo.startsWith("s3://") || conteudo.startsWith("file:")) return conteudo;
    var nome = "csv".equals(tipo) ? "origem.csv" : tipo + ".xml";
    var tipoConteudo = "csv".equals(tipo) ? "text/csv" : "application/xml";
    var chave = ChaveObjetoUpload.montar(deployment, entityId, NomeEntidadeUpload.de(jdbc, entityId), identificador, nome);
    return objetos.gravar(chave, conteudo.getBytes(StandardCharsets.UTF_8), tipoConteudo);
  }

  public String guardar(String identificador, String tipo, String conteudo) {
    if (conteudo != null && (conteudo.startsWith("s3://") || conteudo.startsWith("file:"))) return conteudo;
    return guardar(null, identificador, tipo, conteudo);
  }

  public String ler(String gravado) {
    if (gravado == null) return null;
    if (gravado.startsWith("s3://")) return objetos.lerTexto(gravado);
    if (!gravado.startsWith("file:")) return gravado;
    try {
      return Files.readString(Path.of(gravado.substring("file:".length())), StandardCharsets.UTF_8);
    } catch (Exception ex) {
      throw new IllegalStateException("Não foi possível ler o artefato persistido", ex);
    }
  }
}
