package br.gov.dere.it;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

class LayoutD1199IntegrationTest extends IntegrationTestSupport {
  @Test
  void baixarModeloCsv() throws Exception {
    autenticado(get("/api/layouts/D-1199/modelo.csv"))
        .andExpect(status().isOk())
        .andExpect(content().string(org.hamcrest.Matchers.containsString("id;")));
  }

  @Test
  void validarCsv() throws Exception {
    var csv = withUniqueId(fixture("envio-ciclo/1199-incluir.csv"));
    autenticado(post("/api/layouts/d1199/validate-csv")
            .contentType(MediaType.parseMediaType("text/csv"))
            .header("X-Entity-Id", entityId)
            .content(csv))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.valido").exists());
  }

  @Test
  void validarXml() throws Exception {
    var xml = withUniqueId(fixture("envio-ciclo/1199-incluir.xml"));
    autenticado(post("/api/layouts/d1199/validate-xml")
            .contentType(MediaType.APPLICATION_XML)
            .header("X-Entity-Id", entityId)
            .content(xml))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.valido").exists());
  }
}
