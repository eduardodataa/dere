package br.gov.dere.api;

import br.gov.dere.application.certificate.CertificateService;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/certificates")
public class CertificateController {
  private final CertificateService service;
  public CertificateController(CertificateService service) { this.service = service; }
  @GetMapping("/entity/{entityId}") public List<CertificateService.CertificateDto> list(@PathVariable Long entityId) { return service.list(entityId); }
  @PostMapping(value = "/entity/{entityId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public CertificateService.CertificateDto upload(@PathVariable Long entityId, @RequestPart String label, @RequestPart MultipartFile file, @RequestPart String password) throws Exception {
    var saved = service.save(entityId, label, file, password);
    return service.list(entityId).stream().filter(item -> item.id().equals(saved.getId())).findFirst().orElseThrow();
  }
}
