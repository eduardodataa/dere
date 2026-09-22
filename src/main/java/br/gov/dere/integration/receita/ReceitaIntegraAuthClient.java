package br.gov.dere.integration.receita;

import java.time.Instant;
import java.util.Base64;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

public class ReceitaIntegraAuthClient {
  private final RestClient client; private final String tokenUrl, id, secret; private volatile Token cached;
  public ReceitaIntegraAuthClient(RestClient.Builder builder,String tokenUrl,String id,String secret){this.client=builder.build();this.tokenUrl=tokenUrl;this.id=id;this.secret=secret;}
  public synchronized String token(){ if(cached!=null && cached.expiresAt().isAfter(Instant.now().plusSeconds(30))) return cached.value();
    var basic=Base64.getEncoder().encodeToString((id+":"+secret).getBytes(java.nio.charset.StandardCharsets.UTF_8)); var r=client.post().uri(tokenUrl).contentType(MediaType.APPLICATION_FORM_URLENCODED).header("Authorization","Basic "+basic).body("grant_type=client_credentials").retrieve().body(TokenResponse.class); if(r==null||r.access_token()==null) throw new IllegalStateException("Receita Integra não retornou access_token"); cached=new Token(r.access_token(),Instant.now().plusSeconds(r.expires_in())); return cached.value(); }
  public record Token(String value,Instant expiresAt){} private record TokenResponse(String access_token,long expires_in,String token_type){}
  public synchronized void invalidate(){ cached=null; }
}
