package br.gov.dere.it;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

class TransmissaoIntegrationTest extends IntegrationTestSupport {
  @Test
  void simularConsultarListarDetalharPdfEExcluir() throws Exception {
    var csv = withUniqueId(fixture("envio-ciclo/1001-incluir.csv"));
    var envio = simulate("D-1001", "CSV", "1001-incluir.csv", csv)
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.enviado").value(true))
        .andExpect(jsonPath("$.transmissao.batchId").exists())
        .andReturn();
    Number batchId = JsonPath.read(envio.getResponse().getContentAsString(), "$.transmissao.batchId");

    autenticado(post("/api/transmissions/" + batchId + "/query")
            .header("X-User-Id", masterId)
            .header("X-Entity-Id", entityId))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.receiptNumber").isNotEmpty());

    autenticado(get("/api/transmissions")
            .header("X-User-Id", masterId)
            .header("X-Entity-Id", entityId))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].batchId").value(batchId.intValue()));

    autenticado(get("/api/transmissions/" + batchId)
            .header("X-User-Id", masterId)
            .header("X-Entity-Id", entityId))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.protocol").isNotEmpty())
        .andExpect(jsonPath("$.eventXml").isNotEmpty());

    autenticado(get("/api/transmissions/" + batchId + "/protocolo.pdf")
            .header("X-User-Id", masterId)
            .header("X-Entity-Id", entityId))
        .andExpect(status().isOk());

    autenticado(post("/api/transmissions/relatorio.pdf")
            .header("X-User-Id", masterId)
            .header("X-Entity-Id", entityId)
            .contentType(MediaType.APPLICATION_JSON)
            .content("[]"))
        .andExpect(status().isOk());

    autenticado(delete("/api/transmissions/" + batchId)
            .header("X-User-Id", masterId)
            .header("X-Entity-Id", entityId))
        .andExpect(status().isNoContent());
  }
}
