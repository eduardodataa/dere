package br.gov.dere.application.upload;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

public final class NomeEntidadeUpload {
  private NomeEntidadeUpload() {}

  public static String de(JdbcTemplate jdbc, Long entityId) {
    if (jdbc == null || entityId == null) return "";
    try {
      var nomes = jdbc.query("SELECT legal_name FROM dere_entity WHERE id=?", (rs, row) -> rs.getString(1), entityId);
      if (nomes.isEmpty() || nomes.get(0) == null) return "";
      return nomes.get(0);
    } catch (DataAccessException ex) {
      return "";
    }
  }
}
