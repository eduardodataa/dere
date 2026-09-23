package br.gov.dere.application.layout;

import br.gov.dere.application.periodico.D1199CsvConverter;
import br.gov.dere.application.periodico.D1199XmlGenerator;
import br.gov.dere.domain.layout.CsvDocument;
import br.gov.dere.domain.layout.LayoutCode;
import br.gov.dere.domain.layout.LayoutDefinition;
import br.gov.dere.domain.layout.LayoutVersion;
import br.gov.dere.domain.layout.ValidationContext;
import br.gov.dere.domain.layout.ValidationIssue;
import br.gov.dere.domain.layout.ValidationResult;
import br.gov.dere.domain.layout.XmlDocument;
import br.gov.dere.domain.periodico.D1199;
import java.util.ArrayList;
import org.springframework.stereotype.Component;

@Component
public class D1199LayoutDefinition implements LayoutDefinition<D1199> {
  private static final String SCHEMA = "/dere/schemas/v1_1_0/xsd/06-XSD-D-1199 (v. 0.0.1).xsd";
  private final D1199CsvConverter csv = new D1199CsvConverter();
  private final D1199XmlGenerator xml = new D1199XmlGenerator();

  @Override public LayoutCode layout() { return LayoutCode.D1199; }
  @Override public LayoutVersion version() { return new LayoutVersion("0.0.1"); }
  @Override public String schemaLocation() { return SCHEMA; }
  @Override public D1199 parseXml(XmlDocument document) throws Exception { return csv.fromXml(document.content()); }
  @Override public D1199 parseCsv(CsvDocument document) { return csv.fromCsv(document.content()); }
  @Override public ValidationResult validate(D1199 model, ValidationContext context) {
    var issues = new ArrayList<ValidationIssue>();
    if (model == null) return new ValidationResult(java.util.List.of(error("MODEL_EMPTY", "", "Modelo canônico ausente")));
    if (model.id() == null || model.id().isBlank()) issues.add(error("ID_REQUIRED", "id", "Identificador do evento obrigatório"));
    if (model.cnpjRoot() == null || model.cnpjRoot().isBlank()) issues.add(error("TAXPAYER_REQUIRED", "nrInsc", "Identificação do contribuinte obrigatória"));
    if (model.period() == null || model.period().isBlank()) issues.add(error("PERIOD_REQUIRED", "perApur", "Período de apuração obrigatório"));
    if (model.operation() != 1) issues.add(error("OPERATION_INCLUSION_ONLY", "tpOper", "D-1199 aceita somente inclusão"));
    return new ValidationResult(issues);
  }
  @Override public XmlDocument generateXml(D1199 model) throws Exception { return new XmlDocument(xml.generate(model)); }
  @Override public CsvDocument generateCsv(D1199 model) { return new CsvDocument(csv.toCsv(model)); }
  private static ValidationIssue error(String code, String field, String message) {
    return new ValidationIssue("BUSINESS", code, field, message, ValidationIssue.Severity.ERROR);
  }
}
