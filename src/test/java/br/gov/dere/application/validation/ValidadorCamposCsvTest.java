package br.gov.dere.application.validation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import br.gov.dere.application.validation.relatorio.PlanilhaValidacao;
import br.gov.dere.application.validation.relatorio.RelatorioValidacao;
import br.gov.dere.application.validation.relatorio.TipoCritica;
import java.io.ByteArrayInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;

class ValidadorCamposCsvTest {
  @Test
  void csvValidoD1001NaoGeraCriticaDeCampo() throws Exception {
    var csv = Files.readString(Path.of("examples/csv/d1001-informacoes-contribuinte.csv"));
    var criticas = ValidadorCamposCsv.validar("D-1001", "ok.csv", csv);
    assertEquals(0, criticas.size(), () -> criticas.toString());
  }

  @Test
  void apontaLinhaColunaDominioEValorEsperado() {
    var csv = """
        id;motExcl;nrProc;tpOper;tpAmb;aplicEmi;verAplic;nrInsc;iniValid;fimValid;novaValidadeIniValid;novaValidadeFimValid;regTribPrinc;regTribSecund;indNatTrib;tpAtividadeServFinanc;tpAtividadePlAssistSaude;tpAtividadePrognosticos;UFCredenc
        DeRE10011234567820261001120000000000000001;;;9;2;1;dere-poc/0.1;12345678;01/10/2026;;;;9;;0;;;;
        """;
    var criticas = ValidadorCamposCsv.validar("D-1001", "erro.csv", csv);
    var operacao = criticas.stream().filter(item -> "tpOper".equals(item.coluna())).findFirst().orElseThrow();
    assertEquals(2, operacao.linha());
    assertEquals("9", operacao.valorEncontrado());
    assertEquals(TipoCritica.FORA_DO_DOMINIO, operacao.tipo());
    assertTrue(operacao.valorEsperado().contains("1 = Inclusão"));
    assertTrue(operacao.problema().toLowerCase().contains("domínio"));
  }

  @Test
  void apontaIdForaDoPadrao() {
    var csv = """
        id;tpOper;tpAmb;aplicEmi;verAplic;nrInsc;iniValid;regTribPrinc;indNatTrib
        CURTO;1;2;1;dere-poc/0.1;12345678;01/10/2026;9;0
        """;
    var criticas = ValidadorCamposCsv.validar("D-1001", "id.csv", csv);
    var id = criticas.stream().filter(item -> "id".equals(item.coluna())).findFirst().orElseThrow();
    assertEquals(TipoCritica.FORA_DO_PADRAO, id.tipo());
    assertEquals("CURTO", id.valorEncontrado());
    assertTrue(id.valorEsperado().contains("42"));
  }

  @Test
  void apontaCampoObrigatorioVazio() {
    var csv = """
        id;tpOper;tpAmb;aplicEmi;verAplic;nrInsc;iniValid;regTribPrinc;indNatTrib
        DeRE10011234567820261001120000000000000001;1;2;1;dere-poc/0.1;;01/10/2026;9;0
        """;
    var criticas = ValidadorCamposCsv.validar("D-1001", "obrig.csv", csv);
    var nrInsc = criticas.stream().filter(item -> "nrInsc".equals(item.coluna())).findFirst().orElseThrow();
    assertEquals(TipoCritica.OBRIGATORIO, nrInsc.tipo());
    assertEquals(2, nrInsc.linha());
  }

  @Test
  void d1011ApontaContaComPontoComoForaDoPadrao() throws Exception {
    var csv = Files.readString(Path.of("examples/csv/d1011-pgcc-previc.csv"));
    var criticas = ValidadorCamposCsv.validar("D-1011", "previc.csv", csv);
    assertTrue(criticas.stream().anyMatch(item -> "cCta".equals(item.coluna()) && item.tipo() == TipoCritica.FORA_DO_PADRAO));
  }

  @Test
  void excelListaCriticaNaAba() throws Exception {
    var csv = """
        id;tpOper;tpAmb;aplicEmi;verAplic;nrInsc;iniValid;regTribPrinc;indNatTrib
        CURTO;9;2;1;dere-poc/0.1;12345678;01/10/2026;9;0
        """;
    var criticas = ValidadorCamposCsv.validar("D-1001", "rel.csv", csv);
    var relatorio = RelatorioValidacao.de("D-1001", "CSV", "rel.csv", 1, criticas);
    try (var planilha = new XSSFWorkbook(new ByteArrayInputStream(PlanilhaValidacao.bytes(relatorio)))) {
      assertEquals("Criticas", planilha.getSheetAt(0).getSheetName());
      var aba = planilha.getSheetAt(0);
      assertEquals("Linha", aba.getRow(0).getCell(0).getStringCellValue());
      assertEquals("Coluna", aba.getRow(0).getCell(1).getStringCellValue());
      assertEquals("Tipo", aba.getRow(0).getCell(2).getStringCellValue());
      assertEquals("Encontrado", aba.getRow(0).getCell(3).getStringCellValue());
      assertEquals("Esperado", aba.getRow(0).getCell(4).getStringCellValue());
      assertEquals("Como corrigir", aba.getRow(0).getCell(5).getStringCellValue());
      var colunas = new java.util.ArrayList<String>();
      for (int i = 1; i <= aba.getLastRowNum(); i++) colunas.add(aba.getRow(i).getCell(1).getStringCellValue());
      assertTrue(colunas.contains("id"));
      assertTrue(colunas.contains("tpOper"));
    }
  }
}
