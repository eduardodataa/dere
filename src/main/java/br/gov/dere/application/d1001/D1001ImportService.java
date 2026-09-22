package br.gov.dere.application.d1001;

import br.gov.dere.application.access.AccessService;
import br.gov.dere.application.csv.DereCsvConverter;
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
import javax.xml.parsers.DocumentBuilderFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

@Service
public class D1001ImportService {
  private static final int MAX_XML_BYTES = 10_000_000;
  private static final long MAX_BATCH_BYTES = 100_000_000L;
  private static final String XSD = "/dere/schemas/nota_2026_001/xsd/evtInfoContrib-v1_0_1.xsd";
  private final JdbcTemplate jdbc;
  private final AccessService access;
  private final DereCsvConverter converter = new DereCsvConverter();

  public D1001ImportService(JdbcTemplate jdbc, AccessService access) {
    this.jdbc = jdbc;
    this.access = access;
  }

  public ImportReport importFiles(MultipartFile file, Long userId, Long entityId) throws Exception {
    if (file.isEmpty()) throw new IllegalArgumentException("Arquivo vazio");
    String source = Optional.ofNullable(file.getOriginalFilename()).orElse("upload.xml");
    List<NamedXml> documents = extract(file, source);
    if (documents.isEmpty()) throw new IllegalArgumentException("Nenhum XML encontrado no arquivo");
    String expectedCnpj = access.entity(entityId).getCnpjRoot();
    var xsd = getClass().getResource(XSD);
    if (xsd == null) throw new IllegalStateException("XSD D-1001 não encontrado");
    long totalBytes = documents.stream().mapToLong(x -> x.xml().getBytes(StandardCharsets.UTF_8).length).sum();
    long batchId = insertBatch(userId, entityId, source, documents.size(), totalBytes);
    List<FileResult> files = new ArrayList<>();
    int valid = 0, invalid = 0;
    for (NamedXml document : documents) {
      List<XmlSupport.Issue> issues = new ArrayList<>();
      D1001 parsed = null;
      String eventId = "";
      String layout = detect(document.xml());
      if (!"evtInfoContrib".equals(layout)) {
        issues.add(new XmlSupport.Issue(0, 0, layout == null ? "" : layout, "erro",
            "Layout esperado: D-1001 (evtInfoContrib). Encontrado: " + (layout == null ? "desconhecido" : layout)));
      } else {
        issues.addAll(XmlSupport.validateCollecting(XmlSupport.forSchemaValidation(document.xml()), xsd));
        try {
          parsed = converter.fromXml(document.xml());
          eventId = parsed.id();
          if (parsed.cnpjRoot() != null && !parsed.cnpjRoot().equals(expectedCnpj)) {
            issues.add(new XmlSupport.Issue(0, 0, "nrInsc", "erro",
                "CNPJ raiz do XML (" + parsed.cnpjRoot() + ") diverge da entidade selecionada (" + expectedCnpj + ")"));
          }
        } catch (Exception ex) {
          issues.add(new XmlSupport.Issue(0, 0, "", "erro",
              "Falha ao transformar XML em objeto D-1001: " + Optional.ofNullable(ex.getMessage()).orElse(ex.getClass().getSimpleName())));
          eventId = readEventId(document.xml());
        }
      }
      boolean ok = issues.stream().noneMatch(i -> !"aviso".equals(i.severity()));
      if (ok) valid++; else invalid++;
      insertDocument(batchId, userId, entityId, document, ok, summarize(issues));
      files.add(new FileResult(document.name(), eventId, ok, parsed, issues));
    }
    jdbc.update("UPDATE dere_upload_batch SET valid_count=?, invalid_count=? WHERE id=?", valid, invalid, batchId);
    return new ImportReport(batchId, "D-1001", source, documents.size(), valid, invalid, files, toCsv(files), convertedCsv(files));
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

  private static String detect(String xml) {
    try {
      var factory = DocumentBuilderFactory.newInstance();
      factory.setNamespaceAware(true);
      factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
      var dom = factory.newDocumentBuilder().parse(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));
      for (Node node = dom.getDocumentElement().getFirstChild(); node != null; node = node.getNextSibling()) {
        if (node.getNodeType() == Node.ELEMENT_NODE) return node.getLocalName();
      }
    } catch (Exception ignored) {}
    return null;
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

  private static String summarize(List<XmlSupport.Issue> issues) {
    if (issues.isEmpty()) return "";
    var text = String.join(" | ", issues.stream().map(XmlSupport.Issue::message).toList());
    return text.length() <= 2000 ? text : text.substring(0, 1997) + "...";
  }

  private static String toCsv(List<FileResult> files) {
    var out = new StringBuilder("arquivo,idEvento,valido,linha,coluna,campo,severidade,critica\n");
    for (FileResult file : files) {
      if (file.issues().isEmpty()) {
        out.append(csv(file.fileName())).append(',').append(csv(file.eventId())).append(",SIM,,,,,\n");
        continue;
      }
      for (XmlSupport.Issue issue : file.issues()) {
        out.append(csv(file.fileName())).append(',').append(csv(file.eventId())).append(',')
            .append(file.valid() ? "SIM" : "NAO").append(',')
            .append(issue.line()).append(',').append(issue.column()).append(',')
            .append(csv(issue.field())).append(',').append(csv(issue.severity())).append(',')
            .append(csv(issue.message())).append('\n');
      }
    }
    return out.toString();
  }

  private static String csv(Object v) {
    var s = v == null ? "" : String.valueOf(v);
    return s.contains(",") || s.contains("\"") || s.contains("\n") ? "\"" + s.replace("\"", "\"\"") + "\"" : s;
  }

  private static boolean unsafeZipName(String name) {
    return name == null || name.startsWith("/") || name.startsWith("\\") || name.contains("..") || name.matches("^[A-Za-z]:.*");
  }

  public record FileResult(String fileName, String eventId, boolean valid, D1001 parsed, List<XmlSupport.Issue> issues) {}
  private String convertedCsv(List<FileResult> files) {
    var out = new StringBuilder();
    for (FileResult file : files) {
      if (file.parsed() == null) continue;
      var csv = converter.toCsv(file.parsed());
      if (out.isEmpty()) out.append(csv);
      else {
        var breakAt = csv.indexOf('\n');
        if (breakAt >= 0 && breakAt + 1 < csv.length()) out.append('\n').append(csv.substring(breakAt + 1));
      }
    }
    return out.toString();
  }

  public record ImportReport(long batchId, String layout, String sourceName, int fileCount, int validCount, int invalidCount, List<FileResult> files, String reportCsv, String convertedCsv) {}
  private record NamedXml(String name, String xml) {}
}
