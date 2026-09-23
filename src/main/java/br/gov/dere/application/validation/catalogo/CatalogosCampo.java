package br.gov.dere.application.validation.catalogo;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class CatalogosCampo {
  private CatalogosCampo() {}

  public static final Map<String, String> TP_OPER = dominio(
      "1", "Inclusão",
      "2", "Alteração",
      "3", "Exclusão");
  public static final Map<String, String> MOT_EXCL = dominio(
      "1", "Determinação judicial ou administrativa",
      "2", "Envio indevido (fato inexistente)",
      "3", "Erro na identificação (CNPJ/período incorretos)",
      "9", "Outro");
  public static final Map<String, String> TP_AMB = dominio(
      "1", "Produção",
      "2", "Produção Restrita");
  public static final Map<String, String> APLIC_EMI = dominio(
      "1", "Aplicativo da empresa",
      "2", "Aplicativo governamental");
  public static final Map<String, String> REG_TRIB = dominio(
      "1", "Serviços financeiros",
      "2", "Plano de assistência à saúde",
      "3", "Concursos de prognósticos",
      "9", "Outros regimes");
  public static final Map<String, String> REG_TRIB_SEC = dominio(
      "1", "Serviços financeiros",
      "2", "Plano de assistência à saúde",
      "3", "Concursos de prognósticos");
  public static final Map<String, String> IND_NAT_TRIB = dominio(
      "0", "Tributação regular",
      "1", "Imunidade ou não incidência");
  public static final Map<String, String> PLANO_CTA = dominio(
      "1", "COSIF",
      "2", "ANS",
      "3", "SUSEP",
      "4", "SPED",
      "5", "PREVIC");
  public static final Map<String, String> FREQ = dominio(
      "A", "Anual",
      "S", "Semestral",
      "Q", "Quadrimestral",
      "T", "Trimestral",
      "B", "Bimestral",
      "M", "Mensal");
  public static final Map<String, String> IND_CTA = dominio(
      "S", "Sintética",
      "A", "Analítica");
  public static final Map<String, String> NAT_CTA = dominio(
      "C", "Credora",
      "D", "Devedora",
      "V", "Variável");
  public static final Map<String, String> COD_NAT = dominio(
      "1", "Ativo",
      "2", "Passivo",
      "3", "Patrimônio líquido",
      "4", "Receita",
      "5", "Despesa");
  public static final Map<String, String> IND_ISS = dominio(
      "0", "Não sujeita ao ISS",
      "1", "Sujeita ao ISS");
  public static final Map<String, String> NAT_SALDO = dominio(
      "D", "Devedor",
      "C", "Credor");
  public static final Map<String, String> TP_OPER_1199 = dominio(
      "1", "Inclusão");
  public static final Map<String, String> USAR_BCN = dominio(
      "0", "Não aproveitar bases negativas",
      "1", "Aproveitar bases negativas");
  public static final Map<String, String> METODO_BCN = dominio(
      "0", "PEPS automático",
      "1", "Informação manual");

  public static List<RegraCampo> d1001() {
    return List.of(
        RegraCampo.texto("id", true, "[0-9A-Za-z]{42}", "exatamente 42 caracteres alfanuméricos", 42, "/DeRE/evtInfoContrib/@id", true),
        RegraCampo.dominio("motExcl", false, MOT_EXCL, "/DeRE/evtInfoContrib/ideEvento/motExcl", true),
        RegraCampo.texto("nrProc", false, null, "até 21 caracteres", 21, "/DeRE/evtInfoContrib/ideEvento/nrProc", true),
        RegraCampo.dominio("tpOper", true, TP_OPER, "/DeRE/evtInfoContrib/ideEvento/tpOper", true),
        RegraCampo.dominio("tpAmb", true, TP_AMB, "/DeRE/evtInfoContrib/ideEvento/tpAmb", true),
        RegraCampo.dominio("aplicEmi", true, APLIC_EMI, "/DeRE/evtInfoContrib/ideEvento/aplicEmi", true),
        RegraCampo.texto("verAplic", true, null, "1 a 20 caracteres", 20, "/DeRE/evtInfoContrib/ideEvento/verAplic", true),
        RegraCampo.texto("nrInsc", true, "[0-9A-Z]{8}", "8 caracteres (CNPJ raiz, A-Z e 0-9)", 8, "/DeRE/evtInfoContrib/ideContrib/nrInsc", true),
        RegraCampo.data("iniValid", true, "/DeRE/evtInfoContrib/idePeriodo/iniValid", true),
        RegraCampo.data("fimValid", false, "/DeRE/evtInfoContrib/idePeriodo/fimValid", true),
        RegraCampo.data("novaValidadeIniValid", false, "/DeRE/evtInfoContrib/idePeriodo/novaValidade/iniValid", true),
        RegraCampo.data("novaValidadeFimValid", false, "/DeRE/evtInfoContrib/idePeriodo/novaValidade/fimValid", true),
        RegraCampo.dominio("regTribPrinc", true, REG_TRIB, "/DeRE/evtInfoContrib/infoContrib/regTribPrinc", true),
        RegraCampo.listaDominio("regTribSecund", REG_TRIB_SEC, "/DeRE/evtInfoContrib/infoContrib/regTribSecund"),
        RegraCampo.dominio("indNatTrib", true, IND_NAT_TRIB, "/DeRE/evtInfoContrib/infoContrib/indNatTrib", true),
        RegraCampo.listaPadrao("tpAtividadeServFinanc", "[0-9]{2}[A-Z]", "máscara NNC (ex.: 13B), Tabela 21", "/DeRE/evtInfoContrib/infoContrib/servFinanc/tpAtividades/tpAtividade"),
        RegraCampo.listaPadrao("tpAtividadePlAssistSaude", "[0-9]{2}[A-Z]", "máscara NNC (ex.: 21A), Tabela 31", "/DeRE/evtInfoContrib/infoContrib/plAssistSaude/tpAtividades/tpAtividade"),
        RegraCampo.listaPadrao("tpAtividadePrognosticos", "[0-9]{2}[A-Z]", "máscara NNC, Tabela 41", "/DeRE/evtInfoContrib/infoContrib/prognosticos/tpAtividades/tpAtividade"),
        RegraCampo.listaPadrao("UFCredenc", "\\d{2}", "código IBGE da UF com 2 dígitos (01 a 99)", "/DeRE/evtInfoContrib/infoContrib/prognosticos/UFsCredenc/UFCredenc"));
  }

  public static List<RegraCampo> d1011() {
    return List.of(
        RegraCampo.texto("id", true, "[0-9A-Za-z]{42}", "exatamente 42 caracteres alfanuméricos", 42, "/DeRE/evtPGCC/@id", true),
        RegraCampo.dominio("motExcl", false, MOT_EXCL, "/DeRE/evtPGCC/ideEvento/motExcl", true),
        RegraCampo.texto("nrProc", false, null, "até 21 caracteres", 21, "/DeRE/evtPGCC/ideEvento/nrProc", true),
        RegraCampo.dominio("tpOper", true, TP_OPER, "/DeRE/evtPGCC/ideEvento/tpOper", true),
        RegraCampo.dominio("tpAmb", true, TP_AMB, "/DeRE/evtPGCC/ideEvento/tpAmb", true),
        RegraCampo.dominio("aplicEmi", true, APLIC_EMI, "/DeRE/evtPGCC/ideEvento/aplicEmi", true),
        RegraCampo.texto("verAplic", true, null, "1 a 20 caracteres", 20, "/DeRE/evtPGCC/ideEvento/verAplic", true),
        RegraCampo.texto("nrInsc", true, "[0-9A-Z]{8}", "8 caracteres (CNPJ raiz, A-Z e 0-9)", 8, "/DeRE/evtPGCC/ideContrib/nrInsc", true),
        RegraCampo.data("iniValid", true, "/DeRE/evtPGCC/idePeriodo/iniValid", true),
        RegraCampo.data("fimValid", false, "/DeRE/evtPGCC/idePeriodo/fimValid", true),
        RegraCampo.dominio("planoCtaRef", true, PLANO_CTA, "/DeRE/evtPGCC/infoPGCC/planoCtaRef", true),
        RegraCampo.dominio("freqEncerr", true, FREQ, "/DeRE/evtPGCC/infoPGCC/freqEncerr", true),
        RegraCampo.texto("cCta", true, "[0-9A-Za-z]{1,53}", "somente letras e números, sem ponto ou traço (ex.: 112346)", 53, "/DeRE/evtPGCC/infoPGCC/infoContas/infoConta/cCta", false),
        RegraCampo.texto("cCtaInterna", true, "[0-9A-Za-z]{1,50}", "somente letras e números, sem ponto ou traço", 50, "/DeRE/evtPGCC/infoPGCC/infoContas/infoConta/cCtaInterna", false),
        RegraCampo.texto("cDbrMista", true, "\\d{3}", "3 dígitos (000 se não houver desdobramento)", 3, "/DeRE/evtPGCC/infoPGCC/infoContas/infoConta/cDbrMista", false),
        RegraCampo.texto("nomeCta", true, null, "1 a 100 caracteres", 100, "/DeRE/evtPGCC/infoPGCC/infoContas/infoConta/nomeCta", false),
        RegraCampo.dominio("indCta", true, IND_CTA, "/DeRE/evtPGCC/infoPGCC/infoContas/infoConta/indCta", false),
        RegraCampo.texto("descCta", false, null, "até 600 caracteres", 600, "/DeRE/evtPGCC/infoPGCC/infoContas/infoConta/descCta", false),
        RegraCampo.texto("cCtaSup", false, "[0-9A-Za-z]{1,53}", "somente letras e números, sem ponto ou traço", 53, "/DeRE/evtPGCC/infoPGCC/infoContas/infoConta/cCtaSup", false),
        RegraCampo.texto("cCtaRef", true, "[0-9A-Za-z]{1,13}", "código referencial alfanumérico, sem ponto ou traço", 13, "/DeRE/evtPGCC/infoPGCC/infoContas/infoConta/cCtaRef", false),
        RegraCampo.texto("nivelCta", true, "[1-9][0-9]?", "número de 1 a 99", 2, "/DeRE/evtPGCC/infoPGCC/infoContas/infoConta/nivelCta", false),
        RegraCampo.dominio("natCta", true, NAT_CTA, "/DeRE/evtPGCC/infoPGCC/infoContas/infoConta/natCta", false),
        RegraCampo.dominio("codNat", true, COD_NAT, "/DeRE/evtPGCC/infoPGCC/infoContas/infoConta/codNat", false),
        RegraCampo.texto("codTrib", false, "\\d{1,9}", "número de 1 a 999999999", 9, "/DeRE/evtPGCC/infoPGCC/infoContas/infoConta/codTrib", false),
        RegraCampo.dominio("indTribISS", false, IND_ISS, "/DeRE/evtPGCC/infoPGCC/infoContas/infoConta/indTribISS", false),
        RegraCampo.texto("idLeiDisp", false, "\\d{2}-\\d{2}", "máscara CC-CC (ex.: 01-02)", 5, "/DeRE/evtPGCC/infoPGCC/infoContas/infoConta/idLeiDisp", false),
        RegraCampo.data("iniVig", true, "/DeRE/evtPGCC/infoPGCC/infoContas/infoConta/iniVig", false),
        RegraCampo.data("fimVig", false, "/DeRE/evtPGCC/infoPGCC/infoContas/infoConta/fimVig", false));
  }

  public static List<RegraCampo> d1101() {
    return List.of(
        RegraCampo.texto("id", true, "[0-9A-Za-z]{42}", "exatamente 42 caracteres alfanuméricos", 42, "/DeRE/evtBalancete/@id", true),
        RegraCampo.dominio("motExcl", false, MOT_EXCL, "/DeRE/evtBalancete/ideEvento/motExcl", true),
        RegraCampo.texto("nrProc", false, null, "até 21 caracteres", 21, "/DeRE/evtBalancete/ideEvento/nrProc", true),
        RegraCampo.texto("nrRecibo", false, "[0-9]{4}-20[0-9]{2}(0[1-9]|1[0-2])-[0-9A-Z]{19}", "31 caracteres no formato NNNN-AAAAMM-XXXXXXXXXXXXXXXXXXX", 31, "/DeRE/evtBalancete/ideEvento/nrRecibo", true),
        RegraCampo.dominio("tpOper", true, TP_OPER, "/DeRE/evtBalancete/ideEvento/tpOper", true),
        RegraCampo.dominio("tpAmb", true, TP_AMB, "/DeRE/evtBalancete/ideEvento/tpAmb", true),
        RegraCampo.dominio("aplicEmi", true, APLIC_EMI, "/DeRE/evtBalancete/ideEvento/aplicEmi", true),
        RegraCampo.texto("verAplic", true, null, "1 a 20 caracteres", 20, "/DeRE/evtBalancete/ideEvento/verAplic", true),
        RegraCampo.texto("nrInsc", true, "[0-9A-Z]{8}", "8 caracteres (CNPJ raiz, A-Z e 0-9)", 8, "/DeRE/evtBalancete/ideContrib/nrInsc", true),
        RegraCampo.texto("perApur", true, "20[0-9]{2}-(0[1-9]|1[0-2])", "AAAA-MM", 7, "/DeRE/evtBalancete/idePeriodo/perApur", true),
        RegraCampo.texto("cCta", true, "[0-9A-Za-z]{1,53}", "somente letras e números, sem ponto ou traço", 53, "/DeRE/evtBalancete/infoBalancete/infoContas/infoConta/cCta", false),
        RegraCampo.dominio("natSaldoInic", true, NAT_SALDO, "/DeRE/evtBalancete/infoBalancete/infoContas/infoConta/natSaldoInic", false),
        RegraCampo.texto("vSaldoInic", true, "(0|[1-9][0-9]{0,14})[.,][0-9]{2}", "valor absoluto com 2 decimais", 18, "/DeRE/evtBalancete/infoBalancete/infoContas/infoConta/vSaldoInic", false),
        RegraCampo.texto("vMovDebt", true, "(0|[1-9][0-9]{0,14})[.,][0-9]{2}", "valor absoluto com 2 decimais", 18, "/DeRE/evtBalancete/infoBalancete/infoContas/infoConta/vMovDebt", false),
        RegraCampo.texto("vAjusteDebt", false, "(0|[1-9][0-9]{0,14})[.,][0-9]{2}", "valor absoluto com 2 decimais", 18, "/DeRE/evtBalancete/infoBalancete/infoContas/infoConta/vAjusteDebt", false),
        RegraCampo.texto("vMovCred", true, "(0|[1-9][0-9]{0,14})[.,][0-9]{2}", "valor absoluto com 2 decimais", 18, "/DeRE/evtBalancete/infoBalancete/infoContas/infoConta/vMovCred", false),
        RegraCampo.texto("vAjusteCred", false, "(0|[1-9][0-9]{0,14})[.,][0-9]{2}", "valor absoluto com 2 decimais", 18, "/DeRE/evtBalancete/infoBalancete/infoContas/infoConta/vAjusteCred", false),
        RegraCampo.dominio("natSaldoFinal", true, NAT_SALDO, "/DeRE/evtBalancete/infoBalancete/infoContas/infoConta/natSaldoFinal", false),
        RegraCampo.texto("vSaldoFinal", true, "(0|[1-9][0-9]{0,14})[.,][0-9]{2}", "valor absoluto com 2 decimais", 18, "/DeRE/evtBalancete/infoBalancete/infoContas/infoConta/vSaldoFinal", false),
        RegraCampo.dominio("natVApur", false, NAT_SALDO, "/DeRE/evtBalancete/infoBalancete/infoContas/infoConta/natVApur", false),
        RegraCampo.texto("vApur", true, "(0|[1-9][0-9]{0,14})[.,][0-9]{2}", "valor absoluto com 2 decimais", 18, "/DeRE/evtBalancete/infoBalancete/infoContas/infoConta/vApur", false));
  }

  public static List<RegraCampo> d1199() {
    return List.of(
        RegraCampo.texto("id", true, "[0-9A-Za-z]{42}", "exatamente 42 caracteres alfanuméricos", 42, "/DeRE/evtFechMensal/@id", true),
        RegraCampo.dominio("tpOper", true, TP_OPER_1199, "/DeRE/evtFechMensal/ideEvento/tpOper", true),
        RegraCampo.dominio("tpAmb", true, TP_AMB, "/DeRE/evtFechMensal/ideEvento/tpAmb", true),
        RegraCampo.dominio("aplicEmi", true, APLIC_EMI, "/DeRE/evtFechMensal/ideEvento/aplicEmi", true),
        RegraCampo.texto("verAplic", true, null, "1 a 20 caracteres", 20, "/DeRE/evtFechMensal/ideEvento/verAplic", true),
        RegraCampo.texto("nrInsc", true, "[0-9A-Z]{8}", "8 caracteres (CNPJ raiz, A-Z e 0-9)", 8, "/DeRE/evtFechMensal/ideContrib/nrInsc", true),
        RegraCampo.texto("perApur", true, "20[0-9]{2}-(0[1-9]|1[0-2])", "AAAA-MM", 7, "/DeRE/evtFechMensal/idePeriodo/perApur", true),
        RegraCampo.texto("codBCNRaiz", false, "[1-9][0-9]{3}[IC]", "5 caracteres (ex.: 1001I)", 5, "/DeRE/evtFechMensal/infoFechamento/gUtilizBCN/infoBCN/codBCNRaiz", false),
        RegraCampo.dominio("usarBCNAcum", false, USAR_BCN, "/DeRE/evtFechMensal/infoFechamento/gUtilizBCN/infoBCN/usarBCNAcum", false),
        RegraCampo.dominio("metodoAproveit", false, METODO_BCN, "/DeRE/evtFechMensal/infoFechamento/gUtilizBCN/infoBCN/metodoAproveit", false),
        RegraCampo.texto("vUtilBCN", false, "(0|[1-9][0-9]{0,14})[.,][0-9]{2}", "valor absoluto com 2 decimais", 18, "/DeRE/evtFechMensal/infoFechamento/gUtilizBCN/infoBCN/vUtilBCN", false),
        RegraCampo.texto("codBCN", false, "[1-9][0-9]{3}[IC][0-9]{8}", "13 caracteres (ex.: 1001I20261001)", 13, "/DeRE/evtFechMensal/infoFechamento/gUtilizBCN/detBCNeg/codBCN", false),
        RegraCampo.texto("vUsarBCN", false, "(0|[1-9][0-9]{0,14})[.,][0-9]{2}", "valor absoluto com 2 decimais", 18, "/DeRE/evtFechMensal/infoFechamento/gUtilizBCN/detBCNeg/vUsarBCN", false));
  }

  public static RegraCampo porNome(List<RegraCampo> catalogo, String nome) {
    return catalogo.stream().filter(regra -> regra.nome().equals(nome)).findFirst().orElse(null);
  }

  public static String formatarDominio(Map<String, String> dominio) {
    if (dominio == null || dominio.isEmpty()) return "";
    var saida = new StringBuilder();
    dominio.forEach((codigo, rotulo) -> {
      if (!saida.isEmpty()) saida.append("; ");
      saida.append(codigo).append(" = ").append(rotulo);
    });
    return saida.toString();
  }

  private static Map<String, String> dominio(String... pares) {
    var mapa = new LinkedHashMap<String, String>();
    for (int i = 0; i < pares.length; i += 2) mapa.put(pares[i], pares[i + 1]);
    return Map.copyOf(mapa);
  }
}
