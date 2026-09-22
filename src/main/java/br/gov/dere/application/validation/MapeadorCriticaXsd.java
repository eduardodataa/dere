package br.gov.dere.application.validation;

import br.gov.dere.application.validation.catalogo.CatalogosCampo;
import br.gov.dere.application.validation.catalogo.RegraCampo;
import br.gov.dere.application.validation.relatorio.Critica;
import br.gov.dere.application.validation.relatorio.TipoCritica;
import br.gov.dere.integration.xml.XmlSupport;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

public final class MapeadorCriticaXsd {
  private static final Pattern VALOR = Pattern.compile("(?:[Vv]alue|[Vv]alor) '([^']*)'");
  private static final Pattern ELEMENTO = Pattern.compile("(?:element|elemento|attribute|atributo) '?\\{?[^'}]*\\}?([^']+)'?", Pattern.CASE_INSENSITIVE);

  private MapeadorCriticaXsd() {}

  public static Critica mapear(XmlSupport.Issue problema, String arquivo, List<RegraCampo> catalogo) {
    var campo = nomeLocal(problema.field());
    if (campo.isBlank()) campo = nomeLocal(extrairElemento(problema.message()));
    var regra = CatalogosCampo.porNome(catalogo, campo);
    if (regra == null && "tpAtividade".equals(campo)) regra = CatalogosCampo.porNome(catalogo, "tpAtividadeServFinanc");
    var encontrado = extrairValor(problema.message());
    var tipo = classificar(problema.message());
    var esperado = regra == null ? "" : ValidadorCelula.esperado(regra);
    var caminho = regra == null ? campo : regra.caminhoXml();
    var coluna = campo.isBlank() ? "Documento" : campo;
    return new Critica(arquivo, problema.line() > 0 ? problema.line() : null, coluna, encontrado, tipo, esperado,
        mensagem(tipo, coluna, encontrado, esperado), caminho);
  }

  public static boolean mesmoCampo(Critica esquerda, Critica direita) {
    return esquerda.coluna().equals(direita.coluna()) && esquerda.arquivo().equals(direita.arquivo());
  }

  private static TipoCritica classificar(String mensagem) {
    var texto = mensagem == null ? "" : mensagem.toLowerCase(Locale.ROOT);
    if (texto.contains("enumeration") || texto.contains("enumera")) return TipoCritica.FORA_DO_DOMINIO;
    if (texto.contains("pattern") || texto.contains("facet-valid") || texto.contains("not facet")) return TipoCritica.FORA_DO_PADRAO;
    if (texto.contains("minoccurs") || texto.contains("missing") || texto.contains("not complete") || texto.contains("required")) {
      return TipoCritica.OBRIGATORIO;
    }
    if (texto.contains("length") || texto.contains("maxlength") || texto.contains("minlength")) return TipoCritica.FORA_DO_PADRAO;
    return TipoCritica.ESTRUTURA;
  }

  private static String mensagem(TipoCritica tipo, String coluna, String encontrado, String esperado) {
    var exibido = encontrado == null || encontrado.isBlank() ? "(vazio)" : encontrado;
    return switch (tipo) {
      case OBRIGATORIO -> "Campo obrigatório ausente no XML (" + coluna + ").";
      case FORA_DO_DOMINIO -> "Valor " + exibido + " fora de domínio. Aceitos: " + esperado + ".";
      case FORA_DO_PADRAO -> "Valor " + exibido + " fora do padrão. Esperado: " + esperado + ".";
      case NEGOCIO -> "Regra de negócio violada em " + coluna + ".";
      case ESTRUTURA -> "Estrutura XML inválida em " + coluna + (encontrado.isBlank() ? "." : " (valor " + exibido + ").");
    };
  }

  private static String extrairValor(String mensagem) {
    if (mensagem == null) return "";
    var matcher = VALOR.matcher(mensagem);
    return matcher.find() ? matcher.group(1) : "";
  }

  private static String extrairElemento(String mensagem) {
    if (mensagem == null) return "";
    var matcher = ELEMENTO.matcher(mensagem);
    return matcher.find() ? matcher.group(1) : "";
  }

  private static String nomeLocal(String campo) {
    if (campo == null || campo.isBlank()) return "";
    var texto = campo.trim();
    var barra = Math.max(texto.lastIndexOf('/'), texto.lastIndexOf(':'));
    if (barra >= 0 && barra + 1 < texto.length()) texto = texto.substring(barra + 1);
    var chave = texto.lastIndexOf('}');
    if (chave >= 0 && chave + 1 < texto.length()) texto = texto.substring(chave + 1);
    return texto.replace("'", "").trim();
  }
}
