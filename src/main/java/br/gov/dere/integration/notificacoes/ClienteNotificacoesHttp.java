package br.gov.dere.integration.notificacoes;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.server.ResponseStatusException;

@Component
public class ClienteNotificacoesHttp implements ClienteNotificacoes {
  private final RestClient client;
  private final String baseUrl;
  private final String username;
  private final String password;
  private final String fromName;
  private final String fromEmail;

  public ClienteNotificacoesHttp(
      RestClient.Builder builder,
      @Value("${notificacoes.url:}") String baseUrl,
      @Value("${notificacoes.api.username:}") String username,
      @Value("${notificacoes.api.password:}") String password,
      @Value("${dere.mail.from-name:DeRE}") String fromName,
      @Value("${dere.mail.from-email:noreply@dataa.com.br}") String fromEmail) {
    this.client = builder.build();
    this.baseUrl = baseUrl == null ? "" : baseUrl.trim().replaceAll("/+$", "");
    this.username = username == null ? "" : username.trim();
    this.password = password == null ? "" : password;
    this.fromName = fromName;
    this.fromEmail = fromEmail;
  }

  @Override
  public void enviarEmail(String paraNome, String paraEmail, String assunto, String html) {
    if (baseUrl.isBlank() || username.isBlank() || password.isBlank()) {
      throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de e-mail não configurado.");
    }
    var payload = Map.of(
        "from", Map.of("name", fromName, "email", fromEmail),
        "to", Map.of("name", paraNome == null || paraNome.isBlank() ? paraEmail : paraNome, "email", paraEmail),
        "contentType", "text/html",
        "content", html,
        "templateName", "",
        "subject", assunto,
        "clientName", "DeRE",
        "clientReference", "email=" + paraEmail);
    try {
      client.post()
          .uri(baseUrl + "/email/sender")
          .header(HttpHeaders.AUTHORIZATION, "Basic " + Base64.getEncoder().encodeToString((username + ":" + password).getBytes(StandardCharsets.UTF_8)))
          .contentType(MediaType.APPLICATION_JSON)
          .body(payload)
          .retrieve()
          .toBodilessEntity();
    } catch (ResponseStatusException ex) {
      throw ex;
    } catch (Exception ex) {
      throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Falha ao enviar o e-mail.", ex);
    }
  }
}
