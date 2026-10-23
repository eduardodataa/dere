package br.gov.dere.it;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.gov.dere.application.access.ControleTentativaLogin;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class AuthIntegrationTest extends IntegrationTestSupport {
  @Autowired
  private ControleTentativaLogin tentativas;

  @AfterEach
  void limparTentativas() {
    tentativas.limpar();
  }

  @Test
  void loginComMasterRetornaUserIdEFlagMaster() throws Exception {
    assertLoginOk();
  }

  @Test
  void loginInvalidoRetornaErro() throws Exception {
    login("eduardo", "senha-errada").andExpect(status().isUnauthorized());
  }

  @Test
  void cincoFalhasBloqueiamOLoginENaoOOutroUsuario() throws Exception {
    for (var i = 0; i < 5; i++) login("invasor", "errada").andExpect(status().isUnauthorized());
    login("invasor", "certa").andExpect(status().isTooManyRequests())
        .andExpect(jsonPath("$.message").value(ControleTentativaLogin.MENSAGEM));
    assertLoginOk();
    login("invasor", "errada").andExpect(status().isTooManyRequests());
  }
}
