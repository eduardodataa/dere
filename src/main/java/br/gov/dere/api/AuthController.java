package br.gov.dere.api;

import br.gov.dere.application.access.AccessService;
import br.gov.dere.application.access.ControleTentativaLogin;
import br.gov.dere.application.access.EmissorToken;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
  private final AccessService access;
  private final EmissorToken tokens;
  private final ControleTentativaLogin tentativas;

  public AuthController(AccessService access, EmissorToken tokens, ControleTentativaLogin tentativas) {
    this.access = access;
    this.tokens = tokens;
    this.tentativas = tentativas;
  }

  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody LoginRequest request, HttpServletRequest http) {
    var nome = request == null || request.login() == null ? "" : request.login().trim();
    var senha = request == null ? null : request.password();
    var ip = http.getRemoteAddr();
    if (tentativas.bloqueado(nome, ip)) {
      return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(Map.of("message", ControleTentativaLogin.MENSAGEM));
    }
    if (nome.isBlank() || senha == null) {
      tentativas.registrarFalha(nome, ip);
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Login inválido");
    }
    boolean ok;
    try {
      ok = access.login(nome, senha);
    } catch (ResponseStatusException ex) {
      if (ex.getStatusCode().value() == HttpStatus.UNAUTHORIZED.value()) tentativas.registrarFalha(nome, ip);
      throw ex;
    }
    if (!ok) {
      tentativas.registrarFalha(nome, ip);
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Login inválido");
    }
    tentativas.registrarSucesso(nome, ip);
    var user = access.userByLogin(nome);
    return ResponseEntity.ok(new LoginResponse(user.getId(), user.getDisplayName(), user.isMaster(), tokens.emitir(user.getId())));
  }

  public record LoginRequest(String login, String password) {}
  public record LoginResponse(Long userId, String name, boolean master, String token) {}
}
