package br.gov.dere.it;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.gov.dere.integration.signature.CertificadoTesteLocal;
import com.jayway.jsonpath.JsonPath;
import java.io.ByteArrayOutputStream;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

class CertificadoIntegrationTest extends IntegrationTestSupport {
  @Test
  void listarArmazenarEExcluirCertificado() throws Exception {
    autenticado(get("/api/certificates/entity/" + entityId).header("X-User-Id", masterId))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray());

    var bytes = new ByteArrayOutputStream();
    CertificadoTesteLocal.create().store(bytes, "changeit".toCharArray());
    var file = new MockMultipartFile("file", "teste.p12", "application/x-pkcs12", bytes.toByteArray());

    var criado = autenticado(multipart("/api/certificates/entity/" + entityId)
            .file(file)
            .file(new MockMultipartFile("label", "", "text/plain", "A1 IT".getBytes()))
            .file(new MockMultipartFile("password", "", "text/plain", "changeit".getBytes()))
            .header("X-User-Id", masterId))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.label").value("A1 IT"))
        .andReturn();
    Number id = JsonPath.read(criado.getResponse().getContentAsString(), "$.id");

    autenticado(delete("/api/certificates/" + id)
            .header("X-User-Id", masterId)
            .header("X-Entity-Id", entityId))
        .andExpect(status().isOk());
  }
}
