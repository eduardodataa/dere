package br.gov.dere.application.transmission;

import br.gov.dere.integration.xml.XmlSupport;
import java.time.Instant;
import java.time.LocalDate;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public final class OfficialReturnXmlBuilder {
  public static final String NS_LOTE = "http://www.dere.gov.br/schemas/retornoLoteDere/v1_0_1";
  public static final String NS_TABELA = "http://www.dere.gov.br/schemas/evtRetornoTabela/v1_0_1";

  public String recepcao(String cnpj, String transmissor, String protocolo, Instant recebidoEm) throws Exception {
    var dom = documento(NS_LOTE);
    var lote = elemento(dom, dom.getDocumentElement(), NS_LOTE, "retornoLoteEventos");
    lote.setAttribute("id", idLote(protocolo));
    contribuinte(dom, lote, NS_LOTE, cnpj);
    transmissor(dom, lote, transmissor);
    status(dom, lote, 1, "O lote esta aguardando processamento");
    var recepcao = elemento(dom, lote, NS_LOTE, "dadosRecepcaoLote");
    texto(dom, recepcao, NS_LOTE, "dhRecepcao", recebidoEm.toString());
    texto(dom, recepcao, NS_LOTE, "versaoAplicativoRecepcao", "dere-poc/0.1");
    texto(dom, recepcao, NS_LOTE, "protocolo", protocolo);
    return XmlSupport.serialize(dom);
  }

  public String eventoTabela(String eventId, String cnpj, String layout, String protocolo, String recibo, String hash,
      Instant recebidoEm, Instant processadoEm, LocalDate iniValid, LocalDate fimValid) throws Exception {
    var dom = documento(NS_TABELA);
    var evento = elemento(dom, dom.getDocumentElement(), NS_TABELA, "evtRetornoTabela");
    evento.setAttribute("id", eventId);
    contribuinte(dom, evento, NS_TABELA, cnpj);
    var ideStatus = elemento(dom, evento, NS_TABELA, "ideStatus");
    texto(dom, ideStatus, NS_TABELA, "cdRetorno", "1");
    texto(dom, ideStatus, NS_TABELA, "descRetorno", "Sucesso");
    var rec = elemento(dom, evento, NS_TABELA, "infoRecEv");
    texto(dom, rec, NS_TABELA, "nrRecibo", recibo);
    texto(dom, rec, NS_TABELA, "protocoloLote", protocolo);
    texto(dom, rec, NS_TABELA, "dhRecepcao", recebidoEm.toString());
    texto(dom, rec, NS_TABELA, "dhProcess", processadoEm.toString());
    texto(dom, rec, NS_TABELA, "tpEv", layout);
    texto(dom, rec, NS_TABELA, "hash", hash);
    var info = elemento(dom, evento, NS_TABELA, "infoEvento");
    var periodo = elemento(dom, info, NS_TABELA, "idePeriodo");
    texto(dom, periodo, NS_TABELA, "iniValid", iniValid.toString());
    if (fimValid != null) texto(dom, periodo, NS_TABELA, "fimValid", fimValid.toString());
    var extrato = elemento(dom, evento, NS_TABELA, "extratoEventos");
    var det = elemento(dom, extrato, NS_TABELA, "detEvento");
    texto(dom, det, NS_TABELA, "nrRecibo", recibo);
    texto(dom, det, NS_TABELA, "iniValid", iniValid.toString());
    if (fimValid != null) texto(dom, det, NS_TABELA, "fimValid", fimValid.toString());
    texto(dom, det, NS_TABELA, "indAjusteAuto", "0");
    return XmlSupport.serialize(dom);
  }

  public String consulta(String cnpj, String transmissor, String protocolo, Instant recebidoEm, Instant processadoEm,
      String eventoRetornoAssinado) throws Exception {
    var dom = documento(NS_LOTE);
    var lote = elemento(dom, dom.getDocumentElement(), NS_LOTE, "retornoLoteEventos");
    lote.setAttribute("id", idLote(protocolo));
    contribuinte(dom, lote, NS_LOTE, cnpj);
    transmissor(dom, lote, transmissor);
    status(dom, lote, 2, "Lote processado com sucesso - Todos eventos processados com sucesso");
    var recepcao = elemento(dom, lote, NS_LOTE, "dadosRecepcaoLote");
    texto(dom, recepcao, NS_LOTE, "dhRecepcao", recebidoEm.toString());
    texto(dom, recepcao, NS_LOTE, "versaoAplicativoRecepcao", "dere-poc/0.1");
    texto(dom, recepcao, NS_LOTE, "protocolo", protocolo);
    var proc = elemento(dom, lote, NS_LOTE, "dadosProcessamentoLote");
    texto(dom, proc, NS_LOTE, "dhProcessamento", processadoEm.toString());
    texto(dom, proc, NS_LOTE, "versaoAplicativoProcessamento", "dere-poc/0.1");
    var retornos = elemento(dom, lote, NS_LOTE, "retornoEventos");
    var item = elemento(dom, retornos, NS_LOTE, "evento");
    var interno = XmlSupport.parse(eventoRetornoAssinado);
    var evt = primeiroElemento(interno.getDocumentElement());
    item.setAttribute("id", evt.getAttribute("id"));
    item.appendChild(dom.importNode(interno.getDocumentElement(), true));
    return XmlSupport.serialize(dom);
  }

  public static String idLote(String protocolo) {
    return "ID" + protocolo;
  }

  private static Document documento(String ns) throws Exception {
    var fabrica = DocumentBuilderFactory.newInstance();
    fabrica.setNamespaceAware(true);
    var dom = fabrica.newDocumentBuilder().newDocument();
    dom.appendChild(dom.createElementNS(ns, "DeRE"));
    return dom;
  }

  private static void contribuinte(Document dom, Node pai, String ns, String cnpj) {
    var grupo = elemento(dom, pai, ns, "ideContrib");
    texto(dom, grupo, ns, "nrInsc", cnpj);
  }

  private static void transmissor(Document dom, Node pai, String ni) {
    var grupo = elemento(dom, pai, NS_LOTE, "ideTransmissor");
    texto(dom, grupo, NS_LOTE, "niTransmissor", ni);
  }

  private static void status(Document dom, Node pai, int codigo, String descricao) {
    var grupo = elemento(dom, pai, NS_LOTE, "status");
    texto(dom, grupo, NS_LOTE, "cdResposta", String.valueOf(codigo));
    texto(dom, grupo, NS_LOTE, "descResposta", descricao);
  }

  private static Element elemento(Document dom, Node pai, String ns, String nome) {
    var no = dom.createElementNS(ns, nome);
    pai.appendChild(no);
    return no;
  }

  private static void texto(Document dom, Node pai, String ns, String nome, String valor) {
    elemento(dom, pai, ns, nome).setTextContent(valor);
  }

  private static Element primeiroElemento(Node raiz) {
    for (Node no = raiz.getFirstChild(); no != null; no = no.getNextSibling()) {
      if (no.getNodeType() == Node.ELEMENT_NODE) return (Element) no;
    }
    throw new IllegalStateException("XML sem elemento de evento");
  }
}
