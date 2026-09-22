package br.gov.dere.application.validation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import br.gov.dere.application.d1001.D1001XmlGenerator;
import br.gov.dere.domain.contributor.D1001;
import br.gov.dere.domain.layout.ValidationContext;
import br.gov.dere.domain.pgcc.D1011;
import br.gov.dere.domain.pgcc.PGCCAccount;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

class D1011DependencyValidatorTest {
  @Test
  void requiresEntityContext() {
    var validator = new D1011DependencyValidator(mock(JdbcTemplate.class));
    var issues = validator.validate(document("12345678", LocalDate.of(2026, 10, 1)), null);
    assertEquals("ENTITY_REQUIRED", issues.get(0).code());
  }

  @Test
  void acceptsWhenPersistedD1001CoversValidity() throws Exception {
    var jdbc = mock(JdbcTemplate.class);
    var xml = new D1001XmlGenerator().generate(new D1001(
        "DeRE10011234567820261001120000000000000001", 1, 2, "dere-poc/0.1", "12345678",
        LocalDate.of(2026, 10, 1), null, 9, List.of(), 0));
    when(jdbc.query(anyString(), any(RowMapper.class), eq(8L))).thenReturn(List.of(xml));
    var issues = new D1011DependencyValidator(jdbc).validate(document("12345678", LocalDate.of(2026, 10, 1)), new ValidationContext(8L, null));
    assertTrue(issues.isEmpty());
  }

  @Test
  void rejectsWhenTaxpayerOrValidityDoNotMatch() throws Exception {
    var jdbc = mock(JdbcTemplate.class);
    var xml = new D1001XmlGenerator().generate(new D1001(
        "DeRE10011234567820261001120000000000000001", 1, 2, "dere-poc/0.1", "12345678",
        LocalDate.of(2026, 11, 1), null, 9, List.of(), 0));
    when(jdbc.query(anyString(), any(RowMapper.class), eq(8L))).thenReturn(List.of(xml));
    var issues = new D1011DependencyValidator(jdbc).validate(document("12345678", LocalDate.of(2026, 10, 1)), new ValidationContext(8L, null));
    assertTrue(issues.stream().anyMatch(issue -> "D1001_NOT_FOUND_OR_NOT_VALID".equals(issue.code())));
  }

  @Test
  void recordsParseErrorOfPersistedXml() {
    var jdbc = mock(JdbcTemplate.class);
    when(jdbc.query(anyString(), any(RowMapper.class), eq(8L))).thenReturn(List.of("<nao-e-d1001/>"));
    var issues = new D1011DependencyValidator(jdbc).validate(document("12345678", LocalDate.of(2026, 10, 1)), new ValidationContext(8L, null));
    assertTrue(issues.stream().anyMatch(issue -> "D1001_PARSE_ERROR".equals(issue.code())));
    assertTrue(issues.stream().anyMatch(issue -> "D1001_NOT_FOUND_OR_NOT_VALID".equals(issue.code())));
  }

  private static D1011 document(String cnpj, LocalDate validFrom) {
    var account = new PGCCAccount("1101000", "1101", 0, "Disponibilidades", "A", "desc", null, "101000000", 2, "D", "1", "220110001", 0, null, validFrom, null);
    return new D1011("DeRE10111000000123456782026090212000000007", 1, null, null, 2, 1, "dere-poc/0.1", cnpj, validFrom, null, "5", "A", List.of(account));
  }
}
