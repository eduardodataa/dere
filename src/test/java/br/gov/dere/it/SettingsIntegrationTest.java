package br.gov.dere.it;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

class SettingsIntegrationTest extends IntegrationTestSupport {
  @Test
  void lerESalvarIntegracao() throws Exception {
    mockMvc.perform(get("/api/settings/integration").header("X-User-Id", masterId))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.mockEnabled").value(true));

    mockMvc.perform(put("/api/settings/integration")
            .header("X-User-Id", masterId)
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {
                  "realAdapterEnabled": false,
                  "mockEnabled": true,
                  "baseUrl": "https://api.receitafederal.gov.br/dere",
                  "tokenUrl": "https://api.receitafederal.gov.br/token",
                  "clientId": "cliente-it",
                  "clientSecret": "segredo-it",
                  "environment": "restricted-production",
                  "schemaVersion": "nota_2026_001"
                }
                """))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.clientId").value("cliente-it"))
        .andExpect(jsonPath("$.clientSecretConfigured").value(true));
  }
}
