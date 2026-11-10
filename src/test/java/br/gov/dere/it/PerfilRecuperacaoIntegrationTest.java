package br.gov.dere.it;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.gov.dere.integration.notificacoes.ClienteNotificacoes;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;

@Import(PerfilRecuperacaoIntegrationTest.NotificacoesFake.class)
class PerfilRecuperacaoIntegrationTest extends IntegrationTestSupport {
  @Test
  void perfilAtualizaNomeESenha() throws Exception {
    autenticado(get("/api/auth/perfil"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.login").value("eduardo"))
        .andExpect(jsonPath("$.email").value("eduardo@example.com"));

    autenticado(put("/api/auth/perfil")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"name\":\"Eduardo Perfil\",\"currentPassword\":\"eduardo86\",\"newPassword\":\"novaSenha99\"}"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("Eduardo Perfil"));

    mockMvc.perform(post("/api/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"login\":\"eduardo\",\"password\":\"novaSenha99\"}"))
        .andExpect(status().isOk());
  }

  @Test
  void recuperarSenhaExigeLoginEEmailCorretos() throws Exception {
    mockMvc.perform(post("/api/auth/recuperar-senha")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"login\":\"eduardo\",\"email\":\"errado@example.com\"}"))
        .andExpect(status().isBadRequest());

    mockMvc.perform(post("/api/auth/recuperar-senha")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"login\":\"eduardo\",\"email\":\"eduardo@example.com\"}"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.emailMascarado").value("edu*******@example.com"));
  }

  @TestConfiguration
  static class NotificacoesFake {
    @Bean
    @Primary
    ClienteNotificacoes clienteNotificacoes() {
      return (paraNome, paraEmail, assunto, html) -> {};
    }
  }
}
