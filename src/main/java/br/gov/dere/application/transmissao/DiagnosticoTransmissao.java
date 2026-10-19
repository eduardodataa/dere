package br.gov.dere.application.transmissao;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public record DiagnosticoTransmissao(
    String categoria,
    String titulo,
    String mensagem,
    String orientacao,
    String codigo,
    String descricaoOriginal,
    Integer http,
    String identificador,
    String protocolo,
    String status,
    String schema,
    List<OcorrenciaRetorno> ocorrencias,
    String correlacao,
    boolean repetir,
    boolean inconclusivo,
    int httpAplicacao) {

  public Map<String, Object> corpo() {
    var erro = new LinkedHashMap<String, Object>();
    erro.put("categoria", categoria);
    erro.put("titulo", titulo);
    erro.put("mensagem", mensagem);
    erro.put("orientacao", orientacao);
    colocar(erro, "codigo", codigo);
    colocar(erro, "descricaoOriginal", descricaoOriginal);
    if (http != null) erro.put("http", http);
    colocar(erro, "identificador", identificador);
    colocar(erro, "protocolo", protocolo);
    colocar(erro, "status", status);
    colocar(erro, "schema", schema);
    erro.put("ocorrencias", ocorrencias == null ? List.of() : ocorrencias);
    erro.put("correlacao", correlacao);
    erro.put("repetir", repetir);
    erro.put("inconclusivo", inconclusivo);
    var corpo = new LinkedHashMap<String, Object>();
    corpo.put("message", mensagem);
    corpo.put("erro", erro);
    return corpo;
  }

  private static void colocar(Map<String, Object> destino, String chave, String valor) {
    if (valor != null && !valor.isBlank()) destino.put(chave, valor);
  }
}
