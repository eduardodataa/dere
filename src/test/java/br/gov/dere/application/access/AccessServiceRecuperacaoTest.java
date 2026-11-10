package br.gov.dere.application.access;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.gov.dere.integration.notificacoes.ClienteNotificacoes;
import br.gov.dere.persistence.DereEntityRepository;
import br.gov.dere.persistence.DereUserEntity;
import br.gov.dere.persistence.DereUserRepository;
import java.util.Optional;
import java.util.regex.Pattern;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

class AccessServiceRecuperacaoTest {
  private DereUserRepository users;
  private ClienteNotificacoes notificacoes;
  private AccessService access;
  private BCryptPasswordEncoder encoder;

  @BeforeEach
  void setup() {
    users = mock(DereUserRepository.class);
    notificacoes = mock(ClienteNotificacoes.class);
    encoder = new BCryptPasswordEncoder();
    access = new AccessService(users, mock(DereEntityRepository.class), encoder, notificacoes);
  }

  @Test
  void rejeitaLoginOuEmailInvalidos() {
    when(users.findByLogin("eduardo")).thenReturn(Optional.empty());
    var erro = assertThrows(ResponseStatusException.class, () -> access.recuperarSenha("eduardo", "a@b.com"));
    assertEquals(400, erro.getStatusCode().value());
    assertTrue(erro.getReason().contains("Login ou e-mail"));
  }

  @Test
  void rejeitaEmailDivergente() {
    var user = new DereUserEntity("eduardo", encoder.encode("antiga"), "Eduardo", "certo@example.com");
    when(users.findByLogin("eduardo")).thenReturn(Optional.of(user));
    var erro = assertThrows(ResponseStatusException.class, () -> access.recuperarSenha("eduardo", "errado@example.com"));
    assertEquals(400, erro.getStatusCode().value());
  }

  @Test
  void geraSenhaProvisoriaEnviaEmailEMascara() {
    var user = new DereUserEntity("eduardo", encoder.encode("antiga"), "Eduardo", "duducordeiro_@hotmail.com");
    when(users.findByLogin("eduardo")).thenReturn(Optional.of(user));
    when(users.save(user)).thenReturn(user);
    var mascarado = access.recuperarSenha("eduardo", "duducordeiro_@hotmail.com");
    assertEquals("dud*******@hotmail.com", mascarado);
    var html = ArgumentCaptor.forClass(String.class);
    verify(notificacoes).enviarEmail(eq("Eduardo"), eq("duducordeiro_@hotmail.com"), eq("DeRE — senha provisória"), html.capture());
    var matcher = Pattern.compile("<strong>([^<]+)</strong>").matcher(html.getValue());
    assertTrue(matcher.find());
    assertTrue(encoder.matches(matcher.group(1), user.getPasswordHash()));
  }
}
