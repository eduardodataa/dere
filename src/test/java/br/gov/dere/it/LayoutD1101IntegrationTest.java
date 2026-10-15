package br.gov.dere.it;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

class LayoutD1101IntegrationTest extends IntegrationTestSupport {
  @Test
  void baixarModeloCsv() throws Exception {
    mockMvc.perform(get("/api/layouts/D-1101/modelo.csv"))
        .andExpect(status().isOk())
        .andExpect(content().string(org.hamcrest.Matchers.containsString("id;")));
  }

  @Test
  void validarCsv() throws Exception {
    var csv = withUniqueId(fixture("envio-ciclo/1101-incluir.csv"));
    mockMvc.perform(post("/api/layouts/d1101/validate-csv")
            .contentType(MediaType.parseMediaType("text/csv"))
            .header("X-Entity-Id", entityId)
            .content(csv))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.valido").exists());
  }

  @Test
  void validarXml() throws Exception {
    var xml = withUniqueId(fixture("envio-ciclo/1101-incluir.xml"));
    mockMvc.perform(post("/api/layouts/d1101/validate-xml")
            .contentType(MediaType.APPLICATION_XML)
            .header("X-Entity-Id", entityId)
            .content(xml))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.valido").exists());
  }
}
