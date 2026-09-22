package br.gov.dere.api;

import br.gov.dere.application.access.AccessService;
import br.gov.dere.application.certificate.CertificateService;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/certificates")
public class CertificateController {
  private final CertificateService service;
  private final AccessService access;

  public CertificateController(CertificateService service, AccessService access) {
    this.service = service;
    this.access = access;
  }

  @GetMapping("/entity/{entityId}")
  public List<CertificateService.CertificateDto> list(
      @PathVariable Long entityId,
      @RequestHeader("X-User-Id") Long userId) {
    access.assertAccess(userId, entityId);
    return service.list(entityId);
  }

  @PostMapping(value = "/entity/{entityId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public CertificateService.CertificateDto upload(
      @PathVariable Long entityId,
      @RequestHeader("X-User-Id") Long userId,
      @RequestPart String label,
      @RequestPart MultipartFile file,
      @RequestPart String password) throws Exception {
    access.assertAccess(userId, entityId);
    var saved = service.save(entityId, label, file, password);
    return service.list(entityId).stream().filter(item -> item.id().equals(saved.getId())).findFirst().orElseThrow();
  }

  @DeleteMapping("/{id}")
  public void delete(
      @PathVariable Long id,
      @RequestHeader("X-User-Id") Long userId,
      @RequestHeader("X-Entity-Id") Long entityId) {
    access.assertAccess(userId, entityId);
    service.delete(entityId, id);
  }
}
