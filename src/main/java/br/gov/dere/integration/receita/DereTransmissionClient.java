package br.gov.dere.integration.receita;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;
public class DereTransmissionClient { private final RestClient client; private final ReceitaIntegraAuthClient auth; private final String baseUrl;
  public DereTransmissionClient(RestClient.Builder b,ReceitaIntegraAuthClient a,String baseUrl){client=b.build();auth=a;this.baseUrl=baseUrl;}
  public String transmit(String batchXml){return client.post().uri(baseUrl+"/v1/recepcao/lotes").contentType(MediaType.APPLICATION_XML).header("Authorization","Bearer "+auth.token()).body(batchXml).retrieve().body(String.class);}
  public String query(String protocol){return client.get().uri(baseUrl+"/v1/consulta/lotes/"+protocol).header("Authorization","Bearer "+auth.token()).retrieve().body(String.class);}
}
