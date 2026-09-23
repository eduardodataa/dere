package br.gov.dere.application.validation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import br.gov.dere.application.transmission.TransmissionArtifactStore;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

class ChaveEventoTabelaValidatorTest {
  @Test
  void exclusaoSemEnvioGeraCritica() {
    var jdbc = mock(JdbcTemplate.class);
    when(jdbc.query(any(String.class), any(RowMapper.class), eq(1L), eq("D-1001"))).thenReturn(List.of());
    var validador = new ChaveEventoTabelaValidator(jdbc, new TransmissionArtifactStore());
    var criticas = validador.validar("D-1001", "1001-excluir.xml", 2, 3, "01914904", LocalDate.of(2026, 10, 1), null, 1L);
    assertEquals(1, criticas.size());
    assertEquals("tpOper", criticas.get(0).coluna());
    assertTrue(criticas.get(0).problema().contains("Não existe envio ativo"));
  }

  @Test
  void inclusaoSemEnvioNaoGeraCritica() {
    var jdbc = mock(JdbcTemplate.class);
    when(jdbc.query(any(String.class), any(RowMapper.class), eq(1L), eq("D-1001"))).thenReturn(List.of());
    var validador = new ChaveEventoTabelaValidator(jdbc, new TransmissionArtifactStore());
    var criticas = validador.validar("D-1001", "1001-incluir.xml", 2, 1, "01914904", LocalDate.of(2026, 10, 1), null, 1L);
    assertEquals(List.of(), criticas);
  }
}
