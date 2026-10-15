package br.gov.dere.it;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

class LayoutD1001IntegrationTest extends IntegrationTestSupport {
  @Test
  void baixarModeloCsv() throws Exception {
    mockMvc.perform(get("/api/layouts/D-1001/modelo.csv"))
        .andExpect(status().isOk())
        .andExpect(content().string(org.hamcrest.Matchers.containsString("id;")));
  }

  @Test
  void validarCsv() throws Exception {
    var csv = withUniqueId(fixture("envio-ciclo/1001-incluir.csv"));
    mockMvc.perform(post("/api/validation/csv-to-xml")
            .contentType(MediaType.parseMediaType("text/csv"))
            .header("X-Entity-Id", entityId)
            .content(csv))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.valido").exists());
  }

  @Test
  void validarXml() throws Exception {
    var xml = withUniqueId(fixture("envio-ciclo/1001-incluir.xml"));
    mockMvc.perform(post("/api/validation/xml-to-csv")
            .contentType(MediaType.APPLICATION_XML)
            .header("X-Entity-Id", entityId)
            .content(xml))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.valido").exists());
  }

  @Test
  void importarXml() throws Exception {
    var xml = withUniqueId(fixture("envio-ciclo/1001-incluir.xml"));
    var file = new MockMultipartFile("file", "1001-incluir.xml", "application/xml", xml.getBytes());
    mockMvc.perform(multipart("/api/layouts/d1001/import")
            .file(file)
            .header("X-User-Id", masterId)
            .header("X-Entity-Id", entityId))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.quantidadeArquivos").value(1));
  }
}
