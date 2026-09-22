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
@RequestMapping("/api/validation")
public class ValidationController {
  private final ServicoValidacaoLeiaute validacao;
  private final AccessService acesso;

  public ValidationController(ServicoValidacaoLeiaute validacao, AccessService acesso) {
    this.validacao = validacao;
    this.acesso = acesso;
  }

  @PostMapping("/csv-to-xml")
  public RelatorioValidacao csv(@RequestBody String corpo, @RequestHeader(value = "X-Entity-Id", required = false) Long idEntidade) {
    return validacao.validarCsv("D-1001", corpo, "d1001.csv", cnpj(idEntidade), idEntidade);
  }

  @PostMapping("/xml-to-csv")
  public RelatorioValidacao xml(@RequestBody String corpo, @RequestHeader(value = "X-Entity-Id", required = false) Long idEntidade) {
    return validacao.validarXml("D-1001", corpo, "d1001.xml", cnpj(idEntidade), idEntidade);
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
