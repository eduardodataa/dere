package br.gov.dere.it;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

class AdministrationIntegrationTest extends IntegrationTestSupport {
  @Test
  void usuariosCrud() throws Exception {
    autenticado(get("/api/admin/users").header("X-User-Id", masterId))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].login").value("eduardo"));

    var criado = autenticado(post("/api/admin/users")
            .header("X-User-Id", masterId)
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"login\":\"operador\",\"name\":\"Operador\",\"password\":\"segredo\",\"master\":false}"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.login").value("operador"))
        .andReturn();
    var id = com.jayway.jsonpath.JsonPath.read(criado.getResponse().getContentAsString(), "$.id");

    autenticado(put("/api/admin/users/" + id)
            .header("X-User-Id", masterId)
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"login\":\"operador\",\"name\":\"Operador 2\",\"password\":\"\",\"master\":false}"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("Operador 2"));

    autenticado(delete("/api/admin/users/" + id).header("X-User-Id", masterId))
        .andExpect(status().isOk());
  }

  @Test
  void entidadesListarEAtualizar() throws Exception {
    autenticado(get("/api/admin/entities").header("X-User-Id", masterId))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].cnpjRoot").value("01914904"));

    autenticado(put("/api/admin/entities/" + entityId)
            .header("X-User-Id", masterId)
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"cnpjRoot\":\"01914904\",\"legalName\":\"Entidade IT Atualizada\",\"userIds\":[" + masterId + "]}"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.legalName").value("Entidade IT Atualizada"));
  }
}
