package br.gov.dere.application.pgcc;

import br.gov.dere.domain.pgcc.D1011;
import br.gov.dere.domain.pgcc.PGCCAccount;
import br.gov.dere.integration.xml.XmlSupport;
import java.util.function.BiConsumer;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class D1011XmlGenerator {
  public static final String NS = "http://www.dere.gov.br/schemas/evtPGCC/v1_0_1";

  public String generate(D1011 document) throws Exception {
    var factory = DocumentBuilderFactory.newInstance();
    factory.setNamespaceAware(true);
    Document dom = factory.newDocumentBuilder().newDocument();
    var root = dom.createElementNS(NS, "DeRE"); dom.appendChild(root);
    var event = child(dom, root, "evtPGCC"); event.setAttribute("id", document.id());
    var ide = child(dom, event, "ideEvento"); text(dom, ide, "tpOper", document.operation());
    if (document.exclusionReason() != null) text(dom, ide, "motExcl", document.exclusionReason());
    if (document.processNumber() != null && !document.processNumber().isBlank()) text(dom, ide, "nrProc", document.processNumber());
    text(dom, ide, "tpAmb", document.environment()); text(dom, ide, "aplicEmi", document.application()); text(dom, ide, "verAplic", document.applicationVersion());
    var contributor = child(dom, event, "ideContrib"); text(dom, contributor, "nrInsc", document.cnpjRoot());
    var period = child(dom, event, "idePeriodo"); text(dom, period, "iniValid", document.validFrom()); if (document.validTo() != null) text(dom, period, "fimValid", document.validTo());
    if (document.operation() != 3) {
      var info = child(dom, event, "infoPGCC"); text(dom, info, "planoCtaRef", document.referenceChart()); text(dom, info, "freqEncerr", document.closingFrequency());
      var accounts = child(dom, info, "infoContas");
      for (var account : document.accounts()) appendAccount(dom, accounts, account);
    }
    return XmlSupport.serialize(dom);
  }

  private static void appendAccount(Document dom, Node parent, PGCCAccount account) {
    var node = child(dom, parent, "infoConta");
    text(dom, node, "cCta", account.accountCode()); text(dom, node, "cCtaInterna", account.internalCode()); text(dom, node, "cDbrMista", String.format("%03d", account.mixedDebitCredit()));
    text(dom, node, "nomeCta", account.name()); text(dom, node, "indCta", account.accountIndicator()); textOptional(dom, node, "descCta", account.description());
    textOptional(dom, node, "cCtaSup", account.parentCode()); text(dom, node, "cCtaRef", account.referenceCode()); text(dom, node, "nivelCta", account.level());
    text(dom, node, "natCta", account.nature()); text(dom, node, "codNat", account.natureCode()); textOptional(dom, node, "codTrib", account.taxCode());
    textOptional(dom, node, "indTribISS", account.issTaxIndicator()); textOptional(dom, node, "idLeiDisp", account.legalBasis());
    text(dom, node, "iniVig", account.validFrom()); textOptional(dom, node, "fimVig", account.validTo());
  }

  private static org.w3c.dom.Element child(Document dom, Node parent, String name) { var element = dom.createElementNS(NS, name); parent.appendChild(element); return element; }
  private static void text(Document dom, Node parent, String name, Object value) { if (value == null) throw new IllegalArgumentException("Campo obrigatório ausente: " + name); var element = child(dom, parent, name); element.setTextContent(String.valueOf(value)); }
  private static void textOptional(Document dom, Node parent, String name, Object value) { if (value != null && !String.valueOf(value).isBlank()) text(dom, parent, name, value); }
}
