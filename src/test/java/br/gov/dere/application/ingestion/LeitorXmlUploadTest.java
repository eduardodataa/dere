package br.gov.dere.application.ingestion;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.junit.jupiter.api.Test;

class LeitorXmlUploadTest {
  @Test
  void leXmlDentroDoLimiteERejeitaAcima() throws Exception {
    var xml = "<DeRE>ok</DeRE>";
    var lido = LeitorXmlUpload.ler(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)), "evento.xml", 1024);
    assertEquals(xml, lido.get(0).xml());
    var grande = new byte[2048];
    var erro = assertThrows(IllegalArgumentException.class,
        () -> LeitorXmlUpload.ler(new ByteArrayInputStream(grande), "evento.xml", 1024));
    assertTrue(erro.getMessage().contains("1024"));
  }

  @Test
  void leZipDeXmlEIgnoraEntradaQueNaoEXml() throws Exception {
    var zip = zip(new Entrada("a.xml", "<a/>"), new Entrada("nota.txt", "ignorar"), new Entrada("b.xml", "<b/>"));
    var lido = LeitorXmlUpload.ler(new ByteArrayInputStream(zip), "lote.zip", 1024);
    assertEquals(2, lido.size());
    assertEquals("a.xml", lido.get(0).nome());
    assertEquals("<b/>", lido.get(1).xml());
  }

  @Test
  void rejeitaZipBombaAntesDeMaterializarTudo() {
    var zip = zip(new Entrada("bomba.xml", "0".repeat(200_000)));
    var erro = assertThrows(IllegalArgumentException.class,
        () -> LeitorXmlUpload.ler(new ByteArrayInputStream(zip), "bomba.zip", 8 * 1024));
    assertTrue(erro.getMessage().contains("descompactados"));
  }

  @Test
  void ignoraNomeComTravessiaDeDiretorio() throws Exception {
    var zip = zip(new Entrada("../segredo.xml", "<x/>"), new Entrada("ok.xml", "<ok/>"));
    var lido = LeitorXmlUpload.ler(new ByteArrayInputStream(zip), "lote.zip", 1024);
    assertEquals(1, lido.size());
    assertEquals("ok.xml", lido.get(0).nome());
  }

  private static byte[] zip(Entrada... entradas) {
    var saida = new ByteArrayOutputStream();
    try (var zip = new ZipOutputStream(saida)) {
      for (var entrada : entradas) {
        zip.putNextEntry(new ZipEntry(entrada.nome()));
        zip.write(entrada.conteudo().getBytes(StandardCharsets.UTF_8));
        zip.closeEntry();
      }
    } catch (Exception ex) {
      throw new IllegalStateException(ex);
    }
    return saida.toByteArray();
  }

  private record Entrada(String nome, String conteudo) {}
}
