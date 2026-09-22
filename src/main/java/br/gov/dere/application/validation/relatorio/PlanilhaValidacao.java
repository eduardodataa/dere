package br.gov.dere.application.validation.relatorio;

import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.List;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public final class PlanilhaValidacao {
  private PlanilhaValidacao() {}

  public static String base64(RelatorioValidacao relatorio) {
    return Base64.getEncoder().encodeToString(bytes(relatorio));
  }

  public static byte[] bytes(RelatorioValidacao relatorio) {
    try (var planilha = new XSSFWorkbook(); var saida = new ByteArrayOutputStream()) {
      var cabecalho = estiloCabecalho(planilha);
      var quebra = estiloQuebra(planilha);
      criticas(planilha, relatorio, cabecalho, quebra, estilosTipo(planilha));
      planilha.write(saida);
      return saida.toByteArray();
    } catch (Exception ex) {
      throw new IllegalStateException("Falha ao gerar relatório Excel", ex);
    }
  }

  private static void criticas(XSSFWorkbook planilha, RelatorioValidacao relatorio, XSSFCellStyle cabecalho, XSSFCellStyle quebra, java.util.Map<TipoCritica, XSSFCellStyle> tipos) {
    var aba = planilha.createSheet("Criticas");
    var titulos = List.of("Linha", "Coluna", "Tipo", "Encontrado", "Esperado", "Como corrigir");
    var cab = aba.createRow(0);
    for (int i = 0; i < titulos.size(); i++) {
      cab.createCell(i).setCellValue(titulos.get(i));
      cab.getCell(i).setCellStyle(cabecalho);
    }
    var ordenadas = relatorio.criticas().stream()
        .sorted(java.util.Comparator.comparing((Critica item) -> item.linha() == null ? Integer.MAX_VALUE : item.linha())
            .thenComparing(item -> item.coluna() == null ? "" : item.coluna()))
        .toList();
    int indice = 1;
    for (var item : ordenadas) {
      var linha = aba.createRow(indice++);
      var estilo = tipos.getOrDefault(item.tipo(), quebra);
      if (item.linha() != null) linha.createCell(0).setCellValue(item.linha());
      else linha.createCell(0).setCellValue("");
      linha.getCell(0).setCellStyle(estilo);
      preencher(linha, 1, item.coluna() == null || item.coluna().isBlank() ? "Documento" : item.coluna(), estilo);
      preencher(linha, 2, item.rotuloTipo(), estilo);
      preencher(linha, 3, item.valorEncontrado(), estilo);
      preencher(linha, 4, item.valorEsperado(), estilo);
      preencher(linha, 5, item.problema(), estilo);
      linha.setHeightInPoints(36);
    }
    aba.setAutoFilter(new CellRangeAddress(0, Math.max(1, ordenadas.size()), 0, titulos.size() - 1));
    aba.createFreezePane(0, 1);
    aba.setColumnWidth(0, 2500);
    aba.setColumnWidth(1, 5500);
    aba.setColumnWidth(2, 5000);
    aba.setColumnWidth(3, 6000);
    aba.setColumnWidth(4, 14000);
    aba.setColumnWidth(5, 16000);
  }

  private static void preencher(org.apache.poi.ss.usermodel.Row linha, int coluna, String valor, XSSFCellStyle estilo) {
    linha.createCell(coluna).setCellValue(valor == null ? "" : valor);
    linha.getCell(coluna).setCellStyle(estilo);
  }

  private static XSSFCellStyle estiloCabecalho(XSSFWorkbook planilha) {
    var estilo = planilha.createCellStyle();
    var fonte = planilha.createFont();
    fonte.setBold(true);
    fonte.setColor(IndexedColors.WHITE.getIndex());
    estilo.setFont(fonte);
    estilo.setFillForegroundColor(new XSSFColor(new byte[]{0x1F, 0x4E, 0x79}, null));
    estilo.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    estilo.setAlignment(HorizontalAlignment.LEFT);
    estilo.setVerticalAlignment(VerticalAlignment.CENTER);
    bordas(estilo);
    return estilo;
  }

  private static XSSFCellStyle estiloCorpo(XSSFWorkbook planilha) {
    var estilo = planilha.createCellStyle();
    estilo.setVerticalAlignment(VerticalAlignment.CENTER);
    bordas(estilo);
    return estilo;
  }

  private static XSSFCellStyle estiloQuebra(XSSFWorkbook planilha) {
    var estilo = estiloCorpo(planilha);
    estilo.setWrapText(true);
    return estilo;
  }

  private static java.util.Map<TipoCritica, XSSFCellStyle> estilosTipo(XSSFWorkbook planilha) {
    var mapa = new LinkedHashMap<TipoCritica, XSSFCellStyle>();
    mapa.put(TipoCritica.OBRIGATORIO, tinta(planilha, new byte[]{(byte) 0xFF, (byte) 0xE0, (byte) 0xB2}));
    mapa.put(TipoCritica.FORA_DO_PADRAO, tinta(planilha, new byte[]{(byte) 0xFF, (byte) 0xF3, (byte) 0xB0}));
    mapa.put(TipoCritica.FORA_DO_DOMINIO, tinta(planilha, new byte[]{(byte) 0xFF, (byte) 0xCD, (byte) 0xD2}));
    mapa.put(TipoCritica.NEGOCIO, tinta(planilha, new byte[]{(byte) 0xE1, (byte) 0xBE, (byte) 0xE7}));
    mapa.put(TipoCritica.ESTRUTURA, tinta(planilha, new byte[]{(byte) 0xE0, (byte) 0xE0, (byte) 0xE0}));
    return mapa;
  }

  private static XSSFCellStyle tinta(XSSFWorkbook planilha, byte[] rgb) {
    var estilo = estiloQuebra(planilha);
    estilo.setFillForegroundColor(new XSSFColor(rgb, null));
    estilo.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    return estilo;
  }

  private static void bordas(XSSFCellStyle estilo) {
    estilo.setBorderBottom(BorderStyle.THIN);
    estilo.setBorderTop(BorderStyle.THIN);
    estilo.setBorderLeft(BorderStyle.THIN);
    estilo.setBorderRight(BorderStyle.THIN);
  }
}
