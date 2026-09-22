package br.gov.dere.application.layout;

import br.gov.dere.application.csv.DereCsvConverter;
import br.gov.dere.application.d1001.D1001XmlGenerator;
import br.gov.dere.domain.contributor.D1001;
import br.gov.dere.domain.layout.CsvDocument;
import br.gov.dere.domain.layout.DereLayoutHandler;
import br.gov.dere.domain.layout.LayoutCode;
import br.gov.dere.domain.layout.LayoutDefinition;
import br.gov.dere.domain.layout.LayoutVersion;
import br.gov.dere.domain.layout.ValidationContext;
import br.gov.dere.domain.layout.ValidationIssue;
import br.gov.dere.domain.layout.ValidationResult;
import br.gov.dere.domain.layout.XmlDocument;
import br.gov.dere.integration.xml.XmlSupport;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class D1001LayoutDefinition implements LayoutDefinition<D1001> {
  private static final String SCHEMA = "/dere/schemas/nota_2026_001/xsd/evtInfoContrib-v1_0_1.xsd";

  private final DereCsvConverter csv = new DereCsvConverter();
  private final D1001XmlGenerator xml = new D1001XmlGenerator();

  @Override
  public LayoutCode layout() {
    return LayoutCode.D1001;
  }

  @Override
  public LayoutVersion version() {
    return new LayoutVersion("1.0.1");
  }

  @Override
  public String schemaLocation() {
    return SCHEMA;
  }

  @Override
  public D1001 parseXml(XmlDocument document) throws Exception {
    return csv.fromXml(document.content());
  }

  @Override
  public D1001 parseCsv(CsvDocument document) {
    return csv.fromCsv(document.content());
  }

  @Override
  public ValidationResult validate(D1001 model, ValidationContext context) {
    var issues = new ArrayList<ValidationIssue>();
    if (model == null) {
      issues.add(error("MODEL_EMPTY", "", "Modelo canônico ausente"));
      return new ValidationResult(issues);
    }
    if (context != null && context.entityId() == null) {
      issues.add(error("ENTITY_REQUIRED", "", "Entidade de contexto obrigatória"));
    }
    if (model.id() == null || model.id().isBlank()) {
      issues.add(error("ID_REQUIRED", "id", "Identificador do evento obrigatório"));
    }
    if (model.cnpjRoot() == null || model.cnpjRoot().isBlank()) {
      issues.add(error("TAXPAYER_REQUIRED", "nrInsc", "Identificação do contribuinte obrigatória"));
    }
    if (model.validFrom() == null) {
      issues.add(error("VALIDITY_REQUIRED", "iniValid", "Início de vigência obrigatório"));
    }
    if (model.validTo() != null && model.validFrom() != null && model.validTo().isBefore(model.validFrom())) {
      issues.add(error("VALIDITY_ORDER", "fimValid", "Fim de vigência anterior ao início"));
    }
    return new ValidationResult(issues);
  }

  @Override
  public XmlDocument generateXml(D1001 model) throws Exception {
    return new XmlDocument(xml.generate(model));
  }

  @Override
  public CsvDocument generateCsv(D1001 model) {
    return new CsvDocument(csv.toCsv(model));
  }

  private static ValidationIssue error(String code, String field, String message) {
    return new ValidationIssue("BUSINESS", code, field, message, ValidationIssue.Severity.ERROR);
  }
}
