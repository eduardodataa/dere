package br.gov.dere.application.upload;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Set;

public final class PastaEntidadeUpload {
  private static final Set<String> GENERICOS = Set.of(
      "a", "e", "o", "de", "da", "do", "dos", "das", "fundacao", "fundo", "instituto",
      "entidade", "administradora", "seguridade", "social", "multipatrocinado", "secao",
      "caixa", "assistencia", "complementar", "assembleia", "legislativa", "estado",
      "bahia", "brasil", "ordem");

  private PastaEntidadeUpload() {}

  public static String de(Long entityId, String nome) {
    var id = entityId == null ? "0" : String.valueOf(entityId);
    var slug = slug(nome);
    if (slug.isBlank()) return id;
    return id + "-" + slug;
  }

  static String slug(String nome) {
    var base = semAcento(nome == null ? "" : nome).toLowerCase(Locale.ROOT).replace('/', ' ').replace('\\', ' ');
    if (base.contains("petrobras") || base.contains("petros")) return "petros";
    if (base.contains("albaprev")) return "albaprev";
    if (base.contains("actuarial")) return "actuarial";
    if ((base.contains("advogado") || base.contains("oab")) && base.contains("parana")) return "oab-pr";
    if (base.contains("familia") && base.contains("previdencia")) return "familia-previdencia";
    if (base.contains("efpc") && base.contains("teste")) return "efpc-teste";
    var partes = new ArrayList<String>();
    for (var token : base.split("[^a-z0-9]+")) {
      if (token.isBlank() || GENERICOS.contains(token)) continue;
      partes.add(token);
      if (partes.size() == 2) break;
    }
    return String.join("-", partes);
  }

  private static String semAcento(String valor) {
    return Normalizer.normalize(valor, Normalizer.Form.NFD).replaceAll("\\p{M}", "");
  }
}
