package br.gov.dere.api;

import br.gov.dere.application.csv.DereCsvConverter;
import br.gov.dere.application.d1001.D1001XmlGenerator;
import br.gov.dere.integration.xml.XmlSupport;
import java.util.List;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/validation")
public class ValidationController {
  private final DereCsvConverter csv = new DereCsvConverter();
  private final D1001XmlGenerator gen = new D1001XmlGenerator();

  @PostMapping("/csv-to-xml")
  public Result csv(@RequestBody String body) {
    try {
      var document = csv.fromCsv(body);
      var xml = gen.generate(document);
      var url = getClass().getResource("/dere/schemas/nota_2026_001/xsd/evtInfoContrib-v1_0_1.xsd");
      var issues = XmlSupport.validateCollecting(XmlSupport.forSchemaValidation(xml), url).stream().filter(issue -> !"aviso".equals(issue.severity())).toList();
      return new Result(issues.isEmpty(), xml, csv.toCsv(document), issues.stream().map(XmlSupport.Issue::message).toList());
    } catch (Exception ex) {
      var message = ex.getMessage() == null || ex.getMessage().isBlank() ? "Falha na validação CSV" : ex.getMessage();
      return new Result(false, "", "", List.of(message));
    }
  }

  @PostMapping("/xml-to-csv")
  public String xml(@RequestBody String body) throws Exception {
    return csv.xmlToCsv(body);
  }

  public record Result(boolean valid, String xml, String csv, List<String> errors) {}
}
