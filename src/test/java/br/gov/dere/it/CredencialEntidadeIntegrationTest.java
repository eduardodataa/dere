package br.gov.dere.it;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

class CredencialEntidadeIntegrationTest extends IntegrationTestSupport {
  @Test
  void operadorDaEntidadePodeLerESalvarCredenciais() throws Exception {
    var operador = access.create("operador.cred", "senha123", "Operador Cred", "operador.cred@example.com");
    operador.setMaster(false);
    operador.getEntities().add(entities.findById(entityId).orElseThrow());
    operador = users.save(operador);
    var operadorId = operador.getId();
    var token = tokens.emitir(operadorId);

    mockMvc.perform(get("/api/entities/" + entityId + "/credenciais")
            .header("Authorization", "Bearer " + token)
            .header("X-User-Id", operadorId))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.entityId").value(entityId.intValue()))
        .andExpect(jsonPath("$.clientSecretConfigured").value(false));

    mockMvc.perform(put("/api/entities/" + entityId + "/credenciais")
            .header("Authorization", "Bearer " + token)
            .header("X-User-Id", operadorId)
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {"clientId":"client-it","clientSecret":"secret-it","validadeCredencial":"2026-12-31"}
                """))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.clientId").value("client-it"))
        .andExpect(jsonPath("$.clientSecretConfigured").value(true))
        .andExpect(jsonPath("$.validadeCredencial").value("2026-12-31"));

    mockMvc.perform(get("/api/entities/" + entityId + "/credenciais")
            .header("Authorization", "Bearer " + token)
            .header("X-User-Id", operadorId))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.clientId").value("client-it"))
        .andExpect(jsonPath("$.clientSecretConfigured").value(true));
  }
}
