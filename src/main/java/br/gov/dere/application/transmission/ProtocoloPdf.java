package br.gov.dere.application.transmission;

import br.gov.dere.application.validation.TabelaCsv;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public final class ProtocoloPdf {
  private static final DateTimeFormatter DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm").withZone(ZoneId.systemDefault());

  private ProtocoloPdf() {}

  public static byte[] protocolo(TransmissionView envio) {
    try (var saida = new ByteArrayOutputStream()) {
      var documento = new Document(PageSize.A4, 36, 36, 40, 36);
      PdfWriter.getInstance(documento, saida);
      documento.open();
      var titulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);
      var rotulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9);
      var texto = FontFactory.getFont(FontFactory.HELVETICA, 9);
      documento.add(new Paragraph("Protocolo DeRE — " + nulo(envio.layout()), titulo));
      documento.add(new Paragraph(" "));
      documento.add(tabelaCabecalho(envio, rotulo, texto));
      documento.add(new Paragraph(" "));
      var csv = envio.csv();
      if (csv != null && !csv.isBlank()) {
        var tabela = TabelaCsv.analisar(csv);
        int registro = 1;
        for (var linha : tabela.linhas()) {
          documento.add(new Paragraph("Registro " + registro++, rotulo));
          documento.add(new Paragraph(" "));
          documento.add(tabelaCampos(tabela, linha, rotulo, texto));
          documento.add(new Paragraph(" "));
        }
        if (tabela.linhas().isEmpty()) {
          documento.add(tabelaCamposVazios(tabela.cabecalhos(), rotulo, texto));
        }
      } else {
        documento.add(new Paragraph("CSV do protocolo não disponível.", texto));
      }
      documento.close();
      return saida.toByteArray();
    } catch (Exception ex) {
      throw new IllegalStateException("Falha ao gerar PDF do protocolo", ex);
    }
  }

  public static byte[] relatorio(java.util.List<TransmissionView> linhas) {
    try (var saida = new ByteArrayOutputStream()) {
      var documento = new Document(PageSize.A4.rotate(), 28, 28, 32, 28);
      PdfWriter.getInstance(documento, saida);
      documento.open();
      var titulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 13);
      var rotulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 8);
      var texto = FontFactory.getFont(FontFactory.HELVETICA, 8);
      documento.add(new Paragraph("Relatório de transmissões DeRE", titulo));
      documento.add(new Paragraph(" "));
      var colunas = new String[] { "Layout", "Arquivo", "Operação", "Id", "Protocolo", "Recibo", "Status", "Data de envio" };
      var tabela = new PdfPTable(new float[] { 10, 16, 10, 22, 16, 16, 14, 14 });
      tabela.setWidthPercentage(100);
      for (var coluna : colunas) tabela.addCell(celula(coluna, rotulo, new Color(17, 24, 39), Color.WHITE, true));
      for (var linha : linhas) {
        tabela.addCell(celula(nulo(linha.layout()), texto, Color.WHITE, Color.BLACK, false));
        tabela.addCell(celula(nulo(linha.sourceName()), texto, Color.WHITE, Color.BLACK, false));
        tabela.addCell(celula(operacao(linha.operationType()), texto, Color.WHITE, Color.BLACK, false));
        tabela.addCell(celula(nulo(linha.eventIdentifier()), texto, Color.WHITE, Color.BLACK, false));
        tabela.addCell(celula(nulo(linha.protocol()), texto, Color.WHITE, Color.BLACK, false));
        tabela.addCell(celula(nulo(linha.receiptNumber()), texto, Color.WHITE, Color.BLACK, false));
        tabela.addCell(celula(nulo(linha.status()), texto, Color.WHITE, Color.BLACK, false));
        tabela.addCell(celula(linha.sentAt() == null ? "—" : DATA.format(linha.sentAt()), texto, Color.WHITE, Color.BLACK, false));
      }
      documento.add(tabela);
      documento.close();
      return saida.toByteArray();
    } catch (Exception ex) {
      throw new IllegalStateException("Falha ao gerar relatório PDF", ex);
    }
  }

  private static PdfPTable tabelaCabecalho(TransmissionView envio, Font rotulo, Font texto) {
    var tabela = new PdfPTable(new float[] { 28, 72 });
    tabela.setWidthPercentage(100);
    linha(tabela, "Layout", nulo(envio.layout()), rotulo, texto);
    linha(tabela, "Arquivo", nulo(envio.sourceName()), rotulo, texto);
    linha(tabela, "Operação", operacao(envio.operationType()), rotulo, texto);
    linha(tabela, "Id do evento", nulo(envio.eventIdentifier()), rotulo, texto);
    linha(tabela, "Protocolo", nulo(envio.protocol()), rotulo, texto);
    linha(tabela, "Recibo", nulo(envio.receiptNumber()), rotulo, texto);
    linha(tabela, "Status", nulo(envio.status()), rotulo, texto);
    linha(tabela, "Data de envio", envio.sentAt() == null ? "—" : DATA.format(envio.sentAt()), rotulo, texto);
    return tabela;
  }

  private static PdfPTable tabelaCampos(TabelaCsv tabela, TabelaCsv.Linha linha, Font rotulo, Font texto) {
    var pdf = new PdfPTable(new float[] { 32, 68 });
    pdf.setWidthPercentage(100);
    pdf.addCell(celula("Campo", rotulo, new Color(17, 24, 39), Color.WHITE, true));
    pdf.addCell(celula("Valor", rotulo, new Color(17, 24, 39), Color.WHITE, true));
    for (var coluna : tabela.cabecalhos()) {
      pdf.addCell(celula(coluna, rotulo, new Color(241, 245, 249), Color.BLACK, false));
      pdf.addCell(celula(tabela.valor(linha, coluna), texto, Color.WHITE, Color.BLACK, false));
    }
    return pdf;
  }

  private static PdfPTable tabelaCamposVazios(java.util.List<String> colunas, Font rotulo, Font texto) {
    var pdf = new PdfPTable(new float[] { 32, 68 });
    pdf.setWidthPercentage(100);
    pdf.addCell(celula("Campo", rotulo, new Color(17, 24, 39), Color.WHITE, true));
    pdf.addCell(celula("Valor", rotulo, new Color(17, 24, 39), Color.WHITE, true));
    for (var coluna : colunas) {
      pdf.addCell(celula(coluna, rotulo, new Color(241, 245, 249), Color.BLACK, false));
      pdf.addCell(celula("", texto, Color.WHITE, Color.BLACK, false));
    }
    return pdf;
  }

  private static void linha(PdfPTable tabela, String campo, String valor, Font rotulo, Font texto) {
    tabela.addCell(celula(campo, rotulo, new Color(241, 245, 249), Color.BLACK, false));
    tabela.addCell(celula(valor, texto, Color.WHITE, Color.BLACK, false));
  }

  private static PdfPCell celula(String valor, Font fonte, Color fundo, Color cor, boolean cabecalho) {
    var celula = new PdfPCell(new Phrase(valor == null ? "" : valor, fonte));
    celula.setBackgroundColor(fundo);
    celula.setPadding(5);
    celula.setHorizontalAlignment(Element.ALIGN_LEFT);
    celula.getPhrase().getFont().setColor(cor);
    return celula;
  }

  private static String operacao(String codigo) {
    if ("1".equals(codigo)) return "Cadastro";
    if ("2".equals(codigo)) return "Edição";
    if ("3".equals(codigo)) return "Exclusão";
    return codigo == null || codigo.isBlank() ? "—" : codigo;
  }

  private static String nulo(String valor) {
    return valor == null || valor.isBlank() ? "—" : valor;
  }
}
