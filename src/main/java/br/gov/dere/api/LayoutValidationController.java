package br.gov.dere.api;

import br.gov.dere.application.layout.CsvModelo;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/layouts")
public class LayoutValidationController {
  @GetMapping(value = "/{codigo}/modelo.csv", produces = "text/csv")
  public ResponseEntity<String> modelo(@PathVariable String codigo) {
    var leiaute = CsvModelo.normalizar(codigo);
    var nome = leiaute.toLowerCase().replace("-", "") + "-modelo.csv";
    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + nome)
        .contentType(MediaType.parseMediaType("text/csv;charset=UTF-8"))
        .body(CsvModelo.cabecalho(leiaute) + "\n");
  }
}
