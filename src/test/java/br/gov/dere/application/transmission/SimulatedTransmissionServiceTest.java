package br.gov.dere.application.transmission;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import br.gov.dere.application.access.AccessService;
import br.gov.dere.application.certificate.CertificateService;
import br.gov.dere.application.d1001.D1001ImportService;
import br.gov.dere.application.validation.D1011DependencyValidator;
import br.gov.dere.application.validation.ServicoValidacaoLeiaute;
import br.gov.dere.domain.EventStatus;
import br.gov.dere.integration.xml.XmlSupport;
import br.gov.dere.persistence.DereBatchEntity;
import br.gov.dere.persistence.DereBatchRepository;
import br.gov.dere.persistence.DereEntityEntity;
import br.gov.dere.persistence.DereEventEntity;
import br.gov.dere.persistence.DereEventRepository;
import br.gov.dere.persistence.DereTransmissionAttemptRepository;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.Test;

class SimulatedTransmissionServiceTest {
  @Test
  void recusaCsvComCritica() {
    var fluxo = servico(true);
    var csv = """
        id;tpOper;tpAmb;aplicEmi;verAplic;nrInsc;iniValid;regTribPrinc;indNatTrib
        CURTO;1;2;1;dere-poc/0.1;12345678;01/10/2026;9;0
        """;
    var resultado = fluxo.simulate(1L, 8L, new SimulateSendRequest("D-1001", "CSV", "ruim.csv", csv));
    assertFalse(resultado.enviado());
    assertFalse(resultado.validacao().valido());
  }

  @Test
  void recebeEConsultaD1001Valido() throws Exception {
    var fluxo = servico(true);
    var csv = Files.readString(Path.of("examples/csv/d1001-informacoes-contribuinte.csv"));
    var recepcao = fluxo.simulate(1L, 8L, new SimulateSendRequest("D-1001", "CSV", "1001.csv", csv));
    assertTrue(recepcao.enviado(), () -> String.valueOf(recepcao.validacao().criticas()));
    assertNotNull(recepcao.transmissao().protocol());
    assertTrue(recepcao.transmissao().protocol().length() <= 28);
    assertEquals("PROCESSING", recepcao.transmissao().status());
    assertXsd(recepcao.transmissao().returnXml(), "/dere/schemas/nota_2026_001/xsd/retornoLoteDere-v1_0_1.xsd");
    var consulta = fluxo.query(1L, 8L, recepcao.transmissao().batchId());
    assertEquals(EventStatus.ACCEPTED.name(), consulta.status());
    assertNotNull(consulta.receiptNumber());
    assertTrue(consulta.receiptNumber().length() <= 31);
    assertXsd(consulta.returnXml(), "/dere/schemas/nota_2026_001/xsd/retornoLoteDere-v1_0_1.xsd");
    var repetido = fluxo.simulate(1L, 8L, new SimulateSendRequest("D-1001", "CSV", "1001.csv", csv));
    assertFalse(repetido.enviado());
    assertNotNull(repetido.transmissao());
    assertEquals(consulta.protocol(), repetido.transmissao().protocol());
    assertTrue(repetido.validacao().criticas().stream().anyMatch(item -> item.problema().contains("já existe transmissão") || item.problema().contains("Já existe transmissão")));
  }

  private static SimulatedTransmissionService servico(boolean mock) {
    var events = mock(DereEventRepository.class);
    var batches = mock(DereBatchRepository.class);
    var attempts = mock(DereTransmissionAttemptRepository.class);
    var access = mock(AccessService.class);
    var certificates = mock(CertificateService.class);
    var importD1001 = mock(D1001ImportService.class);
    var dependencias = mock(D1011DependencyValidator.class);
    var chaves = mock(br.gov.dere.application.validation.ChaveEventoTabelaValidator.class);
    when(dependencias.validate(any(), any())).thenReturn(List.of());
    when(chaves.validar(any(), any(), any(), org.mockito.ArgumentMatchers.anyInt(), any(), any(), any(), any())).thenReturn(List.of());
    doNothing().when(access).assertAccess(anyLong(), anyLong());
    when(access.entity(8L)).thenReturn(new DereEntityEntity("12345678", "EFPC teste"));
    when(certificates.list(8L)).thenReturn(List.of());
    var eventIds = new AtomicLong();
    var batchIds = new AtomicLong();
    Map<Long, DereEventEntity> eventos = new ConcurrentHashMap<>();
    Map<String, DereEventEntity> porIdEvento = new ConcurrentHashMap<>();
    Map<Long, DereBatchEntity> lotes = new ConcurrentHashMap<>();
    when(events.save(any())).thenAnswer(inv -> {
      var evento = (DereEventEntity) inv.getArgument(0);
      if (evento.getId() == null) evento.persistId(eventIds.incrementAndGet());
      eventos.put(evento.getId(), evento);
      porIdEvento.put(evento.getEventIdentifier(), evento);
      return evento;
    });
    when(events.saveAndFlush(any())).thenAnswer(inv -> {
      var evento = (DereEventEntity) inv.getArgument(0);
      if (evento.getId() == null) evento.persistId(eventIds.incrementAndGet());
      eventos.put(evento.getId(), evento);
      porIdEvento.put(evento.getEventIdentifier(), evento);
      return evento;
    });
    when(events.findByEventIdentifier(anyString())).thenAnswer(inv -> Optional.ofNullable(porIdEvento.get(inv.getArgument(0))));
    when(events.findByBatchId(anyLong())).thenAnswer(inv -> eventos.values().stream().filter(item -> inv.getArgument(0).equals(item.getBatchId())).toList());
    when(batches.save(any())).thenAnswer(inv -> {
      var lote = (DereBatchEntity) inv.getArgument(0);
      if (lote.getId() == null) lote.persistId(batchIds.incrementAndGet());
      lotes.put(lote.getId(), lote);
      return lote;
    });
    when(batches.saveAndFlush(any())).thenAnswer(inv -> {
      var lote = (DereBatchEntity) inv.getArgument(0);
      if (lote.getId() == null) lote.persistId(batchIds.incrementAndGet());
      lotes.put(lote.getId(), lote);
      return lote;
    });
    when(batches.findById(anyLong())).thenAnswer(inv -> Optional.ofNullable(lotes.get(inv.getArgument(0))));
    when(attempts.save(any())).thenAnswer(inv -> inv.getArgument(0));
    return new SimulatedTransmissionService(mock, "restricted-production", "nota_2026_001", access,
        new ServicoValidacaoLeiaute(dependencias, chaves), importD1001, certificates, events, batches, attempts,
        new TransmissionArtifactStore());
  }

  private static void assertXsd(String xml, String xsd) {
    var url = SimulatedTransmissionServiceTest.class.getResource(xsd);
    var issues = XmlSupport.validateCollecting(xml, url).stream().filter(issue -> !"aviso".equals(issue.severity())).toList();
    assertEquals(List.of(), issues, () -> issues.stream().map(XmlSupport.Issue::message).reduce("", (a, b) -> a + "\n" + b));
  }
}
