package br.gov.dere.api;

import br.gov.dere.application.access.AccessService;
import br.gov.dere.application.validation.ServicoValidacaoLeiaute;
import br.gov.dere.application.validation.relatorio.RelatorioValidacao;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/layouts/d1011")
public class D1011ValidationController {
  private final ServicoValidacaoLeiaute validacao;
  private final AccessService acesso;

  public D1011ValidationController(ServicoValidacaoLeiaute validacao, AccessService acesso) {
    this.validacao = validacao;
    this.acesso = acesso;
  }

  @PostMapping(value = "/validate-csv", consumes = "text/csv")
  public RelatorioValidacao validarCsv(@RequestBody String corpo, @RequestHeader(value = "X-Entity-Id", required = false) Long idEntidade) {
    return validacao.validarCsv("D-1011", corpo, "d1011.csv", cnpj(idEntidade), idEntidade);
  }

  @PostMapping(value = "/validate-xml", consumes = "application/xml")
  public RelatorioValidacao validarXml(@RequestBody String corpo, @RequestHeader(value = "X-Entity-Id", required = false) Long idEntidade) {
    return validacao.validarXml("D-1011", corpo, "d1011.xml", cnpj(idEntidade), idEntidade);
  }

  @PostMapping(value = "/xml-to-csv", consumes = "application/xml")
  public RelatorioValidacao xmlParaCsv(@RequestBody String corpo, @RequestHeader(value = "X-Entity-Id", required = false) Long idEntidade) {
    return validarXml(corpo, idEntidade);
  }

  private String cnpj(Long idEntidade) {
    if (idEntidade == null) return null;
    try {
      return acesso.entity(idEntidade).getCnpjRoot();
    } catch (Exception ex) {
      return null;
    }
  }
}
