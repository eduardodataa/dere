package br.gov.dere.api;

import br.gov.dere.application.access.AccessService;
import br.gov.dere.application.validacao.ServicoValidacaoLeiaute;
import br.gov.dere.application.validacao.relatorio.RelatorioValidacao;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/layouts/d1101")
public class D1101ValidacaoController {
  private final ServicoValidacaoLeiaute validacao;
  private final AccessService acesso;

  public D1101ValidacaoController(ServicoValidacaoLeiaute validacao, AccessService acesso) {
    this.validacao = validacao;
    this.acesso = acesso;
  }

  @PostMapping(value = "/validate-csv", consumes = "text/csv")
  public RelatorioValidacao validarCsv(
      @RequestBody String corpo,
      @RequestHeader("X-User-Id") Long userId,
      @RequestHeader(value = "X-Entity-Id", required = false) Long idEntidade) {
    acesso.exigirVinculo(userId, idEntidade);
    return validacao.validarCsv("D-1101", corpo, "d1101.csv", cnpj(idEntidade), idEntidade);
  }

  @PostMapping(value = "/validate-xml", consumes = "application/xml")
  public RelatorioValidacao validarXml(
      @RequestBody String corpo,
      @RequestHeader("X-User-Id") Long userId,
      @RequestHeader(value = "X-Entity-Id", required = false) Long idEntidade) {
    acesso.exigirVinculo(userId, idEntidade);
    return validacao.validarXml("D-1101", corpo, "d1101.xml", cnpj(idEntidade), idEntidade);
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
