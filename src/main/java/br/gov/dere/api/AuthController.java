package br.gov.dere.api;

import br.gov.dere.application.access.AccessService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
  private final AccessService access;

  public AuthController(AccessService access) {
    this.access = access;
  }

  @PostMapping("/login")
  public LoginResponse login(@RequestBody LoginRequest request) {
    if (!access.login(request.login(), request.password())) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Login inválido");
    }
    var user = access.userByLogin(request.login());
    return new LoginResponse(user.getId(), user.getDisplayName(), user.isMaster());
  }

  public record LoginRequest(String login, String password) {}
  public record LoginResponse(Long userId, String name, boolean master) {}
}
