package br.gov.dere.application.validacao;

import br.gov.dere.application.transmissao.ArmazenamentoArtefatoTransmissao;
import br.gov.dere.domain.layout.ContextoValidacao;
import br.gov.dere.domain.layout.ProblemaValidacao;
import br.gov.dere.domain.pgcc.D1011;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class D1011DependencyValidator {
  private final JdbcTemplate jdbc;
  private final ArmazenamentoArtefatoTransmissao artefatos;

  public D1011DependencyValidator(JdbcTemplate jdbc, ArmazenamentoArtefatoTransmissao artefatos) {
    this.jdbc = jdbc;
    this.artefatos = artefatos;
  }

  public List<ProblemaValidacao> validate(D1011 document, ContextoValidacao context) {
    var issues = new ArrayList<ProblemaValidacao>();
    if (context == null || context.entityId() == null) {
      issues.add(error("ENTITY_REQUIRED", "entityId", "Entidade obrigatória para validar dependências"));
      return issues;
    }
    var records = jdbc.query(
        "SELECT xml_content FROM dere_d1001_xml WHERE entity_id=? AND valid=TRUE ORDER BY created_at DESC",
        (rs, row) -> rs.getString("xml_content"),
        context.entityId());
    var validD1001 = records.stream().map(xml -> parse(xml, issues)).filter(java.util.Objects::nonNull).toList();
    var matching = validD1001.stream()
        .filter(item -> document.cnpjRoot().equals(item.cnpjRoot()))
        .filter(item -> covers(item, document.validFrom()))
        .findAny();
    if (matching.isEmpty()) {
      issues.add(error("D1001_NOT_FOUND_OR_NOT_VALID", "nrInsc", "Não existe D-1001 válido e vigente para o D-1011"));
    }
    return issues;
  }

  private br.gov.dere.domain.contributor.D1001 parse(String gravado, List<ProblemaValidacao> issues) {
    try {
      return converterD1001(artefatos.ler(gravado));
    } catch (Exception ex) {
      issues.add(error("D1001_PARSE_ERROR", "", "Não foi possível ler um D-1001 persistido"));
      return null;
    }
  }

  private br.gov.dere.domain.contributor.D1001 converterD1001(String xml) throws Exception {
    return new br.gov.dere.application.csv.DereCsvConverter().fromXml(xml);
  }

  private static boolean covers(br.gov.dere.domain.contributor.D1001 d1001, LocalDate date) {
    return date != null
        && d1001.validFrom() != null
        && !date.isBefore(d1001.validFrom())
        && (d1001.validTo() == null || !date.isAfter(d1001.validTo()));
  }

  private static ProblemaValidacao error(String code, String field, String message) {
    return new ProblemaValidacao("DEPENDENCY", code, field, message, ProblemaValidacao.Severity.ERROR);
  }
}
