package br.gov.dere.api;

import br.gov.dere.application.layout.D1011LayoutDefinition;
import br.gov.dere.application.validation.D1011DependencyValidator;
import br.gov.dere.domain.layout.CsvDocument;
import br.gov.dere.domain.layout.ValidationContext;
import br.gov.dere.domain.layout.XmlDocument;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestHeader;

@RestController
@RequestMapping("/api/layouts/d1011")
public class D1011ValidationController {
  private final D1011LayoutDefinition definition;
  private final D1011DependencyValidator dependencies;

  public D1011ValidationController(D1011LayoutDefinition definition, D1011DependencyValidator dependencies) { this.definition = definition; this.dependencies = dependencies; }

  @PostMapping(value = "/validate-csv", consumes = "text/csv")
  public Result validateCsv(@RequestBody String body, @RequestHeader(value = "X-Entity-Id", required = false) Long entityId) {
    try {
      var model = definition.parseCsv(new CsvDocument(body));
      var context = new ValidationContext(entityId, null);
      var validation = definition.validate(model, context);
      var issues = new java.util.ArrayList<>(validation.issues());
      issues.addAll(dependencies.validate(model, context));
      validation = new br.gov.dere.domain.layout.ValidationResult(issues);
      return new Result(validation.valid(), definition.generateXml(model).content(), definition.generateCsv(model).content(), validation);
    } catch (Exception ex) {
      return new Result(false, "", "", null);
    }
  }

  @PostMapping(value = "/xml-to-csv", consumes = "application/xml")
  public String xmlToCsv(@RequestBody String body) throws Exception {
    return definition.generateCsv(definition.parseXml(new XmlDocument(body))).content();
  }

  public record Result(boolean valid, String xml, String csv, Object details) {}
}
