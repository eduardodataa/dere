package br.gov.dere.application.d1001;

import br.gov.dere.application.access.AccessService;
import br.gov.dere.application.csv.DereCsvConverter;
import br.gov.dere.application.validation.ServicoValidacaoLeiaute;
import br.gov.dere.application.validation.relatorio.Critica;
import br.gov.dere.application.validation.relatorio.RelatorioValidacao;
import br.gov.dere.domain.contributor.D1001;
import br.gov.dere.integration.xml.XmlSupport;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Element;

@Service
public class D1001ImportService {
  private static final int MAX_XML_BYTES = 10_000_000;
  private static final long MAX_BATCH_BYTES = 100_000_000L;
  private final JdbcTemplate jdbc;
  private final AccessService access;
  private final ServicoValidacaoLeiaute validacao;
  private final DereCsvConverter converter = new DereCsvConverter();

  public D1001ImportService(JdbcTemplate jdbc, AccessService access, ServicoValidacaoLeiaute validacao) {
    this.jdbc = jdbc;
    this.access = access;
    this.validacao = validacao;
  }

  public void persistValidatedXml(Long userId, Long entityId, String sourceName, String xml) {
    long batchId = insertBatch(userId, entityId, sourceName == null ? "simulado.xml" : sourceName, 1, xml == null ? 0 : xml.getBytes(StandardCharsets.UTF_8).length);
    insertDocument(batchId, userId, entityId, new NamedXml(sourceName == null ? "simulado.xml" : sourceName, xml), true, "");
    jdbc.update("UPDATE dere_upload_batch SET valid_count=?, invalid_count=? WHERE id=?", 1, 0, batchId);
  }

  public RelatorioImportacao importFiles(MultipartFile file, Long userId, Long entityId) throws Exception {
    if (file.isEmpty()) throw new IllegalArgumentException("Arquivo vazio");
    String source = Optional.ofNullable(file.getOriginalFilename()).orElse("upload.xml");
    List<NamedXml> documents = extract(file, source);
    if (documents.isEmpty()) throw new IllegalArgumentException("Nenhum XML encontrado no arquivo");
    String cnpjEsperado = access.entity(entityId).getCnpjRoot();
    long totalBytes = documents.stream().mapToLong(x -> x.xml().getBytes(StandardCharsets.UTF_8).length).sum();
    long batchId = insertBatch(userId, entityId, source, documents.size(), totalBytes);
    List<ResultadoArquivo> arquivos = new ArrayList<>();
    List<Critica> criticas = new ArrayList<>();
    int validos = 0, invalidos = 0;
    for (NamedXml documento : documents) {
      var relatorio = validacao.validarXml("D-1001", documento.xml(), documento.name(), cnpjEsperado, entityId);
      criticas.addAll(relatorio.criticas());
      D1001 analisado = null;
      String idEvento = "";
      try {
        analisado = converter.fromXml(documento.xml());
        idEvento = analisado.id();
      } catch (Exception ex) {
        idEvento = readEventId(documento.xml());
      }
      boolean ok = relatorio.valido();
      if (ok) validos++; else invalidos++;
      insertDocument(batchId, userId, entityId, documento, ok, resumir(relatorio));
      arquivos.add(new ResultadoArquivo(documento.name(), idEvento, ok, analisado, paraProblemas(relatorio), relatorio.criticas()));
    }
    jdbc.update("UPDATE dere_upload_batch SET valid_count=?, invalid_count=? WHERE id=?", validos, invalidos, batchId);
    var planilha = validacao.agregar("D-1001", "XML", source, documents.size(), criticas);
    return new RelatorioImportacao(batchId, "D-1001", source, documents.size(), validos, invalidos, arquivos, toCsv(arquivos), csvConvertido(arquivos), criticas, planilha.relatorioXlsx());
  }

  private List<NamedXml> extract(MultipartFile file, String source) throws Exception {
    List<NamedXml> documents = new ArrayList<>();
    if (source.toLowerCase().endsWith(".zip")) {
      long total = 0;
      try (var zip = new ZipInputStream(new ByteArrayInputStream(file.getBytes()))) {
        ZipEntry entry;
        while ((entry = zip.getNextEntry()) != null) {
          String name = entry.getName();
          if (entry.isDirectory() || unsafeZipName(name) || !name.toLowerCase().endsWith(".xml")) continue;
          byte[] bytes = zip.readAllBytes();
          total += bytes.length;
          if (bytes.length > MAX_XML_BYTES || total > MAX_BATCH_BYTES)
            throw new IllegalArgumentException("Lote ZIP excede o limite permitido");
          documents.add(new NamedXml(name, new String(bytes, StandardCharsets.UTF_8)));
        }
      }
    } else {
      byte[] bytes = file.getBytes();
      if (bytes.length > MAX_XML_BYTES) throw new IllegalArgumentException("XML excede 10 MB");
      documents.add(new NamedXml(source, new String(bytes, StandardCharsets.UTF_8)));
    }
    return documents;
  }

  private long insertBatch(Long userId, Long entityId, String source, int count, long bytes) {
    var keys = new GeneratedKeyHolder();
    jdbc.update(connection -> {
      PreparedStatement ps = connection.prepareStatement(
          "INSERT INTO dere_upload_batch(user_id,entity_id,source_name,file_count,valid_count,invalid_count,total_bytes,created_at) VALUES(?,?,?,?,?,?,?,CURRENT_TIMESTAMP)",
          new String[] {"id"});
      ps.setLong(1, userId); ps.setLong(2, entityId); ps.setString(3, source);
      ps.setInt(4, count); ps.setInt(5, 0); ps.setInt(6, 0); ps.setLong(7, bytes); return ps;
    }, keys);
    return keys.getKey().longValue();
  }

  private void insertDocument(long batchId, Long userId, Long entityId, NamedXml doc, boolean valid, String message) {
    jdbc.update("INSERT INTO dere_d1001_xml(upload_batch_id,user_id,entity_id,source_name,valid,validation_message,xml_content,created_at) VALUES(?,?,?,?,?,?,?,CURRENT_TIMESTAMP)",
        batchId, userId, entityId, doc.name(), valid, message, doc.xml());
  }

  private static String readEventId(String xml) {
    try {
      var d = XmlSupport.parse(xml);
      var e = (Element) d.getElementsByTagNameNS(D1001XmlGenerator.NS, "evtInfoContrib").item(0);
      return e == null ? "" : Optional.ofNullable(e.getAttribute("id")).orElse("");
    } catch (Exception e) {
      return "";
    }
  }

  private static String resumir(RelatorioValidacao relatorio) {
    if (relatorio.criticas().isEmpty()) return "";
    var texto = String.join(" | ", relatorio.criticas().stream().map(Critica::problema).toList());
    return texto.length() <= 2000 ? texto : texto.substring(0, 1997) + "...";
  }

  private static List<XmlSupport.Issue> paraProblemas(RelatorioValidacao relatorio) {
    return relatorio.criticas().stream()
        .map(item -> new XmlSupport.Issue(item.linha() == null ? 0 : item.linha(), 0, item.coluna(), "erro", item.problema()))
        .toList();
  }

  private static String toCsv(List<ResultadoArquivo> arquivos) {
    var saida = new StringBuilder("arquivo,idEvento,valido,linha,coluna,campo,severidade,critica\n");
    for (ResultadoArquivo arquivo : arquivos) {
      if (arquivo.problemas().isEmpty()) {
        saida.append(csv(arquivo.nomeArquivo())).append(',').append(csv(arquivo.idEvento())).append(",SIM,,,,,\n");
        continue;
      }
      for (XmlSupport.Issue problema : arquivo.problemas()) {
        saida.append(csv(arquivo.nomeArquivo())).append(',').append(csv(arquivo.idEvento())).append(',')
            .append(arquivo.valido() ? "SIM" : "NAO").append(',')
            .append(problema.line()).append(',').append(problema.column()).append(',')
            .append(csv(problema.field())).append(',').append(csv(problema.severity())).append(',')
            .append(csv(problema.message())).append('\n');
      }
    }
    return saida.toString();
  }

  private static String csv(Object v) {
    var s = v == null ? "" : String.valueOf(v);
    return s.contains(",") || s.contains("\"") || s.contains("\n") ? "\"" + s.replace("\"", "\"\"") + "\"" : s;
  }

  private static boolean unsafeZipName(String name) {
    return name == null || name.startsWith("/") || name.startsWith("\\") || name.contains("..") || name.matches("^[A-Za-z]:.*");
  }

  public record ResultadoArquivo(String nomeArquivo, String idEvento, boolean valido, D1001 analisado, List<XmlSupport.Issue> problemas, List<Critica> criticas) {}
  private String csvConvertido(List<ResultadoArquivo> arquivos) {
    var saida = new StringBuilder();
    for (ResultadoArquivo arquivo : arquivos) {
      if (arquivo.analisado() == null) continue;
      var csv = converter.toCsv(arquivo.analisado());
      if (saida.isEmpty()) saida.append(csv);
      else {
        var quebra = csv.indexOf('\n');
        if (quebra >= 0 && quebra + 1 < csv.length()) saida.append('\n').append(csv.substring(quebra + 1));
      }
    }
    return saida.toString();
  }

  public record RelatorioImportacao(long idLote, String leiaute, String nomeOrigem, int quantidadeArquivos, int quantidadeValidos, int quantidadeInvalidos, List<ResultadoArquivo> arquivos, String csvRelatorio, String csvConvertido, List<Critica> criticas, String relatorioXlsx) {}
  private record NamedXml(String name, String xml) {}
}
