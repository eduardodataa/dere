package br.gov.dere.api;

import br.gov.dere.application.access.AccessService;
import br.gov.dere.persistence.DereAuditoriaValidacaoRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/reports")
public class ReportController {
  private final DereAuditoriaValidacaoRepository repo;
  private final AccessService access;

  public ReportController(DereAuditoriaValidacaoRepository repo, AccessService access) {
    this.repo = repo;
    this.access = access;
  }

  @GetMapping(value = "/{id}", produces = "text/plain")
  public ResponseEntity<String> report(@PathVariable Long id, @RequestHeader("X-User-Id") Long userId) {
    var auditoria = repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Relatório não encontrado"));
    if (auditoria.getEntityId() == null) throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Relatório sem vínculo com entidade");
    access.assertAccess(userId, auditoria.getEntityId());
    var body = "RELATÓRIO DeRE\nData: " + auditoria.getCreatedAt() + "\nVálido: " + auditoria.isValid() + "\nErros: " + auditoria.getErrors() + "\n\nXML:\n" + auditoria.getPayloadXml() + "\n\nCSV:\n" + auditoria.getPayloadCsv();
    return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=relatorio-dere-" + id + ".txt").body(body);
  }
}
