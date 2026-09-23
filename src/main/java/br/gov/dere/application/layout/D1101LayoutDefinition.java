package br.gov.dere.application.layout;

import br.gov.dere.application.periodico.D1101CsvConverter;
import br.gov.dere.application.periodico.D1101XmlGenerator;
import br.gov.dere.domain.layout.CsvDocument;
import br.gov.dere.domain.layout.LayoutCode;
import br.gov.dere.domain.layout.LayoutDefinition;
import br.gov.dere.domain.layout.LayoutVersion;
import br.gov.dere.domain.layout.ValidationContext;
import br.gov.dere.domain.layout.ValidationIssue;
import br.gov.dere.domain.layout.ValidationResult;
import br.gov.dere.domain.layout.XmlDocument;
import br.gov.dere.domain.periodico.D1101;
import java.util.ArrayList;
import org.springframework.stereotype.Component;

@Component
public class D1101LayoutDefinition implements LayoutDefinition<D1101> {
  private static final String SCHEMA = "/dere/schemas/v1_1_0/xsd/06-XSD-D-1101 (v. 0.0.1).xsd";
  private final D1101CsvConverter csv = new D1101CsvConverter();
  private final D1101XmlGenerator xml = new D1101XmlGenerator();

  @Override public LayoutCode layout() { return LayoutCode.D1101; }
  @Override public LayoutVersion version() { return new LayoutVersion("0.0.1"); }
  @Override public String schemaLocation() { return SCHEMA; }
  @Override public D1101 parseXml(XmlDocument document) throws Exception { return csv.fromXml(document.content()); }
  @Override public D1101 parseCsv(CsvDocument document) { return csv.fromCsv(document.content()); }
  @Override public ValidationResult validate(D1101 model, ValidationContext context) {
    var issues = new ArrayList<ValidationIssue>();
    if (model == null) return new ValidationResult(java.util.List.of(error("MODEL_EMPTY", "", "Modelo canônico ausente")));
    if (model.id() == null || model.id().isBlank()) issues.add(error("ID_REQUIRED", "id", "Identificador do evento obrigatório"));
    if (model.cnpjRoot() == null || model.cnpjRoot().isBlank()) issues.add(error("TAXPAYER_REQUIRED", "nrInsc", "Identificação do contribuinte obrigatória"));
    if (model.period() == null || model.period().isBlank()) issues.add(error("PERIOD_REQUIRED", "perApur", "Período de apuração obrigatório"));
    if (model.operation() != 3 && model.accounts().isEmpty()) issues.add(error("ACCOUNT_REQUIRED", "infoContas", "Balancete deve possuir ao menos uma conta analítica"));
    return new ValidationResult(issues);
  }
  @Override public XmlDocument generateXml(D1101 model) throws Exception { return new XmlDocument(xml.generate(model)); }
  @Override public CsvDocument generateCsv(D1101 model) { return new CsvDocument(csv.toCsv(model)); }
  private static ValidationIssue error(String code, String field, String message) {
    return new ValidationIssue("BUSINESS", code, field, message, ValidationIssue.Severity.ERROR);
  }
}
