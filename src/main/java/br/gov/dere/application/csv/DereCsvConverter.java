package br.gov.dere.application.csv;

import br.gov.dere.application.d1001.D1001XmlGenerator;
import br.gov.dere.domain.contributor.D1001;
import br.gov.dere.integration.xml.XmlSupport;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import org.w3c.dom.Element;

/** Conversor CSV canônico do D-1001. Listas usam ponto e vírgula. */
public class DereCsvConverter {
  private static final String HEADER="id,motExcl,nrProc,tpOper,tpAmb,aplicEmi,verAplic,nrInsc,iniValid,fimValid,novaValidadeIniValid,novaValidadeFimValid,regTribPrinc,regTribSecund,indNatTrib,tpAtividadeServFinanc,tpAtividadePlAssistSaude,tpAtividadePrognosticos,UFCredenc";
  public D1001 fromCsv(String csv){
    var lines=csv.lines().filter(s->!s.isBlank()).toList(); if(lines.size()<2)throw new IllegalArgumentException("CSV deve conter cabeçalho e uma linha");
    var names=lines.get(0).split(",",-1);var values=lines.get(1).split(",",-1);var h=new HashMap<String,Integer>();for(int i=0;i<names.length;i++)h.put(names[i].trim(),i);
    Function<String,String> get=n->{var i=h.get(n);return i==null||i>=values.length?"":values[i].trim();};
    return new D1001(get.apply("id"),integer(get,"tpOper"),optionalInteger(get,"motExcl"),empty(get,"nrProc"),integer(get,"tpAmb"),optionalInteger(get,"aplicEmi",1),get.apply("verAplic"),get.apply("nrInsc"),date(get,"iniValid"),date(get,"fimValid"),date(get,"novaValidadeIniValid"),date(get,"novaValidadeFimValid"),integer(get,"regTribPrinc"),ints(get.apply("regTribSecund")),integer(get,"indNatTrib"),list(get.apply("tpAtividadeServFinanc")),list(get.apply("tpAtividadePlAssistSaude")),list(get.apply("tpAtividadePrognosticos")),ints(get.apply("UFCredenc")));
  }
  public String toCsv(D1001 d){return HEADER+"\n"+String.join(",",csv(d.id()),csv(d.exclusionReason()),csv(d.processNumber()),csv(d.operation()),csv(d.environment()),csv(d.application()),csv(d.applicationVersion()),csv(d.cnpjRoot()),csv(d.validFrom()),csv(d.validTo()),csv(d.newValidFrom()),csv(d.newValidTo()),csv(d.primaryTaxRegime()),csv(join(d.secondaryTaxRegimes())),csv(d.taxNature()),csv(join(d.financialActivities())),csv(join(d.healthActivities())),csv(join(d.prognosticActivities())),csv(join(d.accreditedUfs())));}
  public D1001 fromXml(String xml)throws Exception{return fromCsv(xmlToCsv(xml));}
  public String xmlToCsv(String xml)throws Exception{
    var d=XmlSupport.parse(xml);var e=(Element)d.getElementsByTagNameNS(D1001XmlGenerator.NS,"evtInfoContrib").item(0);if(e==null)throw new IllegalArgumentException("XML não contém evtInfoContrib");
    Function<String,String> text=n->first(e,n)==null?"":first(e,n).getTextContent();Function<String,List<String>> vals=n->elements(e,n).stream().map(Element::getTextContent).toList();
    var ide=(Element)e.getElementsByTagNameNS(D1001XmlGenerator.NS,"ideEvento").item(0);var per=(Element)e.getElementsByTagNameNS(D1001XmlGenerator.NS,"idePeriodo").item(0);var info=(Element)e.getElementsByTagNameNS(D1001XmlGenerator.NS,"infoContrib").item(0);
    var row=String.join(",",csv(e.getAttribute("id")),csv(textFrom(ide,"motExcl")),csv(textFrom(ide,"nrProc")),csv(textFrom(ide,"tpOper")),csv(textFrom(ide,"tpAmb")),csv(textFrom(ide,"aplicEmi")),csv(textFrom(ide,"verAplic")),csv(textFrom((Element)e.getElementsByTagNameNS(D1001XmlGenerator.NS,"ideContrib").item(0),"nrInsc")),csv(textFrom(per,"iniValid")),csv(textFrom(per,"fimValid")),csv(textFrom((Element)per.getElementsByTagNameNS(D1001XmlGenerator.NS,"novaValidade").item(0),"iniValid")),csv(textFrom((Element)per.getElementsByTagNameNS(D1001XmlGenerator.NS,"novaValidade").item(0),"fimValid")),csv(textFrom(info,"regTribPrinc")),csv(String.join(";",valsFrom(info,"regTribSecund"))),csv(textFrom(info,"indNatTrib")),csv(String.join(";",groupValues(info,"servFinanc"))),csv(String.join(";",groupValues(info,"plAssistSaude"))),csv(String.join(";",groupValues(info,"prognosticos"))),csv(String.join(";",groupValues(info,"prognosticos","UFCredenc"))));
    return HEADER+"\n"+row;
  }
  private static String textFrom(Element e,String n){if(e==null)return "";var x=first(e,n);return x==null?"":x.getTextContent();}
  private static Element first(Element e,String n){if(e==null)return null;var list=e.getElementsByTagNameNS(D1001XmlGenerator.NS,n);return list.getLength()==0?null:(Element)list.item(0);}
  private static List<Element> elements(Element e,String n){if(e==null)return List.of();var l=e.getElementsByTagNameNS(D1001XmlGenerator.NS,n);var r=new ArrayList<Element>();for(int i=0;i<l.getLength();i++)r.add((Element)l.item(i));return r;}
  private static List<String> valsFrom(Element e,String n){return elements(e,n).stream().map(Element::getTextContent).toList();}
  private static List<String> groupValues(Element info,String group){var g=first(info,group);return valsFrom(g,"tpAtividade");}
  private static List<String> groupValues(Element info,String group,String item){return valsFrom(first(info,group),item);}
  private static LocalDate date(Function<String,String> g,String n){return empty(g,n)==null?null:LocalDate.parse(g.apply(n));}
  private static String empty(Function<String,String> g,String n){var s=g.apply(n);return s.isBlank()?null:s;}
  private static int integer(Function<String,String> g,String n){var s=empty(g,n);if(s==null)throw new IllegalArgumentException("Campo obrigatório ausente: "+n);return Integer.parseInt(s);}
  private static Integer optionalInteger(Function<String,String> g,String n){return optionalInteger(g,n,null);}
  private static Integer optionalInteger(Function<String,String> g,String n,Integer def){var s=empty(g,n);return s==null?def:Integer.valueOf(s);}
  private static List<Integer> ints(String s){return s==null||s.isBlank()?List.of():Arrays.stream(s.split(";|\\|")).filter(x->!x.isBlank()).map(Integer::valueOf).toList();}
  private static List<String> list(String s){return s==null||s.isBlank()?List.of():Arrays.stream(s.split(";|\\|")).filter(x->!x.isBlank()).toList();}
  private static String join(Collection<?> c){return c==null?"":String.join(";",c.stream().map(String::valueOf).toList());}
  private static String csv(Object v){var s=v==null?"":String.valueOf(v);return s.contains(",")||s.contains("\"")?"\""+s.replace("\"","\"\"")+"\"":s;}
}
