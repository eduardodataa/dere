package br.gov.dere.application.upload;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import br.gov.dere.application.transmissao.ArmazenamentoArtefatoTransmissao;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import org.junit.jupiter.api.Test;

class ArmazenamentoObjetoUploadTest {
  @Test
  void gravaELeUriS3EmMemoria() {
    var objetos = new MemoriaArmazenamentoObjetoUpload("dataa-dere");
    var uri = objetos.gravar("local/1/2026-11/abc-evento.xml", "<DeRE/>".getBytes(StandardCharsets.UTF_8), "application/xml");
    assertEquals("s3://dataa-dere/local/1/2026-11/abc-evento.xml", uri);
    assertEquals("<DeRE/>", objetos.lerTexto(uri));
  }

  @Test
  void artefatoLerResolveS3FileETexto() {
    var objetos = new MemoriaArmazenamentoObjetoUpload("dataa-dere");
    var artefatos = new ArmazenamentoArtefatoTransmissao(objetos);
    var uri = objetos.gravar("local/1/2026-11/xyz-evento.xml", "<ok/>".getBytes(StandardCharsets.UTF_8), "application/xml");
    assertEquals("<ok/>", artefatos.ler(uri));
    assertEquals("<inline/>", artefatos.ler("<inline/>"));
    assertEquals(uri, artefatos.guardar("id", "evento", uri));
  }

  @Test
  void pastaAmbienteMapeiaDeployment() {
    assertEquals("prod", PastaAmbienteUpload.de("producao"));
    assertEquals("hml", PastaAmbienteUpload.de("homologacao"));
    assertEquals("stg", PastaAmbienteUpload.de("staging"));
    assertEquals("local", PastaAmbienteUpload.de("local"));
  }

  @Test
  void chaveRecusaTravessia() {
    assertThrows(IllegalArgumentException.class, () -> ChaveObjetoUpload.sanitizarNome("../x.xml"));
    assertThrows(IllegalArgumentException.class, () -> ChaveObjetoUpload.sanitizarNome("/abs.xml"));
    var chave = ChaveObjetoUpload.montar("producao", 4L, "Fundação Petrobras/Seguridade Social", "abc/123", "Evt Info.XML");
    assertTrue(chave.startsWith("prod/4-petros/"));
    assertFalse(chave.split("/")[1].contains("/"));
    assertEquals("4-petros", chave.split("/")[1]);
    assertTrue(chave.endsWith("abc_123-evt_info.xml"));
    assertEquals("1-efpc-teste", PastaEntidadeUpload.de(1L, "Entidade EFPC de teste"));
    assertEquals("3-familia-previdencia", PastaEntidadeUpload.de(3L, "Fundação Família Previdência"));
    assertEquals("5-albaprev", PastaEntidadeUpload.de(5L, "Albaprev Instituto de Previdência"));
    assertEquals("6-actuarial", PastaEntidadeUpload.de(6L, "Actuarial Administradora de Fundos"));
    assertEquals("7-oab-pr", PastaEntidadeUpload.de(7L, "Fundo de Pensão da Ordem dos Advogados do Paraná"));
    assertEquals("8-alpha-beta", PastaEntidadeUpload.de(8L, "Alpha/Beta"));
  }

  @Test
  void reservaLocalQuandoS3Falha() throws Exception {
    var raiz = Files.createTempDirectory("dere-objetos");
    var remoto = new ArmazenamentoObjetoUpload() {
      @Override
      public String gravar(String chave, byte[] conteudo, String contentType) {
        throw new IllegalStateException("s3 fora");
      }

      @Override
      public byte[] lerBytes(String uri) {
        throw new IllegalStateException("s3 fora");
      }

      @Override
      public String lerTexto(String uri) {
        return new String(lerBytes(uri), StandardCharsets.UTF_8);
      }
    };
    var objetos = new ArmazenamentoComReservaLocal(remoto, raiz);
    var uri = objetos.gravar("prod/4-petros/2026-11/abc-evento.xml", "<DeRE/>".getBytes(StandardCharsets.UTF_8), "application/xml");
    assertTrue(uri.startsWith("file:"));
    assertTrue(uri.contains("4-petros"));
    assertEquals("<DeRE/>", objetos.lerTexto(uri));
  }
}
