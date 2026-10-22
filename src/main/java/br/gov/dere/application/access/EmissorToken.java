package br.gov.dere.application.access;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.Base64;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class EmissorToken {
  private static final long VALIDADE_SEGUNDOS = 12 * 60 * 60;
  private final byte[] chave;

  public EmissorToken(@Value("${dere.certificate.encryption-key}") String segredo) {
    if (segredo == null || segredo.isBlank()) throw new IllegalStateException("Chave de assinatura do token ausente");
    chave = segredo.getBytes(StandardCharsets.UTF_8);
  }

  public String emitir(Long userId) {
    var corpo = userId + "." + (Instant.now().getEpochSecond() + VALIDADE_SEGUNDOS);
    return corpo + "." + assinar(corpo);
  }

  public Long usuario(String token) {
    if (token == null || token.isBlank()) throw invalido();
    var partes = token.split("\\.");
    if (partes.length != 3) throw invalido();
    var corpo = partes[0] + "." + partes[1];
    var esperado = assinar(corpo);
    if (!MessageDigest.isEqual(esperado.getBytes(StandardCharsets.UTF_8), partes[2].getBytes(StandardCharsets.UTF_8))) throw invalido();
    try {
      var userId = Long.parseLong(partes[0]);
      var expira = Long.parseLong(partes[1]);
      if (Instant.now().getEpochSecond() > expira) throw invalido();
      return userId;
    } catch (NumberFormatException ex) {
      throw invalido();
    }
  }

  private String assinar(String corpo) {
    try {
      var mac = Mac.getInstance("HmacSHA256");
      mac.init(new SecretKeySpec(chave, "HmacSHA256"));
      return Base64.getUrlEncoder().withoutPadding().encodeToString(mac.doFinal(corpo.getBytes(StandardCharsets.UTF_8)));
    } catch (Exception ex) {
      throw new IllegalStateException("Não foi possível assinar o token", ex);
    }
  }

  private static ResponseStatusException invalido() {
    return new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Sessão inválida ou expirada.");
  }
}
