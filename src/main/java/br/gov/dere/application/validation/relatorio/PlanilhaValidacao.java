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
      var titulo = estiloTitulo(planilha);
      var rotulo = estiloRotulo(planilha);
      var corpo = estiloCorpo(planilha);
      var quebra = estiloQuebra(planilha);
      resumo(planilha, relatorio, titulo, rotulo, corpo, cabecalho);
      criticas(planilha, relatorio, cabecalho, quebra, estilosTipo(planilha));
      planilha.write(saida);
      return saida.toByteArray();
    } catch (Exception ex) {
      throw new IllegalStateException("Falha ao gerar relatório Excel", ex);
    }
  }

  private static void resumo(XSSFWorkbook planilha, RelatorioValidacao relatorio, XSSFCellStyle titulo, XSSFCellStyle rotulo, XSSFCellStyle corpo, XSSFCellStyle cabecalho) {
    var aba = planilha.createSheet("Resumo");
    var linha = 0;
    var destaque = aba.createRow(linha++);
    destaque.createCell(0).setCellValue("Relatório de validação DeRE");
    destaque.getCell(0).setCellStyle(titulo);
    aba.addMergedRegion(new CellRangeAddress(0, 0, 0, 1));
    linha++;
    preencher(aba, linha++, "Resultado", relatorio.valido() ? "Válido — nenhuma crítica" : "Inválido — há críticas para correção", rotulo, corpo);
    preencher(aba, linha++, "Leiaute", relatorio.leiaute(), rotulo, corpo);
    preencher(aba, linha++, "Origem", relatorio.origem(), rotulo, corpo);
    preencher(aba, linha++, "Arquivo", relatorio.arquivo(), rotulo, corpo);
    preencher(aba, linha++, "Linhas analisadas", String.valueOf(relatorio.totalLinhas()), rotulo, corpo);
    preencher(aba, linha++, "Linhas com problema", String.valueOf(relatorio.linhasComProblema()), rotulo, corpo);
    preencher(aba, linha++, "Total de críticas", String.valueOf(relatorio.criticas().size()), rotulo, corpo);
    linha++;
    var contagens = new LinkedHashMap<TipoCritica, Integer>();
    for (var tipo : TipoCritica.values()) contagens.put(tipo, 0);
    relatorio.criticas().forEach(item -> contagens.merge(item.tipo(), 1, Integer::sum));
    var cab = aba.createRow(linha++);
    cab.createCell(0).setCellValue("Tipo de problema");
    cab.createCell(1).setCellValue("Quantidade");
    cab.getCell(0).setCellStyle(cabecalho);
    cab.getCell(1).setCellStyle(cabecalho);
    for (var entrada : contagens.entrySet()) {
      if (entrada.getValue() == 0 && relatorio.valido()) continue;
      var item = aba.createRow(linha++);
      item.createCell(0).setCellValue(entrada.getKey().rotulo());
      item.createCell(1).setCellValue(entrada.getValue());
      item.getCell(0).setCellStyle(corpo);
      item.getCell(1).setCellStyle(corpo);
    }
    linha++;
    var dica = aba.createRow(linha);
    dica.createCell(0).setCellValue("Abra a aba Críticas e filtre por Linha, Coluna ou Tipo. Cada linha indica o valor encontrado e o que o leiaute espera.");
    dica.getCell(0).setCellStyle(corpo);
    aba.addMergedRegion(new CellRangeAddress(linha, linha, 0, 1));
    aba.setColumnWidth(0, 8000);
    aba.setColumnWidth(1, 18000);
  }

  private static void criticas(XSSFWorkbook planilha, RelatorioValidacao relatorio, XSSFCellStyle cabecalho, XSSFCellStyle quebra, java.util.Map<TipoCritica, XSSFCellStyle> tipos) {
    var aba = planilha.createSheet("Criticas");
    var titulos = List.of("Arquivo", "Linha", "Coluna", "Tipo", "Valor encontrado", "Valor esperado", "Como corrigir", "Caminho XML");
    var cab = aba.createRow(0);
    for (int i = 0; i < titulos.size(); i++) {
      cab.createCell(i).setCellValue(titulos.get(i));
      cab.getCell(i).setCellStyle(cabecalho);
    }
    var ordenadas = relatorio.criticas().stream()
        .sorted(java.util.Comparator.comparing((Critica item) -> item.arquivo() == null ? "" : item.arquivo())
            .thenComparing(item -> item.linha() == null ? Integer.MAX_VALUE : item.linha())
            .thenComparing(item -> item.coluna() == null ? "" : item.coluna()))
        .toList();
    int indice = 1;
    for (var item : ordenadas) {
      var linha = aba.createRow(indice++);
      var estilo = tipos.getOrDefault(item.tipo(), quebra);
      preencher(linha, 0, item.arquivo(), estilo);
      if (item.linha() != null) linha.createCell(1).setCellValue(item.linha());
      else linha.createCell(1).setCellValue("");
      linha.getCell(1).setCellStyle(estilo);
      preencher(linha, 2, item.coluna(), estilo);
      preencher(linha, 3, item.rotuloTipo(), estilo);
      preencher(linha, 4, item.valorEncontrado(), estilo);
      preencher(linha, 5, item.valorEsperado(), estilo);
      preencher(linha, 6, item.problema(), estilo);
      preencher(linha, 7, item.caminhoXml(), estilo);
      linha.setHeightInPoints(36);
    }
    if (ordenadas.isEmpty()) {
      var linha = aba.createRow(1);
      preencher(linha, 0, "Nenhuma crítica", quebra);
    }
    aba.setAutoFilter(new CellRangeAddress(0, Math.max(1, ordenadas.size()), 0, titulos.size() - 1));
    aba.createFreezePane(0, 1);
    aba.setColumnWidth(0, 7000);
    aba.setColumnWidth(1, 2500);
    aba.setColumnWidth(2, 5500);
    aba.setColumnWidth(3, 5000);
    aba.setColumnWidth(4, 6000);
    aba.setColumnWidth(5, 14000);
    aba.setColumnWidth(6, 16000);
    aba.setColumnWidth(7, 14000);
  }

  private static void preencher(org.apache.poi.ss.usermodel.Sheet aba, int indice, String chave, String valor, XSSFCellStyle rotulo, XSSFCellStyle corpo) {
    var linha = aba.createRow(indice);
    linha.createCell(0).setCellValue(chave);
    linha.createCell(1).setCellValue(valor == null ? "" : valor);
    linha.getCell(0).setCellStyle(rotulo);
    linha.getCell(1).setCellStyle(corpo);
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

  private static XSSFCellStyle estiloTitulo(XSSFWorkbook planilha) {
    var estilo = planilha.createCellStyle();
    var fonte = planilha.createFont();
    fonte.setBold(true);
    fonte.setFontHeightInPoints((short) 16);
    estilo.setFont(fonte);
    return estilo;
  }

  private static XSSFCellStyle estiloRotulo(XSSFWorkbook planilha) {
    var estilo = planilha.createCellStyle();
    var fonte = planilha.createFont();
    fonte.setBold(true);
    estilo.setFont(fonte);
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
