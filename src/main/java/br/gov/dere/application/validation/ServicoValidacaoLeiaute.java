package br.gov.dere.application.validation;

import br.gov.dere.application.csv.DereCsvConverter;
import br.gov.dere.application.d1001.D1001XmlGenerator;
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
  private final DereCsvConverter csvD1001 = new DereCsvConverter();
  private final D1001XmlGenerator xmlD1001 = new D1001XmlGenerator();
  private final D1011CsvConverter csvD1011 = new D1011CsvConverter();
  private final D1011XmlGenerator xmlD1011 = new D1011XmlGenerator();
  private final D1011DependencyValidator dependencias;

  public ServicoValidacaoLeiaute(D1011DependencyValidator dependencias) {
    this.dependencias = dependencias;
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
      } else {
        var modelo = csvD1001.fromCsv(csv);
        canonico = csvD1001.toCsv(modelo);
        xml = xmlD1001.generate(modelo);
        adicionarXsd(criticas, arquivo, xml, XSD_D1001, CatalogosCampo.d1001());
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
    var eventoEsperado = "D-1011".equals(leiaute) ? "evtPGCC" : "evtInfoContrib";
    var detectado = detectar(xml);
    if (detectado != null && !eventoEsperado.equals(detectado)) {
      criticas.add(new Critica(arquivo, 1, "Documento", detectado, TipoCritica.ESTRUTURA, eventoEsperado,
          "Leiaute esperado: " + leiaute + " (" + eventoEsperado + "). Encontrado: " + detectado + ".", "/DeRE"));
    }
    var catalogo = "D-1011".equals(leiaute) ? CatalogosCampo.d1011() : CatalogosCampo.d1001();
    var xsd = "D-1011".equals(leiaute) ? XSD_D1011 : XSD_D1001;
    adicionarXsd(criticas, arquivo, xml, xsd, catalogo);
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
      } else {
        var modelo = csvD1001.fromXml(xml);
        csv = csvD1001.toCsv(modelo);
        mesclarAusentes(criticas, ValidadorCamposCsv.validar(leiaute, arquivo, csv));
        var tabela = TabelaCsv.analisar(csv);
        quantidadeLinhas = tabela.linhas().size();
        if (!tabela.linhas().isEmpty()) {
          ValidadorCamposCsv.adicionarDivergenciaCnpj(criticas, arquivo, tabela.linhas().get(0).numero(), tabela.valor(tabela.linhas().get(0), "nrInsc"), cnpjEsperado, caminhoNrInsc(leiaute));
        }
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
    return "D-1011".equals(leiaute) ? "/DeRE/evtPGCC/ideContrib/nrInsc" : "/DeRE/evtInfoContrib/ideContrib/nrInsc";
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
