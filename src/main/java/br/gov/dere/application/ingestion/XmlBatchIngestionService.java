package br.gov.dere.application.ingestion;

import br.gov.dere.application.configuracao.ConfiguracaoIntegracaoService;
import br.gov.dere.integration.xml.XmlSupport;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.sql.PreparedStatement;
import java.util.Map;
import java.util.Optional;
import javax.xml.parsers.DocumentBuilderFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Node;

@Service
public class XmlBatchIngestionService {
    private static final Map<String, String> TYPES = Map.ofEntries(
        Map.entry("evtInfoContrib", "d1001"), Map.entry("evtPGCC", "d1011"),
        Map.entry("evtBalancete", "d1101"), Map.entry("evtAplicResTec", "d1106"),
        Map.entry("evtDebOpOfPublic", "d2101"), Map.entry("evtFechMensal", "d1199"),
        Map.entry("evtRetornoTabela", "d9001"), Map.entry("evtRetornoBalan", "d9101"),
        Map.entry("evtRetornoAplicFin", "d9106"), Map.entry("evtRetornoTitPub", "d9121"),
        Map.entry("evtRetornoMensal", "d9199"));

    private final JdbcTemplate jdbc;
    private final ConfiguracaoIntegracaoService integracao;
    public XmlBatchIngestionService(JdbcTemplate jdbc, ConfiguracaoIntegracaoService integracao) {
        this.jdbc = jdbc;
        this.integracao = integracao;
    }

    public Summary ingest(MultipartFile file, Long userId, Long entityId) throws Exception {
        var documents = LeitorXmlUpload.ler(file, integracao.current().limiteUploadMb() * 1024 * 1024);
        if (documents.isEmpty()) throw new IllegalArgumentException("Nenhum XML encontrado no arquivo");
        String source = Optional.ofNullable(file.getOriginalFilename()).orElse("upload.xml");
        long totalBytes = documents.stream().mapToLong(x -> x.xml().getBytes(StandardCharsets.UTF_8).length).sum();
        long batchId = insertBatch(userId, entityId, source, documents.size(), totalBytes);
        int valid = 0, invalid = 0;
        for (LeitorXmlUpload.Documento document : documents) {
            String type = null, message = "";
            boolean isValid = false;
            try {
                var factory = DocumentBuilderFactory.newInstance();
                factory.setNamespaceAware(true);
                factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
                var dom = factory.newDocumentBuilder().parse(new ByteArrayInputStream(document.xml().getBytes(StandardCharsets.UTF_8)));
                Node root = dom.getDocumentElement();
                for (Node node = root.getFirstChild(); node != null; node = node.getNextSibling()) {
                    if (node.getNodeType() == Node.ELEMENT_NODE) { type = TYPES.get(node.getLocalName()); break; }
                }
                if (type == null) throw new IllegalArgumentException("Layout não reconhecido");
                String xsd = schema(type);
                if (xsd == null) throw new IllegalArgumentException("XSD oficial não disponível para " + type);
                var schemaUrl = getClass().getResource(xsd);
                if (schemaUrl == null) throw new IllegalArgumentException("XSD não encontrado: " + xsd);
                XmlSupport.validate(document.xml(), schemaUrl);
                isValid = true; valid++;
            } catch (Exception ex) {
                invalid++;
                message = Optional.ofNullable(ex.getMessage()).orElse(ex.getClass().getSimpleName());
                if (type == null) type = detect(document.xml());
            }
            if (type != null) insertDocument(type, batchId, userId, entityId, document, isValid, message);
        }
        jdbc.update("UPDATE dere_upload_batch SET valid_count=?, invalid_count=? WHERE id=?", valid, invalid, batchId);
        return new Summary(batchId, source, documents.size(), valid, invalid, totalBytes);
    }

    private long insertBatch(Long userId, Long entityId, String source, int count, long bytes) {
        KeyHolder keys = new GeneratedKeyHolder();
        jdbc.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO dere_upload_batch(user_id,entity_id,source_name,file_count,valid_count,invalid_count,total_bytes,created_at) VALUES(?,?,?,?,?,?,?,CURRENT_TIMESTAMP)",
                new String[] { "id" });
            ps.setLong(1, userId); ps.setLong(2, entityId); ps.setString(3, source);
            ps.setInt(4, count); ps.setInt(5, 0); ps.setInt(6, 0); ps.setLong(7, bytes); return ps;
        }, keys);
        return keys.getKey().longValue();
    }

    private void insertDocument(String type, long batchId, Long userId, Long entityId, LeitorXmlUpload.Documento doc, boolean valid, String message) {
        jdbc.update("INSERT INTO dere_" + type + "_xml(upload_batch_id,user_id,entity_id,source_name,valid,validation_message,xml_content,created_at) VALUES(?,?,?,?,?,?,?,CURRENT_TIMESTAMP)", batchId, userId, entityId, doc.nome(), valid, message, doc.xml());
    }

    private String detect(String xml) { return TYPES.entrySet().stream().filter(e -> xml.contains(e.getKey())).map(Map.Entry::getValue).findFirst().orElse(null); }

    private String schema(String type) {
        return switch (type) {
            case "d1001" -> "/dere/schemas/nota_2026_001/xsd/evtInfoContrib-v1_0_1.xsd";
            case "d1011" -> "/dere/schemas/nota_2026_001/xsd/evtPGCC-v1_0_2.xsd";
            case "d1101" -> "/dere/schemas/nota_2026_001/xsd/evtBalancete-v1_0_0.xsd";
            case "d1106" -> "/dere/schemas/nota_2026_001/xsd/evtAplicResTec-v1_0_0.xsd";
            case "d9001" -> "/dere/schemas/nota_2026_001/xsd/evtRetornoTabela-v1_0_1.xsd";
            case "d9101" -> "/dere/schemas/nota_2026_001/xsd/evtRetornoBalan-v1_0_0.xsd";
            case "d9106" -> "/dere/schemas/nota_2026_001/xsd/evtRetornoAplicFin-v1_0_0.xsd";
            case "d1199", "d2101", "d9121", "d9199" -> "/dere/schemas/v1_1_0/xsd/06-XSD-D-" + type.substring(1).toUpperCase() + " (v. 0.0.1).xsd";
            default -> null;
        };
    }
    public record Summary(long batchId, String sourceName, int fileCount, int validCount, int invalidCount, long totalBytes) {}
}
