package br.gov.dere.application.layout;

import br.gov.dere.application.pgcc.D1011CsvConverter;
import br.gov.dere.application.pgcc.D1011XmlGenerator;
import br.gov.dere.domain.layout.CsvDocument;
import br.gov.dere.domain.layout.LayoutCode;
import br.gov.dere.domain.layout.LayoutDefinition;
import br.gov.dere.domain.layout.LayoutVersion;
import br.gov.dere.domain.layout.ValidationContext;
import br.gov.dere.domain.layout.ValidationIssue;
import br.gov.dere.domain.layout.ValidationResult;
import br.gov.dere.domain.layout.XmlDocument;
import br.gov.dere.domain.pgcc.D1011;
import java.util.ArrayList;
import java.util.HashSet;
import org.springframework.stereotype.Component;

@Component
public class D1011LayoutDefinition implements LayoutDefinition<D1011> {
  private static final String SCHEMA = "/dere/schemas/v1_1_0/xsd/06-XSD-D-1011 (v. 1.0.1).xsd";
  private final D1011CsvConverter csv = new D1011CsvConverter();
  private final D1011XmlGenerator xml = new D1011XmlGenerator();

  @Override public LayoutCode layout() { return LayoutCode.D1011; }
  @Override public LayoutVersion version() { return new LayoutVersion("1.0.1"); }
  @Override public String schemaLocation() { return SCHEMA; }
  @Override public D1011 parseXml(XmlDocument document) throws Exception { return csv.fromXml(document.content()); }
  @Override public D1011 parseCsv(CsvDocument document) { return csv.fromCsv(document.content()); }
  @Override public ValidationResult validate(D1011 model, ValidationContext context) {
    var issues = new ArrayList<ValidationIssue>();
    if (model == null) return new ValidationResult(java.util.List.of(error("MODEL_EMPTY", "", "Modelo canônico ausente")));
    if (model.id() == null || model.id().isBlank()) issues.add(error("ID_REQUIRED", "id", "Identificador do evento obrigatório"));
    if (model.cnpjRoot() == null || model.cnpjRoot().isBlank()) issues.add(error("TAXPAYER_REQUIRED", "nrInsc", "Identificação do contribuinte obrigatória"));
    if (model.validFrom() == null) issues.add(error("VALIDITY_REQUIRED", "iniValid", "Início de vigência obrigatório"));
    if (model.validTo() != null && model.validFrom() != null && model.validTo().isBefore(model.validFrom())) issues.add(error("VALIDITY_ORDER", "fimValid", "Fim de vigência anterior ao início"));
    if (model.accounts().isEmpty()) issues.add(error("ACCOUNT_REQUIRED", "infoContas", "PGCC deve possuir ao menos uma conta"));
    var codes = new HashSet<String>();
    for (var account : model.accounts()) {
      if (account.accountCode() == null || account.accountCode().isBlank()) issues.add(error("ACCOUNT_CODE_REQUIRED", "cCta", "Código da conta obrigatório"));
      else if (!codes.add(account.accountCode())) issues.add(error("ACCOUNT_DUPLICATED", "cCta", "Código de conta duplicado: " + account.accountCode()));
      if (account.validFrom() == null) issues.add(error("ACCOUNT_VALIDITY_REQUIRED", "iniVig", "Início de vigência da conta obrigatório"));
      if (account.validTo() != null && account.validFrom() != null && account.validTo().isBefore(account.validFrom())) issues.add(error("ACCOUNT_VALIDITY_ORDER", "fimVig", "Fim de vigência da conta anterior ao início"));
    }
    return new ValidationResult(issues);
  }
  @Override public XmlDocument generateXml(D1011 model) throws Exception { return new XmlDocument(xml.generate(model)); }
  @Override public CsvDocument generateCsv(D1011 model) { return new CsvDocument(csv.toCsv(model)); }
  private static ValidationIssue error(String code, String field, String message) { return new ValidationIssue("BUSINESS", code, field, message, ValidationIssue.Severity.ERROR); }
}
