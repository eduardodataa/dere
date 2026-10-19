package br.gov.dere.integration.receita;

import br.gov.dere.application.transmissao.OcorrenciaRetorno;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public final class InterpretadorRetornoReceita {
  private InterpretadorRetornoReceita() {}

  public static RetornoReceita ler(String xml) {
    if (xml == null || xml.isBlank() || !xml.contains("<")) return RetornoReceita.ilegivel();
    try {
      var fabrica = DocumentBuilderFactory.newInstance();
      fabrica.setNamespaceAware(true);
      fabrica.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
      fabrica.setFeature("http://xml.org/sax/features/external-general-entities", false);
      fabrica.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
      fabrica.setXIncludeAware(false);
      fabrica.setExpandEntityReferences(false);
      var construtor = fabrica.newDocumentBuilder();
      construtor.setErrorHandler(new org.xml.sax.helpers.DefaultHandler());
      Document documento = construtor.parse(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));
      Element raiz = documento.getDocumentElement();
      var ocorrencias = ocorrencias(raiz);
      return new RetornoReceita(
          true,
          codigoPrincipal(raiz),
          descricaoPrincipal(raiz),
          primeiro(raiz, "idTransmissao", "ideTransmissao", "identificadorTransmissao", "identificador"),
          primeiro(raiz, "protocolo", "protocoloRecepcao", "nrProt"),
          primeiro(raiz, "status", "situacao", "cdRetorno"),
          schema(raiz),
          ocorrencias);
    } catch (Exception ex) {
      return RetornoReceita.ilegivel();
    }
  }

  private static List<OcorrenciaRetorno> ocorrencias(Element raiz) {
    var lista = new ArrayList<OcorrenciaRetorno>();
    for (String nome : List.of("ocorrencia", "ocorrencias")) {
      for (Element item : elementos(raiz, nome)) {
        var codigo = textoFilho(item, "codigo");
        var descricao = textoFilho(item, "descricao");
        if (codigo == null && descricao == null) continue;
        lista.add(new OcorrenciaRetorno(codigo, descricao, textoFilho(item, "tipo"), textoFilho(item, "localizacao")));
      }
    }
    return List.copyOf(lista);
  }

  private static String codigoPrincipal(Element raiz) {
    for (String nome : List.of("codigoMensagem", "codMensagem", "codigoErro", "codResposta", "cdResposta")) {
      var valor = primeiroForaDeOcorrencia(raiz, nome);
      if (valor != null) return valor;
    }
    for (Element item : elementos(raiz, "codigo")) {
      if (dentroDeOcorrencia(item)) continue;
      var valor = texto(item);
      if (valor != null && valor.matches("(?i)[A-Z]{2,}[0-9].*")) return valor;
    }
    return null;
  }

  private static String descricaoPrincipal(Element raiz) {
    for (String nome : List.of("descResposta", "descMensagem", "descricaoErro", "descricao")) {
      var valor = primeiroForaDeOcorrencia(raiz, nome);
      if (valor != null) return valor;
    }
    return null;
  }

  private static String primeiroForaDeOcorrencia(Element raiz, String nome) {
    for (Element item : elementos(raiz, nome)) {
      if (!dentroDeOcorrencia(item)) return texto(item);
    }
    return null;
  }

  private static String primeiro(Element raiz, String... nomes) {
    for (String nome : nomes) {
      var valor = primeiroForaDeOcorrencia(raiz, nome);
      if (valor != null) return valor;
    }
    return null;
  }

  private static String textoFilho(Element pai, String nome) {
    for (Element item : elementos(pai, nome)) {
      if (item.getParentNode() == pai) return texto(item);
    }
    return null;
  }

  private static boolean dentroDeOcorrencia(Element item) {
    Node atual = item.getParentNode();
    while (atual != null) {
      if (atual instanceof Element elemento && ("ocorrencia".equals(local(elemento)) || "ocorrencias".equals(local(elemento)))) return true;
      atual = atual.getParentNode();
    }
    return false;
  }

  private static List<Element> elementos(Element raiz, String nome) {
    var lista = new ArrayList<Element>();
    var nos = raiz.getElementsByTagNameNS("*", nome);
    for (int i = 0; i < nos.getLength(); i++) {
      if (nos.item(i) instanceof Element elemento) lista.add(elemento);
    }
    if (lista.isEmpty()) {
      var simples = raiz.getElementsByTagName(nome);
      for (int i = 0; i < simples.getLength(); i++) {
        if (simples.item(i) instanceof Element elemento) lista.add(elemento);
      }
    }
    return lista;
  }

  private static String schema(Element raiz) {
    var espaco = raiz.getNamespaceURI();
    var versao = atributo(raiz, "versao");
    if (versao == null) versao = atributo(raiz, "versaoSchema");
    if (espaco == null && versao == null) return null;
    if (espaco == null) return versao;
    if (versao == null) return espaco;
    return espaco + " " + versao;
  }

  private static String atributo(Element elemento, String nome) {
    var direto = elemento.getAttribute(nome);
    if (direto != null && !direto.isBlank()) return direto.trim();
    var atributos = elemento.getAttributes();
    for (int i = 0; i < atributos.getLength(); i++) {
      var item = atributos.item(i);
      if (nome.equals(item.getLocalName()) && item.getNodeValue() != null && !item.getNodeValue().isBlank()) {
        return item.getNodeValue().trim();
      }
    }
    return null;
  }

  private static String local(Element elemento) {
    var nome = elemento.getLocalName();
    return (nome == null ? elemento.getNodeName() : nome).toLowerCase(Locale.ROOT);
  }

  private static String texto(Node no) {
    var valor = no == null ? null : no.getTextContent();
    return valor == null || valor.isBlank() ? null : valor.trim();
  }

  public record RetornoReceita(
      boolean legivel,
      String codigo,
      String descricao,
      String identificador,
      String protocolo,
      String status,
      String schema,
      List<OcorrenciaRetorno> ocorrencias) {
    public static RetornoReceita ilegivel() {
      return new RetornoReceita(false, null, null, null, null, null, null, List.of());
    }
  }
}
