package br.gov.dere.it;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;

class AuthIntegrationTest extends IntegrationTestSupport {
  @Test
  void loginComMasterRetornaUserIdEFlagMaster() throws Exception {
    assertLoginOk();
  }

  @Test
  void loginInvalidoRetornaErro() throws Exception {
    login("eduardo", "senha-errada").andExpect(status().is4xxClientError());
  }
}
