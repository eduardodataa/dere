package br.gov.dere.application.access;

import br.gov.dere.integration.notificacoes.ClienteNotificacoes;
import br.gov.dere.persistence.DereEntityEntity;
import br.gov.dere.persistence.DereEntityRepository;
import br.gov.dere.persistence.DereUserEntity;
import br.gov.dere.persistence.DereUserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AccessService {
  private final DereUserRepository users;
  private final DereEntityRepository entities;
  private final PasswordEncoder encoder;
  private final ClienteNotificacoes notificacoes;

  public AccessService(DereUserRepository users, DereEntityRepository entities, PasswordEncoder encoder, ClienteNotificacoes notificacoes) {
    this.users = users;
    this.entities = entities;
    this.encoder = encoder;
    this.notificacoes = notificacoes;
  }

  public DereUserEntity user(Long id) {
    return users.findById(id).orElseThrow();
  }

  public DereUserEntity userByLogin(String login) {
    return users.findByLogin(login).orElseThrow();
  }

  public DereEntityEntity entity(Long id) {
    return entities.findById(id).orElseThrow();
  }

  public void assertAccess(Long uid, Long eid) {
    var user = user(uid);
    if (user.isMaster()) return;
    if (user.getEntities().stream().noneMatch(item -> item.getId().equals(eid))) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Usuário não associado à entidade");
    }
  }

  public void exigirVinculo(Long userId, Long entityId) {
    if (entityId != null) assertAccess(userId, entityId);
  }

  public DereUserEntity create(String login, String password, String name) {
    return create(login, password, name, null);
  }

  public DereUserEntity create(String login, String password, String name, String email) {
    var emailNormalizado = email == null || email.isBlank() ? null : ValidadorEmail.obrigatorio(email);
    return users.save(new DereUserEntity(login, encoder.encode(password), name, emailNormalizado));
  }

  public String encode(String password) {
    return encoder.encode(password);
  }

  public boolean login(String login, String password) {
    var user = users.findByLogin(login).orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Login inválido"));
    return encoder.matches(password, user.getPasswordHash());
  }

  @Transactional
  public DereUserEntity atualizarPerfil(Long userId, String name, String currentPassword, String newPassword) {
    var user = user(userId);
    if (name == null || name.isBlank()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Informe o nome.");
    String hash = null;
    if (newPassword != null && !newPassword.isBlank()) {
      if (currentPassword == null || currentPassword.isBlank() || !encoder.matches(currentPassword, user.getPasswordHash())) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Senha atual incorreta.");
      }
      hash = encoder.encode(newPassword);
    }
    user.update(null, name.trim(), hash, user.getEmail());
    return users.save(user);
  }

  @Transactional
  public String recuperarSenha(String login, String email) {
    var nome = login == null ? "" : login.trim();
    var emailInformado = email == null ? "" : email.trim();
    if (nome.isBlank() || emailInformado.isBlank()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Informe o login e o e-mail.");
    }
    var user = users.findByLogin(nome).orElse(null);
    var emailCadastro = user == null || user.getEmail() == null ? "" : user.getEmail().trim();
    if (user == null || emailCadastro.isBlank() || !emailCadastro.equalsIgnoreCase(emailInformado)) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Login ou e-mail inválidos.");
    }
    var provisoria = GeradorSenhaProvisoria.gerar();
    user.definirSenha(encoder.encode(provisoria));
    users.save(user);
    var html = "<p>Olá, " + escapar(user.getDisplayName()) + ".</p>"
        + "<p>Foi gerada uma senha provisória para acesso à DeRE:</p>"
        + "<p><strong>" + escapar(provisoria) + "</strong></p>"
        + "<p>Após entrar, altere a senha em Perfil.</p>";
    notificacoes.enviarEmail(user.getDisplayName(), user.getEmail(), "DeRE — senha provisória", html);
    return MascararEmail.aplicar(user.getEmail());
  }

  private static String escapar(String valor) {
    if (valor == null) return "";
    return valor.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
  }
}
