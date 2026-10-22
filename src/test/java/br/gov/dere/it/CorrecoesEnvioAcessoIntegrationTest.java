package br.gov.dere.it;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.gov.dere.application.configuracao.ConfiguracaoIntegracaoService;
import br.gov.dere.application.transmissao.TransacaoIsolada;
import br.gov.dere.application.transmissao.TransmissaoGovernoPort;
import br.gov.dere.application.transmissao.TransmissaoPollingService;
import br.gov.dere.domain.EventStatus;
import br.gov.dere.domain.transmissao.EstadoTransmissao;
import br.gov.dere.domain.transmissao.ResultadoConsulta;
import br.gov.dere.integration.receita.ExcecaoTransmissaoGoverno;
import br.gov.dere.integration.signature.CertificadoTesteLocal;
import br.gov.dere.persistence.DereAppSettingEntity;
import br.gov.dere.persistence.DereAppSettingRepository;
import br.gov.dere.persistence.DereAuditoriaValidacaoEntity;
import br.gov.dere.persistence.DereAuditoriaValidacaoRepository;
import br.gov.dere.persistence.DereBatchEntity;
import br.gov.dere.persistence.DereBatchRepository;
import br.gov.dere.persistence.DereEventEntity;
import br.gov.dere.persistence.DereEventErrorRepository;
import br.gov.dere.persistence.DereEventRepository;
import br.gov.dere.persistence.DereTentativaTransmissaoRepository;
import java.io.ByteArrayOutputStream;
import java.util.regex.Pattern;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

class CorrecoesEnvioAcessoIntegrationTest extends IntegrationTestSupport {
  private static final Pattern ID_EVENTO = Pattern.compile("DeRE[0-9]{38}");

  @MockitoBean(name = "routingTransmissaoGovernoAdapter", enforceOverride = true)
  private TransmissaoGovernoPort governo;

  @Autowired private TransmissaoPollingService polling;
  @Autowired private TransacaoIsolada isolada;
  @Autowired private DereEventRepository eventos;
  @Autowired private DereEventErrorRepository erros;
  @Autowired private DereBatchRepository lotes;
  @Autowired private DereTentativaTransmissaoRepository tentativas;
  @Autowired private DereAppSettingRepository ajustes;
  @Autowired private DereAuditoriaValidacaoRepository auditorias;

  @AfterEach
  void limparCommitado() {
    isolada.executar(() -> {
      erros.deleteAll();
      tentativas.deleteAll();
      eventos.deleteAll();
      lotes.deleteAll();
      ajustes.deleteById(ConfiguracaoIntegracaoService.REAL_ADAPTER);
      return null;
    });
  }

  @Test
  void apisExigemTokenEIgnoramUsuarioForjado() throws Exception {
    mockMvc.perform(get("/api/ambiente")).andExpect(status().isOk());
    mockMvc.perform(get("/api/admin/entities")).andExpect(status().isUnauthorized());
    mockMvc.perform(get("/api/admin/entities").header("Authorization", "Bearer token-invalido"))
        .andExpect(status().isUnauthorized())
        .andExpect(jsonPath("$.message", containsString("Sessão")));

    var operador = operador();
    mockMvc.perform(get("/api/admin/users").with(sessaoDe(operador)).header("X-User-Id", masterId))
        .andExpect(status().isForbidden());
    mockMvc.perform(get("/api/admin/entities").with(sessaoDe(operador)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[*].cnpjRoot", not(hasItem("01914904"))));
    autenticado(get("/api/admin/entities"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[*].cnpjRoot", hasItem("01914904")));
  }

  @Test
  void relatorioTentativasEValidacaoExigemVinculo() throws Exception {
    var operador = operador();
    var auditoria = auditorias.save(new DereAuditoriaValidacaoEntity(masterId, entityId, "SEND", "D-1001", false, "erro", "<xml/>", "a;b"));
    autenticado(get("/api/reports/" + auditoria.getId()))
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("<xml/>")));
    mockMvc.perform(get("/api/reports/" + auditoria.getId())).andExpect(status().isUnauthorized());
    mockMvc.perform(get("/api/reports/" + auditoria.getId()).with(sessaoDe(operador))).andExpect(status().isForbidden());

    var evento = new DereEventEntity("D-1001", "IT-ACESSO-" + System.nanoTime(), "restricted-production", "nota_2026_001", "<evt/>");
    evento.ownedBy(masterId, entityId);
    evento = eventos.save(evento);
    autenticado(get("/api/transmissions/events/" + evento.getId() + "/attempts")).andExpect(status().isOk());
    mockMvc.perform(get("/api/transmissions/events/" + evento.getId() + "/attempts").with(sessaoDe(operador)))
        .andExpect(status().isForbidden());

    mockMvc.perform(post("/api/validation/csv-to-xml")
            .with(sessaoDe(operador))
            .header("X-Entity-Id", entityId)
            .contentType(MediaType.parseMediaType("text/csv"))
            .content("id\n"))
        .andExpect(status().isForbidden());
  }

  @Test
  void consultaAgendadaUsaReceitaNoLoteRealEMockNoSimulado() throws Exception {
    ligarAdaptadorCommitado();
    var real = loteDevido("2.202610.0000000000000011111", false);
    var simulado = loteDevido("1.20261022120000.000001", true);
    when(governo.query("2.202610.0000000000000011111")).thenReturn(new ResultadoConsulta(
        "2.202610.0000000000000011111", EstadoTransmissao.ACCEPTED, "2", "RECIBO-OFICIAL", "<ok/>", 200));

    polling.pollDueBatches();

    var eventoReal = isolada.executar(() -> eventos.findById(real[1]).orElseThrow());
    var eventoSimulado = isolada.executar(() -> eventos.findById(simulado[1]).orElseThrow());
    org.junit.jupiter.api.Assertions.assertEquals("RECIBO-OFICIAL", eventoReal.getReceiptNumber());
    org.junit.jupiter.api.Assertions.assertEquals(EventStatus.ACCEPTED, eventoReal.getStatus());
    org.junit.jupiter.api.Assertions.assertTrue(eventoSimulado.getReceiptNumber().startsWith("REC-"));
    org.junit.jupiter.api.Assertions.assertEquals(EventStatus.ACCEPTED, eventoSimulado.getStatus());
    verify(governo).query("2.202610.0000000000000011111");
    verify(governo, never()).query("1.20261022120000.000001");
  }

  @Test
  void reciboFalsoDeLoteRealNaoEncerraAConsulta() throws Exception {
    ligarAdaptadorCommitado();
    var protocolo = "2.202610.0000000000000022222";
    long eventoId = isolada.executar(() -> {
      var evento = new DereEventEntity("D-1001", "IT-FALSO-" + System.nanoTime(), "restricted-production", "nota_2026_001", "<evt/>");
      evento.ownedBy(masterId, entityId);
      evento.source("real.xml", null, false);
      evento = eventos.saveAndFlush(evento);
      var lote = new DereBatchEntity("<lote/>");
      lote.ownedBy(masterId, entityId);
      lote = lotes.saveAndFlush(lote);
      lote.received(protocolo);
      lote.completed(EventStatus.ACCEPTED, "<falso/>");
      lotes.saveAndFlush(lote);
      evento.attachBatch(lote.getId());
      evento.processed(EventStatus.ACCEPTED, "REC-" + lote.getId());
      eventos.saveAndFlush(evento);
      return evento.getId();
    });
    when(governo.query(protocolo)).thenReturn(new ResultadoConsulta(protocolo, EstadoTransmissao.ACCEPTED, "2", "RECIBO-OFICIAL", "<ok/>", 200));

    polling.pollDueBatches();

    var evento = isolada.executar(() -> eventos.findById(eventoId).orElseThrow());
    org.junit.jupiter.api.Assertions.assertEquals("RECIBO-OFICIAL", evento.getReceiptNumber());
    verify(governo).query(protocolo);
  }

  @Test
  void mockNaoAceitaLoteRealNemChamadaSemVinculo() throws Exception {
    mockMvc.perform(post("/api/mock/dere/batches/1/query")).andExpect(status().isUnauthorized());

    var simulado = loteNaTransacao("MOCK-IT", true);
    autenticado(post("/api/mock/dere/batches/" + simulado + "/query"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value("ACCEPTED"));

    var operador = operador();
    var deOutro = loteNaTransacao("MOCK-OUTRO", true);
    mockMvc.perform(post("/api/mock/dere/batches/" + deOutro + "/query").with(sessaoDe(operador)))
        .andExpect(status().isForbidden());

    ligarAdaptador();
    var comAdaptador = loteNaTransacao("MOCK-REAL", true);
    autenticado(post("/api/mock/dere/batches/" + comAdaptador + "/query")).andExpect(status().isConflict());
    org.junit.jupiter.api.Assertions.assertNotEquals(EventStatus.ACCEPTED, eventos.findByBatchId(comAdaptador).get(0).getStatus());

    desligarAdaptador();
    var naoSimulado = loteNaTransacao("MOCK-NAO-SIM", false);
    autenticado(post("/api/mock/dere/batches/" + naoSimulado + "/query")).andExpect(status().isConflict());
  }

  @Test
  void envioRealSemCertificadoA1ERecusado() throws Exception {
    ligarAdaptador();
    var csv = withUniqueId(fixture("envio-ciclo/1001-incluir.csv"));
    simulate("D-1001", "CSV", "1001.csv", csv)
        .andExpect(status().isUnprocessableEntity())
        .andExpect(jsonPath("$.message", containsString("certificado A1")));
    org.junit.jupiter.api.Assertions.assertTrue(eventos.findByEventIdentifier(idEvento(csv)).isEmpty());
    verify(governo, never()).submit(any(), any());
  }

  @Test
  void timeoutMantemResultadoDesconhecidoENaoReenvia() throws Exception {
    ligarAdaptador();
    guardarA1();
    when(governo.submit(any(), any())).thenThrow(new ExcecaoTransmissaoGoverno(503, true, "<erro/>"));
    var csv = withUniqueId(fixture("envio-ciclo/1001-incluir.csv"));

    simulate("D-1001", "CSV", "1001.csv", csv)
        .andExpect(status().isBadGateway())
        .andExpect(jsonPath("$.message", containsString("pode ter recebido")))
        .andExpect(jsonPath("$.erro.inconclusivo").value(true));

    var gravado = isolada.executar(() -> eventos.findByEventIdentifier(idEvento(csv)).orElseThrow());
    org.junit.jupiter.api.Assertions.assertEquals(EventStatus.RESULTADO_DESCONHECIDO, gravado.getStatus());
    org.junit.jupiter.api.Assertions.assertNull(gravado.getBatchId() == null ? null : lotes.findById(gravado.getBatchId()).orElseThrow().getProtocol());

    simulate("D-1001", "CSV", "1001.csv", csv)
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.enviado").value(false))
        .andExpect(jsonPath("$.validacao.criticas[*].problema", hasItem(containsString("não teve confirmação"))));
    verify(governo, times(1)).submit(any(), any());
  }

  @Test
  void recusaDefinitivaGravaRejeicao() throws Exception {
    ligarAdaptador();
    guardarA1();
    when(governo.submit(any(), any())).thenThrow(new ExcecaoTransmissaoGoverno(401, false, "<erro/>"));
    var csv = withUniqueId(fixture("envio-ciclo/1001-incluir.csv"));

    simulate("D-1001", "CSV", "1001.csv", csv).andExpect(status().isUnauthorized());

    var gravado = isolada.executar(() -> eventos.findByEventIdentifier(idEvento(csv)).orElseThrow());
    org.junit.jupiter.api.Assertions.assertEquals(EventStatus.REJECTED, gravado.getStatus());
    verify(governo, times(1)).submit(any(), any());
  }

  private long operador() {
    return access.create("operador-" + System.nanoTime(), "segredo", "Operador").getId();
  }

  private RequestPostProcessor sessaoDe(long userId) {
    var token = tokens.emitir(userId);
    return request -> {
      request.addHeader("Authorization", "Bearer " + token);
      return request;
    };
  }

  private void ligarAdaptador() {
    var atual = ajustes.findById(ConfiguracaoIntegracaoService.REAL_ADAPTER)
        .orElseGet(() -> new DereAppSettingEntity(ConfiguracaoIntegracaoService.REAL_ADAPTER, "true"));
    atual.value("true");
    ajustes.saveAndFlush(atual);
  }

  private void desligarAdaptador() {
    ajustes.findById(ConfiguracaoIntegracaoService.REAL_ADAPTER).ifPresent(atual -> {
      atual.value("false");
      ajustes.saveAndFlush(atual);
    });
  }

  private void ligarAdaptadorCommitado() {
    isolada.executar(() -> {
      ligarAdaptador();
      return null;
    });
  }

  private void guardarA1() throws Exception {
    var bytes = new ByteArrayOutputStream();
    CertificadoTesteLocal.create().store(bytes, "changeit".toCharArray());
    var arquivo = new MockMultipartFile("file", "teste.p12", "application/x-pkcs12", bytes.toByteArray());
    autenticado(multipart("/api/certificates/entity/" + entityId)
            .file(arquivo)
            .file(new MockMultipartFile("label", "", "text/plain", "A1 IT".getBytes()))
            .file(new MockMultipartFile("password", "", "text/plain", "changeit".getBytes()))
            .header("X-User-Id", masterId))
        .andExpect(status().isOk());
  }

  private long loteNaTransacao(String protocolo, boolean simulado) {
    var evento = new DereEventEntity("D-1001", "IT" + Long.toUnsignedString(System.nanoTime(), 36), "restricted-production", "nota_2026_001", "<evt/>");
    evento.ownedBy(masterId, entityId);
    evento.source("it.xml", null, simulado);
    evento = eventos.saveAndFlush(evento);
    var lote = new DereBatchEntity("<lote/>");
    lote.ownedBy(masterId, entityId);
    lote = lotes.saveAndFlush(lote);
    lote.received(protocolo);
    lotes.saveAndFlush(lote);
    evento.attachBatch(lote.getId());
    eventos.saveAndFlush(evento);
    return lote.getId();
  }

  private long[] loteDevido(String protocolo, boolean simulado) {
    return isolada.executar(() -> {
      var id = loteNaTransacao(protocolo, simulado);
      var lote = lotes.findById(id).orElseThrow();
      lote.queryFailed(0);
      lotes.saveAndFlush(lote);
      var evento = eventos.findByBatchId(id).get(0);
      evento.sent();
      eventos.saveAndFlush(evento);
      return new long[] { id, evento.getId() };
    });
  }

  private static String idEvento(String csv) {
    var busca = ID_EVENTO.matcher(csv);
    if (!busca.find()) throw new IllegalStateException("CSV sem id de evento");
    return busca.group();
  }
}
