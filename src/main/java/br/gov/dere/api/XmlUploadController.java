package br.gov.dere.api;

import br.gov.dere.application.access.AccessService;
import br.gov.dere.application.ingestion.XmlBatchIngestionService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/uploads")
public class XmlUploadController {
    private final XmlBatchIngestionService service;
    private final AccessService access;

    public XmlUploadController(XmlBatchIngestionService service, AccessService access) {
        this.service = service;
        this.access = access;
    }

    @PostMapping(value = "/xml", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public XmlBatchIngestionService.Summary upload(
            @RequestPart("file") MultipartFile file,
            @RequestHeader("X-User-Id") Long userId,
            @RequestHeader("X-Entity-Id") Long entityId) throws Exception {
        access.assertAccess(userId, entityId);
        return service.ingest(file, userId, entityId);
    }
}
