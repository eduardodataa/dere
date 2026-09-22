package br.gov.dere.integration.receita;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;
import java.nio.charset.StandardCharsets;
public class DereTransmissionClient { private final RestClient client; private final ReceitaIntegraAuthClient auth; private final String baseUrl;
  public DereTransmissionClient(RestClient.Builder b,ReceitaIntegraAuthClient a,String baseUrl){client=b.build();auth=a;this.baseUrl=baseUrl;}
  public GovernmentHttpResponse transmit(String batchXml,String idempotencyKey){var request=client.post().uri(baseUrl+"/v1/recepcao/lotes").contentType(MediaType.APPLICATION_XML).header("Authorization","Bearer "+auth.token()); if(idempotencyKey!=null&&!idempotencyKey.isBlank()) request.header("Idempotency-Key",idempotencyKey); return request.body(batchXml).exchange((req,response)->new GovernmentHttpResponse(response.getStatusCode().value(),read(response)));}
  public GovernmentHttpResponse query(String protocol){return client.get().uri(baseUrl+"/v1/consulta/lotes/"+protocol).header("Authorization","Bearer "+auth.token()).exchange((request,response)->new GovernmentHttpResponse(response.getStatusCode().value(),read(response)));}
  public void invalidateToken(){ auth.invalidate(); }
  private static String read(org.springframework.http.client.ClientHttpResponse response) throws java.io.IOException { return response.getBody()==null?"":new String(response.getBody().readAllBytes(), StandardCharsets.UTF_8); }
}
