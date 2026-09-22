package br.gov.dere.api;

import br.gov.dere.application.access.AccessService;
import br.gov.dere.application.d1001.D1001ImportService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/layouts/d1001")
public class D1001ImportController {
  private final D1001ImportService service;
  private final AccessService access;

  public D1001ImportController(D1001ImportService service, AccessService access) {
    this.service = service;
    this.access = access;
  }

  @PostMapping(value = "/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public D1001ImportService.RelatorioImportacao importarXml(
      @RequestPart("file") MultipartFile file,
      @RequestHeader("X-User-Id") Long userId,
      @RequestHeader("X-Entity-Id") Long entityId) throws Exception {
    access.assertAccess(userId, entityId);
    return service.importFiles(file, userId, entityId);
  }
}
