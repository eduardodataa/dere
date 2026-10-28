package br.gov.dere.application.transmissao;

import br.gov.dere.integration.receita.ExcecaoTransmissaoGoverno;
import br.gov.dere.integration.receita.InterpretadorRetornoReceita;
import br.gov.dere.integration.receita.InterpretadorRetornoReceita.RetornoReceita;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;

public final class ApresentacaoErroTransmissao {
  private static final Logger log = LoggerFactory.getLogger(ApresentacaoErroTransmissao.class);
  private static final Pattern SEGREDO = Pattern.compile("(?i)(senha|password|token|secret|authorization)\\s*[:=]\\s*\\S+");
  private static final int LIMITE_LOG = 8000;

  private ApresentacaoErroTransmissao() {}

  public static DiagnosticoTransmissao deGoverno(ExcecaoTransmissaoGoverno ex, String identificadorEvento) {
    var retorno = InterpretadorRetornoReceita.ler(ex.responseBody());
    var correlacao = UUID.randomUUID().toString();
    int http = ex.statusCode();
    if (http == 401 || http == 403) return autenticacao(http, correlacao, retorno, identificadorEvento);
    if (http == 408 || indicaTimeout(ex.responseBody())) return comunicacao(http, correlacao, retorno, identificadorEvento, true);
    if (respostaTransiente(http)) return comunicacao(http, correlacao, retorno, identificadorEvento, false);
    if (http >= 500) return comunicacao(http, correlacao, retorno, identificadorEvento, true);
    return rejeicao(http, correlacao, retorno, identificadorEvento);
  }

  public static DiagnosticoTransmissao resultadoDesconhecido(ExcecaoTransmissaoGoverno ex, String identificadorEvento) {
    var base = deGoverno(ex, identificadorEvento);
    return new DiagnosticoTransmissao(
        "COMUNICACAO",
        "O resultado do envio ficou desconhecido",
        "A Receita Federal pode ter recebido o lote, mas o protocolo não foi confirmado nesta tela.",
        "Não reenvie este identificador. Concilie pelo protocolo, se a Receita o tiver emitido.",
        base.codigo(),
        base.descricaoOriginal(),
        base.http(),
        identificadorEvento,
        base.protocolo(),
        base.status(),
        base.schema(),
        base.ocorrencias(),
        base.correlacao(),
        false,
        true,
        base.httpAplicacao());
  }

  public static DiagnosticoTransmissao deConsulta(String protocolo, ExcecaoTransmissaoGoverno ex) {
    var base = deGoverno(ex, null);
    var mensagem = ex.statusCode() == 404
        ? "A Receita Federal não encontrou o protocolo informado."
        : base.mensagem();
    return new DiagnosticoTransmissao(
        base.categoria(),
        "Não foi possível consultar o protocolo",
        mensagem,
        "Use o identificador de correlação ao falar com o suporte. Não reenvie a declaração por causa desta consulta.",
        base.codigo(),
        base.descricaoOriginal(),
        base.http(),
        base.identificador(),
        protocolo,
        base.status(),
        base.schema(),
        base.ocorrencias(),
        base.correlacao(),
        false,
        base.inconclusivo(),
        base.httpAplicacao());
  }

  public static DiagnosticoTransmissao deFalhaLocal(Exception ex, String identificadorEvento) {
    var correlacao = UUID.randomUUID().toString();
    var texto = textoCausa(ex);
    if (contem(texto, "certificado", "keystore", "xmldsig", "assinatura", "pkcs")) {
      return montar("CERTIFICADO", "Não foi possível assinar a declaração",
          "O certificado digital não pôde ser usado neste envio.",
          "Revise o certificado da entidade com o suporte antes de transmitir novamente.",
          null, resumir(texto), null, identificadorEvento, null, null, null, List.of(), correlacao, false, false, 422);
    }
    if (contem(texto, "duplicate entry", "uk_transmission_attempt", "constraintviolation")) {
      return montar("COMUNICACAO", "Não foi possível confirmar o registro do envio",
          "O envio pode ter chegado à Receita Federal, mas o retorno não foi gravado.",
          "Não envie a declaração outra vez até confirmar o protocolo. Informe a correlação ao suporte.",
          null, "Falha ao gravar a tentativa local.", null, identificadorEvento, null, null, null, List.of(), correlacao, false, true, 504);
    }
    if (contem(texto, "erro ao gerar token", "não retornou access_token", "nao retornou access_token")) {
      return naoTransmitido(ex, identificadorEvento);
    }
    if (contem(texto, "crítica", "critica", "leiaute", "id do evento", "cnpj")) {
      return montar("VALIDACAO_LOCAL", "A declaração não passou na validação local",
          "O arquivo precisa ser corrigido antes do envio.",
          "A validação local não significa que a Receita Federal aceitou a declaração.",
          null, resumir(texto), null, identificadorEvento, null, null, null, List.of(), correlacao, false, false, 422);
    }
    return montar("COMUNICACAO", "Não foi possível concluir o envio",
        "A declaração não foi confirmada pela Receita Federal.",
        "Não repita o envio enquanto o resultado estiver indefinido. Informe a correlação ao suporte.",
        null, resumir(texto), null, identificadorEvento, null, null, null, List.of(), correlacao, false, true, 502);
  }

  public static DiagnosticoTransmissao naoTransmitido(Throwable ex, String identificadorEvento) {
    var correlacao = UUID.randomUUID().toString();
    var texto = textoCausa(ex);
    var token = contem(texto, "erro ao gerar token", "não retornou access_token", "nao retornou access_token", "access_token");
    return montar(token ? "AUTENTICACAO" : "COMUNICACAO",
        token ? "A Receita não entregou o token de acesso" : "O lote não foi enviado",
        "O lote não foi enviado. A falha ocorreu antes da transmissão do XML.",
        "O mesmo identificador será reenviado quando a Receita voltar. Não crie outro id.",
        null, resumir(texto), null, identificadorEvento, null, null, null, List.of(), correlacao, false, false, 502);
  }

  public static void registrar(DiagnosticoTransmissao diagnostico, String xml) {
    log.warn(
        "correlacao={} categoria={} http={} codigo={} identificador={} protocolo={} status={} schema={} retorno={}",
        diagnostico.correlacao(),
        diagnostico.categoria(),
        diagnostico.http(),
        diagnostico.codigo(),
        diagnostico.identificador(),
        diagnostico.protocolo(),
        diagnostico.status(),
        diagnostico.schema(),
        sanitizar(xml));
  }

  static String sanitizar(String xml) {
    if (xml == null || xml.isBlank()) return "";
    var texto = SEGREDO.matcher(xml).replaceAll("$1=[omitido]");
    if (texto.contains("<")) texto = redactarElementos(texto);
    if (texto.length() > LIMITE_LOG) return texto.substring(0, LIMITE_LOG);
    return texto;
  }

  private static DiagnosticoTransmissao rejeicao(int http, String correlacao, RetornoReceita retorno, String identificadorEvento) {
    var codigo = retorno.codigo();
    var ms0015 = codigo != null && codigo.equalsIgnoreCase("MS0015");
    var titulo = "Não foi possível transmitir a declaração";
    var mensagem = ms0015 ? "A Receita Federal não recebeu o lote." : "A Receita Federal recusou o envio.";
    var orientacao = ms0015
        ? "O arquivo passou pela validação local, mas o envio foi recusado. Consulte os detalhes do retorno antes de tentar novamente."
        : "Consulte os detalhes técnicos e encaminhe o diagnóstico ao suporte antes de um novo envio.";
    var descricao = retorno.legivel() ? retorno.descricao() : "O retorno não pôde ser interpretado.";
    return montar("REJEICAO_RECEITA", titulo, mensagem, orientacao, codigo, descricao, http,
        primeiroPreenchido(retorno.identificador(), identificadorEvento), retorno.protocolo(), retorno.status(), retorno.schema(),
        retorno.ocorrencias(), correlacao, false, false, 422);
  }

  private static DiagnosticoTransmissao autenticacao(int http, String correlacao, RetornoReceita retorno, String identificadorEvento) {
    var mensagem = http == 403
        ? "A Receita Federal recusou o acesso neste ambiente."
        : "As credenciais de integração foram recusadas.";
    return montar("AUTENTICACAO", "Não foi possível autenticar na Receita Federal", mensagem,
        "Verifique o certificado e as credenciais com o suporte. Não reenvie a declaração até a autenticação ser corrigida.",
        retorno.codigo(), retorno.descricao(), http, primeiroPreenchido(retorno.identificador(), identificadorEvento),
        retorno.protocolo(), retorno.status(), retorno.schema(), retorno.ocorrencias(), correlacao, false, false, 401);
  }

  private static DiagnosticoTransmissao comunicacao(int http, String correlacao, RetornoReceita retorno, String identificadorEvento, boolean inconclusivo) {
    var titulo = inconclusivo ? "Não foi possível confirmar o envio" : "A Receita Federal está indisponível";
    var mensagem = inconclusivo
        ? "A comunicação falhou antes de uma confirmação da Receita Federal."
        : "A Receita Federal não concluiu o recebimento neste momento.";
    var orientacao = inconclusivo
        ? "O resultado da transmissão é desconhecido. Não envie de novo até confirmar se o lote foi recebido."
        : "Aguarde alguns instantes e confirme o protocolo antes de um novo envio.";
    return montar("COMUNICACAO", titulo, mensagem, orientacao, retorno.codigo(), retorno.descricao(), http == 0 ? null : http,
        primeiroPreenchido(retorno.identificador(), identificadorEvento), retorno.protocolo(), retorno.status(), retorno.schema(),
        retorno.ocorrencias(), correlacao, !inconclusivo && respostaTransiente(http), inconclusivo, inconclusivo ? 504 : 502);
  }

  private static DiagnosticoTransmissao montar(
      String categoria, String titulo, String mensagem, String orientacao, String codigo, String descricao, Integer http,
      String identificador, String protocolo, String status, String schema, List<OcorrenciaRetorno> ocorrencias,
      String correlacao, boolean repetir, boolean inconclusivo, int httpAplicacao) {
    return new DiagnosticoTransmissao(categoria, titulo, mensagem, orientacao, codigo, limitar(descricao), http,
        identificador, protocolo, status, schema, ocorrencias == null ? List.of() : ocorrencias, correlacao, repetir, inconclusivo, httpAplicacao);
  }

  private static boolean respostaTransiente(int http) {
    return http == 429 || http == 503;
  }

  private static boolean indicaTimeout(String corpo) {
    if (corpo == null) return false;
    var texto = corpo.toLowerCase(Locale.ROOT);
    return texto.contains("timeout") || texto.contains("timed out") || texto.contains("read timed out");
  }

  private static String textoCausa(Throwable ex) {
    var texto = new StringBuilder();
    for (Throwable atual = ex; atual != null; atual = atual.getCause()) {
      if (atual.getMessage() != null) texto.append(' ').append(atual.getMessage());
    }
    return texto.toString();
  }

  private static boolean contem(String texto, String... partes) {
    var base = texto.toLowerCase(Locale.ROOT);
    for (String parte : partes) {
      if (base.contains(parte)) return true;
    }
    return false;
  }

  private static String resumir(String texto) {
    var limpo = texto.replaceAll("\\s+", " ").trim();
    if (limpo.contains("<")) return "Falha interna ao preparar o envio.";
    return limitar(limpo);
  }

  private static String limitar(String texto) {
    if (texto == null || texto.isBlank()) return null;
    var limpo = texto.replaceAll("\\s+", " ").trim();
    return limpo.length() > 500 ? limpo.substring(0, 500) : limpo;
  }

  private static String primeiroPreenchido(String principal, String reserva) {
    if (principal != null && !principal.isBlank()) return principal;
    if (reserva != null && !reserva.isBlank()) return reserva;
    return null;
  }

  private static String redactarElementos(String xml) {
    try {
      var fabrica = javax.xml.parsers.DocumentBuilderFactory.newInstance();
      fabrica.setNamespaceAware(true);
      fabrica.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
      fabrica.setFeature("http://xml.org/sax/features/external-general-entities", false);
      fabrica.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
      fabrica.setXIncludeAware(false);
      fabrica.setExpandEntityReferences(false);
      var construtor = fabrica.newDocumentBuilder();
      construtor.setErrorHandler(new org.xml.sax.helpers.DefaultHandler());
      var documento = construtor.parse(new java.io.ByteArrayInputStream(xml.getBytes(java.nio.charset.StandardCharsets.UTF_8)));
      redactar(documento.getDocumentElement());
      var destino = new java.io.StringWriter();
      var transformador = javax.xml.transform.TransformerFactory.newInstance().newTransformer();
      transformador.transform(new javax.xml.transform.dom.DOMSource(documento), new javax.xml.transform.stream.StreamResult(destino));
      return destino.toString();
    } catch (Exception ex) {
      return SEGREDO.matcher(xml).replaceAll("$1=[omitido]");
    }
  }

  private static void redactar(Element elemento) {
    var nome = elemento.getLocalName() == null ? elemento.getNodeName() : elemento.getLocalName();
    if (nome != null && nome.toLowerCase(Locale.ROOT).matches(".*(senha|password|token|secret|authorization).*")) {
      while (elemento.hasChildNodes()) elemento.removeChild(elemento.getFirstChild());
      elemento.setTextContent("[omitido]");
      return;
    }
    var filhos = elemento.getChildNodes();
    for (int i = 0; i < filhos.getLength(); i++) {
      if (filhos.item(i) instanceof Element filho) redactar(filho);
    }
  }
}
