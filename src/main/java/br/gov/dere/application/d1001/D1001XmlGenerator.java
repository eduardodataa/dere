package br.gov.dere.application.d1001;

import br.gov.dere.domain.contributor.D1001;
import br.gov.dere.integration.xml.XmlSupport;
import java.util.List;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class D1001XmlGenerator {
  public static final String NS = "http://www.dere.gov.br/schemas/evtInfoContrib/v1_0_1";
  public String generate(D1001 d) throws Exception {
    var f=DocumentBuilderFactory.newInstance(); f.setNamespaceAware(true); Document doc=f.newDocumentBuilder().newDocument();
    var root=doc.createElementNS(NS,"DeRE"); doc.appendChild(root); var evt=child(doc,root,"evtInfoContrib"); evt.setAttribute("id",d.id());
    var ide=child(doc,evt,"ideEvento"); text(doc,ide,"tpOper",d.operation()); if(d.exclusionReason()!=null)text(doc,ide,"motExcl",d.exclusionReason()); if(d.processNumber()!=null&&!d.processNumber().isBlank())text(doc,ide,"nrProc",d.processNumber()); text(doc,ide,"tpAmb",d.environment()); text(doc,ide,"aplicEmi",d.application()); text(doc,ide,"verAplic",d.applicationVersion());
    var contrib=child(doc,evt,"ideContrib"); text(doc,contrib,"nrInsc",d.cnpjRoot()); var period=child(doc,evt,"idePeriodo"); text(doc,period,"iniValid",d.validFrom()); if(d.validTo()!=null)text(doc,period,"fimValid",d.validTo()); if(d.newValidFrom()!=null){var nv=child(doc,period,"novaValidade");text(doc,nv,"iniValid",d.newValidFrom());if(d.newValidTo()!=null)text(doc,nv,"fimValid",d.newValidTo());}
    var info=child(doc,evt,"infoContrib"); text(doc,info,"regTribPrinc",d.primaryTaxRegime()); for(var v:d.secondaryTaxRegimes())text(doc,info,"regTribSecund",v); text(doc,info,"indNatTrib",d.taxNature());
    activities(doc,info,"servFinanc",d.financialActivities(),List.of(),false); activities(doc,info,"plAssistSaude",d.healthActivities(),List.of(),false); activities(doc,info,"prognosticos",d.prognosticActivities(),d.accreditedUfs(),true);
    return XmlSupport.serialize(doc);
  }
  private static void activities(Document d,Node parent,String group,List<String> values,List<Integer> ufs,boolean withUf){if(values.isEmpty()&&(!withUf||ufs.isEmpty()))return;var g=child(d,parent,group);var ts=child(d,g,"tpAtividades");for(String v:values)text(d,ts,"tpAtividade",v);if(withUf&&!ufs.isEmpty()){var us=child(d,g,"UFsCredenc");for(Integer uf:ufs)text(d,us,"UFCredenc",String.format("%02d",uf));}}
  private static org.w3c.dom.Element child(Document d,Node p,String n){var e=d.createElementNS(NS,n);p.appendChild(e);return e;} private static void text(Document d,Node p,String n,Object v){var e=child(d,p,n);e.setTextContent(String.valueOf(v));}
}
