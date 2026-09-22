package br.gov.dere.application.layout;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import br.gov.dere.domain.layout.CsvDocument;
import br.gov.dere.domain.layout.ValidationContext;
import br.gov.dere.domain.pgcc.D1011;
import br.gov.dere.domain.pgcc.PGCCAccount;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;

class D1011LayoutDefinitionTest {
  private final D1011LayoutDefinition definition = new D1011LayoutDefinition();

  @Test
  void rejectsMissingIdentityAndEmptyAccounts() {
    var model = new D1011("", 1, null, null, 2, 1, "dere-poc/0.1", "", LocalDate.of(2026, 10, 1), null, "5", "A", List.of());
    var result = definition.validate(model, new ValidationContext(1L, null));
    assertFalse(result.valid());
    assertTrue(result.issues().stream().anyMatch(issue -> "ID_REQUIRED".equals(issue.code())));
    assertTrue(result.issues().stream().anyMatch(issue -> "TAXPAYER_REQUIRED".equals(issue.code())));
    assertTrue(result.issues().stream().anyMatch(issue -> "ACCOUNT_REQUIRED".equals(issue.code())));
  }

  @Test
  void rejectsDuplicateAccountCodesAndInvertedValidity() {
    var first = account("1.1.01", LocalDate.of(2026, 10, 1), LocalDate.of(2026, 9, 1));
    var second = account("1.1.01", LocalDate.of(2026, 10, 1), null);
    var model = new D1011("ID1", 1, null, null, 2, 1, "dere-poc/0.1", "12345678",
        LocalDate.of(2026, 10, 1), LocalDate.of(2026, 9, 1), "5", "A", List.of(first, second));
    var result = definition.validate(model, new ValidationContext(1L, null));
    assertFalse(result.valid());
    assertTrue(result.issues().stream().anyMatch(issue -> "VALIDITY_ORDER".equals(issue.code())));
    assertTrue(result.issues().stream().anyMatch(issue -> "ACCOUNT_DUPLICATED".equals(issue.code())));
    assertTrue(result.issues().stream().anyMatch(issue -> "ACCOUNT_VALIDITY_ORDER".equals(issue.code())));
  }

  @Test
  void acceptsCanonicalExampleCsv() {
    var csv = new CsvDocument("""
        id,tpOper,tpAmb,aplicEmi,verAplic,nrInsc,iniValid,fimValid,planoCtaRef,freqEncerr,cCta,cCtaInterna,cDbrMista,nomeCta,indCta,descCta,cCtaSup,cCtaRef,nivelCta,natCta,codNat,codTrib,indTribISS,idLeiDisp,iniVig,fimVig
        DeRE10111000000123456782026090212000000007,1,2,1,dere-poc/0.1,12345678,2026-10-01,,5,A,1.1.01,1.1.01,0,Disponibilidades,1,Conta de disponibilidades,,101000000,2,1,01,220110001,0,,2026-10-01,
        """);
    var model = definition.parseCsv(csv);
    var result = definition.validate(model, new ValidationContext(1L, null));
    assertTrue(result.valid());
    assertEquals("1.0.1", definition.version().value());
    assertEquals("D-1011", definition.layout().displayName());
  }

  private static PGCCAccount account(String code, LocalDate from, LocalDate to) {
    return new PGCCAccount(code, code, 0, "Conta", "A", "desc", null, "101000000", 2, "D", "1", "220110001", 0, null, from, to);
  }
}
