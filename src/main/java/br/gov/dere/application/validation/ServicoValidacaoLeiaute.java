package br.gov.dere.application.validation;

import br.gov.dere.application.csv.DereCsvConverter;
import br.gov.dere.application.d1001.D1001XmlGenerator;
import br.gov.dere.application.periodico.D1101CsvConverter;
import br.gov.dere.application.periodico.D1101XmlGenerator;
import br.gov.dere.application.periodico.D1199CsvConverter;
import br.gov.dere.application.periodico.D1199XmlGenerator;
import br.gov.dere.application.pgcc.D1011CsvConverter;
import br.gov.dere.application.pgcc.D1011XmlGenerator;
import br.gov.dere.application.validation.catalogo.CatalogosCampo;
import br.gov.dere.application.validation.catalogo.RegraCampo;
import br.gov.dere.application.validation.relatorio.Critica;
import br.gov.dere.application.validation.relatorio.PlanilhaValidacao;
import br.gov.dere.application.validation.relatorio.RelatorioValidacao;
import br.gov.dere.application.validation.relatorio.TipoCritica;
import br.gov.dere.domain.layout.ValidationContext;
import br.gov.dere.domain.pgcc.D1011;
import br.gov.dere.integration.xml.XmlSupport;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilderFactory;
import org.springframework.stereotype.Service;
import org.w3c.dom.Node;

@Service
public class ServicoValidacaoLeiaute {
  private static final String XSD_D1001 = "/dere/schemas/nota_2026_001/xsd/evtInfoContrib-v1_0_1.xsd";
  private static final String XSD_D1011 = "/dere/schemas/v1_1_0/xsd/06-XSD-D-1011 (v. 1.0.1).xsd";
  private static final String XSD_D1101 = "/dere/schemas/v1_1_0/xsd/06-XSD-D-1101 (v. 0.0.1).xsd";
  private final DereCsvConverter csvD1001 = new DereCsvConverter();
  private final D1001XmlGenerator xmlD1001 = new D1001XmlGenerator();
  private final D1011CsvConverter csvD1011 = new D1011CsvConverter();
  private final D1011XmlGenerator xmlD1011 = new D1011XmlGenerator();
  private final D1101CsvConverter csvD1101 = new D1101CsvConverter();
  private final D1101XmlGenerator xmlD1101 = new D1101XmlGenerator();
  private final D1199CsvConverter csvD1199 = new D1199CsvConverter();
  private final D1199XmlGenerator xmlD1199 = new D1199XmlGenerator();
  private final D1011DependencyValidator dependencias;
  private final ChaveEventoTabelaValidator chaves;

  public ServicoValidacaoLeiaute(D1011DependencyValidator dependencias, ChaveEventoTabelaValidator chaves) {
    this.dependencias = dependencias;
    this.chaves = chaves;
  }

  public RelatorioValidacao validarCsv(String leiaute, String csv, String nomeArquivo, String cnpjEsperado, Long idEntidade) {
    var arquivo = vazioPara(nomeArquivo, "arquivo.csv");
    var tabela = TabelaCsv.analisar(csv);
    var criticas = new ArrayList<>(ValidadorCamposCsv.validar(leiaute, arquivo, csv));
    if (!tabela.linhas().isEmpty()) {
      var primeira = tabela.linhas().get(0);
      ValidadorCamposCsv.adicionarDivergenciaCnpj(criticas, arquivo, primeira.numero(), tabela.valor(primeira, "nrInsc"), cnpjEsperado, caminhoNrInsc(leiaute));
    }
    var xml = "";
    var canonico = "";
    try {
      if ("D-1011".equals(leiaute)) {
        var modelo = csvD1011.fromCsv(csv);
        canonico = csvD1011.toCsv(modelo);
        xml = xmlD1011.generate(modelo);
        adicionarXsd(criticas, arquivo, xml, XSD_D1011, CatalogosCampo.d1011());
        adicionarDependencias(criticas, arquivo, modelo, idEntidade, tabela.linhas().isEmpty() ? null : tabela.linhas().get(0).numero());
        adicionarChave(criticas, arquivo, leiaute, modelo.operation(), modelo.cnpjRoot(), modelo.validFrom(), modelo.validTo(), idEntidade, tabela.linhas().isEmpty() ? null : tabela.linhas().get(0).numero());
      } else if ("D-1101".equals(leiaute)) {
        var modelo = csvD1101.fromCsv(csv);
        canonico = csvD1101.toCsv(modelo);
        xml = xmlD1101.generate(modelo);
        adicionarXsd(criticas, arquivo, xml, XSD_D1101, CatalogosCampo.d1101());
      } else if ("D-1199".equals(leiaute)) {
        var modelo = csvD1199.fromCsv(csv);
        canonico = csvD1199.toCsv(modelo);
        xml = xmlD1199.generate(modelo);
      } else {
        var modelo = csvD1001.fromCsv(csv);
        canonico = csvD1001.toCsv(modelo);
        xml = xmlD1001.generate(modelo);
        adicionarXsd(criticas, arquivo, xml, XSD_D1001, CatalogosCampo.d1001());
        adicionarChave(criticas, arquivo, leiaute, modelo.operation(), modelo.cnpjRoot(), modelo.validFrom(), modelo.validTo(), idEntidade, tabela.linhas().isEmpty() ? null : tabela.linhas().get(0).numero());
      }
    } catch (Exception ex) {
      if (criticas.isEmpty()) {
        criticas.add(new Critica(arquivo, 1, "Documento", "", TipoCritica.ESTRUTURA, "CSV no formato do leiaute " + leiaute,
            mensagemDe(ex), ""));
      }
    }
    return finalizar(leiaute, "CSV", arquivo, tabela.linhas().size(), criticas, xml, canonico.isBlank() ? csv : canonico);
  }

  public RelatorioValidacao validarXml(String leiaute, String xml, String nomeArquivo, String cnpjEsperado, Long idEntidade) {
    var arquivo = vazioPara(nomeArquivo, "arquivo.xml");
    var criticas = new ArrayList<Critica>();
    var eventoEsperado = evento(leiaute);
    var detectado = detectar(xml);
    if (detectado != null && !eventoEsperado.equals(detectado)) {
      criticas.add(new Critica(arquivo, 1, "Documento", detectado, TipoCritica.ESTRUTURA, eventoEsperado,
          "Leiaute esperado: " + leiaute + " (" + eventoEsperado + "). Encontrado: " + detectado + ".", "/DeRE"));
    }
    if (!"D-1199".equals(leiaute)) {
      adicionarXsd(criticas, arquivo, xml, xsd(leiaute), catalogo(leiaute));
    }
    var csv = "";
    var quantidadeLinhas = 0;
    try {
      if ("D-1011".equals(leiaute)) {
        var modelo = csvD1011.fromXml(xml);
        csv = csvD1011.toCsv(modelo);
        mesclarAusentes(criticas, ValidadorCamposCsv.validar(leiaute, arquivo, csv));
        var tabela = TabelaCsv.analisar(csv);
        quantidadeLinhas = tabela.linhas().size();
        if (!tabela.linhas().isEmpty()) {
          ValidadorCamposCsv.adicionarDivergenciaCnpj(criticas, arquivo, tabela.linhas().get(0).numero(), tabela.valor(tabela.linhas().get(0), "nrInsc"), cnpjEsperado, caminhoNrInsc(leiaute));
        }
        adicionarDependencias(criticas, arquivo, modelo, idEntidade, tabela.linhas().isEmpty() ? null : tabela.linhas().get(0).numero());
        adicionarChave(criticas, arquivo, leiaute, modelo.operation(), modelo.cnpjRoot(), modelo.validFrom(), modelo.validTo(), idEntidade, tabela.linhas().isEmpty() ? null : tabela.linhas().get(0).numero());
      } else if ("D-1101".equals(leiaute)) {
        var modelo = csvD1101.fromXml(xml);
        csv = csvD1101.toCsv(modelo);
        mesclarAusentes(criticas, ValidadorCamposCsv.validar(leiaute, arquivo, csv));
        var tabela = TabelaCsv.analisar(csv);
        quantidadeLinhas = tabela.linhas().size();
        if (!tabela.linhas().isEmpty()) {
          ValidadorCamposCsv.adicionarDivergenciaCnpj(criticas, arquivo, tabela.linhas().get(0).numero(), tabela.valor(tabela.linhas().get(0), "nrInsc"), cnpjEsperado, caminhoNrInsc(leiaute));
        }
      } else if ("D-1199".equals(leiaute)) {
        var modelo = csvD1199.fromXml(xml);
        csv = csvD1199.toCsv(modelo);
        mesclarAusentes(criticas, ValidadorCamposCsv.validar(leiaute, arquivo, csv));
        var tabela = TabelaCsv.analisar(csv);
        quantidadeLinhas = tabela.linhas().size();
        if (!tabela.linhas().isEmpty()) {
          ValidadorCamposCsv.adicionarDivergenciaCnpj(criticas, arquivo, tabela.linhas().get(0).numero(), tabela.valor(tabela.linhas().get(0), "nrInsc"), cnpjEsperado, caminhoNrInsc(leiaute));
        }
      } else {
        var modelo = csvD1001.fromXml(xml);
        csv = csvD1001.toCsv(modelo);
        mesclarAusentes(criticas, ValidadorCamposCsv.validar(leiaute, arquivo, csv));
        var tabela = TabelaCsv.analisar(csv);
        quantidadeLinhas = tabela.linhas().size();
        if (!tabela.linhas().isEmpty()) {
          ValidadorCamposCsv.adicionarDivergenciaCnpj(criticas, arquivo, tabela.linhas().get(0).numero(), tabela.valor(tabela.linhas().get(0), "nrInsc"), cnpjEsperado, caminhoNrInsc(leiaute));
        }
        adicionarChave(criticas, arquivo, leiaute, modelo.operation(), modelo.cnpjRoot(), modelo.validFrom(), modelo.validTo(), idEntidade, tabela.linhas().isEmpty() ? null : tabela.linhas().get(0).numero());
      }
    } catch (Exception ex) {
      if (criticas.isEmpty()) {
        criticas.add(new Critica(arquivo, 1, "Documento", "", TipoCritica.ESTRUTURA, "XML do leiaute " + leiaute, mensagemDe(ex), "/DeRE"));
      }
    }
    return finalizar(leiaute, "XML", arquivo, quantidadeLinhas, criticas, xml, csv);
  }

  public RelatorioValidacao agregar(String leiaute, String origem, String arquivo, int totalLinhas, List<Critica> criticas) {
    return finalizar(leiaute, origem, arquivo, totalLinhas, criticas, "", "");
  }

  private RelatorioValidacao finalizar(String leiaute, String origem, String arquivo, int totalLinhas, List<Critica> criticas, String xml, String csv) {
    var relatorio = RelatorioValidacao.de(leiaute, origem, arquivo, totalLinhas, criticas);
    return relatorio.comArtefatos(xml, csv, PlanilhaValidacao.base64(relatorio));
  }

  private void adicionarXsd(List<Critica> criticas, String arquivo, String xml, String xsd, List<RegraCampo> catalogo) {
    var url = getClass().getResource(xsd);
    if (url == null || xml == null || xml.isBlank()) return;
    var conhecidos = new java.util.HashSet<String>();
    catalogo.forEach(regra -> conhecidos.add(regra.nome()));
    conhecidos.add("tpAtividade");
    var mapeadas = XmlSupport.validateCollecting(XmlSupport.forSchemaValidation(xml), url).stream()
        .filter(problema -> !"aviso".equals(problema.severity()))
        .map(problema -> MapeadorCriticaXsd.mapear(problema, arquivo, catalogo))
        .toList();
    var campos = mapeadas.stream().filter(item -> conhecidos.contains(item.coluna())).toList();
    var demais = mapeadas.stream().filter(item -> !conhecidos.contains(item.coluna())).toList();
    mesclarAusentes(criticas, campos);
    if (criticas.isEmpty()) mesclarAusentes(criticas, demais);
  }

  private static void mesclarAusentes(List<Critica> destino, List<Critica> novas) {
    for (var item : novas) {
      var existe = destino.stream().anyMatch(atual -> MapeadorCriticaXsd.mesmoCampo(atual, item));
      if (!existe) destino.add(item);
    }
  }

  private void adicionarChave(List<Critica> criticas, String arquivo, String leiaute, int operacao, String cnpj, java.time.LocalDate iniValid, java.time.LocalDate fimValid, Long idEntidade, Integer linha) {
    mesclarAusentes(criticas, chaves.validar(leiaute, arquivo, linha, operacao, cnpj, iniValid, fimValid, idEntidade));
  }

  private void adicionarDependencias(List<Critica> criticas, String arquivo, D1011 modelo, Long idEntidade, Integer linha) {
    var dependenciasEvento = dependencias.validate(modelo, new ValidationContext(idEntidade, null));
    for (var problema : dependenciasEvento) {
      var coluna = problema.field() == null || problema.field().isBlank() ? "nrInsc" : problema.field();
      criticas.add(new Critica(arquivo, linha, coluna, modelo.cnpjRoot() == null ? "" : modelo.cnpjRoot(), TipoCritica.NEGOCIO,
          "D-1001 válido e vigente para o mesmo CNPJ",
          problema.message() + ". Envie e valide o D-1001 da entidade antes do D-1011.",
          caminhoNrInsc("D-1011")));
    }
  }

  private static String caminhoNrInsc(String leiaute) {
    return "/DeRE/" + evento(leiaute) + "/ideContrib/nrInsc";
  }

  private static String evento(String leiaute) {
    if ("D-1011".equals(leiaute)) return "evtPGCC";
    if ("D-1101".equals(leiaute)) return "evtBalancete";
    if ("D-1199".equals(leiaute)) return "evtFechMensal";
    return "evtInfoContrib";
  }

  private static List<RegraCampo> catalogo(String leiaute) {
    if ("D-1011".equals(leiaute)) return CatalogosCampo.d1011();
    if ("D-1101".equals(leiaute)) return CatalogosCampo.d1101();
    if ("D-1199".equals(leiaute)) return CatalogosCampo.d1199();
    return CatalogosCampo.d1001();
  }

  private static String xsd(String leiaute) {
    if ("D-1011".equals(leiaute)) return XSD_D1011;
    if ("D-1101".equals(leiaute)) return XSD_D1101;
    return XSD_D1001;
  }

  private static String vazioPara(String valor, String padrao) {
    return valor == null || valor.isBlank() ? padrao : valor;
  }

  private static String mensagemDe(Exception ex) {
    return ex.getMessage() == null || ex.getMessage().isBlank() ? "Falha ao interpretar o arquivo." : ex.getMessage();
  }

  public static String detectar(String xml) {
    try {
      var fabrica = DocumentBuilderFactory.newInstance();
      fabrica.setNamespaceAware(true);
      fabrica.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
      var dom = fabrica.newDocumentBuilder().parse(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));
      for (Node no = dom.getDocumentElement().getFirstChild(); no != null; no = no.getNextSibling()) {
        if (no.getNodeType() == Node.ELEMENT_NODE) return no.getLocalName();
      }
    } catch (Exception ignorado) {
    }
    return null;
  }
}
