package br.gov.dere.it;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

class UploadXmlIntegrationTest extends IntegrationTestSupport {
  @Test
  void uploadXmlGenerico() throws Exception {
    var xml = withUniqueId(fixture("envio-ciclo/1001-incluir.xml"));
    var file = new MockMultipartFile("file", "1001-incluir.xml", "application/xml", xml.getBytes());
    mockMvc.perform(multipart("/api/uploads/xml")
            .file(file)
            .header("X-User-Id", masterId)
            .header("X-Entity-Id", entityId))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.fileCount").value(1));
  }
}
