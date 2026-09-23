package br.gov.dere.application.validation;

import br.gov.dere.application.csv.DereCsvConverter;
import br.gov.dere.application.pgcc.D1011CsvConverter;
import br.gov.dere.application.transmission.TransmissionArtifactStore;
import br.gov.dere.application.validation.relatorio.Critica;
import br.gov.dere.application.validation.relatorio.TipoCritica;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class ChaveEventoTabelaValidator {
  private static final Set<String> ENVIADOS = Set.of("SENT", "PROTOCOL_RECEIVED", "PROCESSING", "ACCEPTED", "ACCEPTED_WITH_WARNINGS");
  private final JdbcTemplate jdbc;
  private final TransmissionArtifactStore artefatos;
  private final DereCsvConverter csvD1001 = new DereCsvConverter();
  private final D1011CsvConverter csvD1011 = new D1011CsvConverter();

  public ChaveEventoTabelaValidator(JdbcTemplate jdbc, TransmissionArtifactStore artefatos) {
    this.jdbc = jdbc;
    this.artefatos = artefatos;
  }

  public List<Critica> validar(String leiaute, String arquivo, Integer linha, int operacao, String cnpj, LocalDate iniValid, LocalDate fimValid, Long idEntidade) {
    if (operacao != 1 && operacao != 2 && operacao != 3) return List.of();
    var caminho = "D-1011".equals(leiaute) ? "/DeRE/evtPGCC/ideEvento/tpOper" : "/DeRE/evtInfoContrib/ideEvento/tpOper";
    if (idEntidade == null) {
      if (operacao == 1) return List.of();
      return List.of(new Critica(arquivo, linha, "tpOper", String.valueOf(operacao), TipoCritica.NEGOCIO,
          "evento ativo na mesma chave (nrInsc + iniValid)",
          "Não foi possível consultar envios anteriores. Selecione a entidade.", caminho));
    }
    var ativo = chaveAtiva(leiaute, idEntidade, cnpj, iniValid, fimValid);
    if (operacao == 3 && !ativo) {
      return List.of(new Critica(arquivo, linha, "tpOper", "3", TipoCritica.NEGOCIO,
          "inclusão ou alteração aceita com a mesma chave",
          "Não existe envio ativo para excluir. A chave é nrInsc + iniValid (e fimValid, se houver). Envie a inclusão antes da exclusão.",
          caminho));
    }
    if (operacao == 2 && !ativo) {
      return List.of(new Critica(arquivo, linha, "tpOper", "2", TipoCritica.NEGOCIO,
          "inclusão ou alteração aceita com a mesma chave",
          "Não existe envio ativo para alterar. A chave é nrInsc + iniValid (e fimValid, se houver). Envie a inclusão antes da alteração.",
          caminho));
    }
    if (operacao == 1 && ativo) {
      return List.of(new Critica(arquivo, linha, "tpOper", "1", TipoCritica.NEGOCIO,
          "chave inédita ou período já excluído",
          "Já existe envio ativo com esta chave (nrInsc + iniValid). Use alteração (tpOper 2) ou exclusão (tpOper 3).",
          caminho));
    }
    return List.of();
  }

  private boolean chaveAtiva(String leiaute, Long idEntidade, String cnpj, LocalDate iniValid, LocalDate fimValid) {
    var linhas = jdbc.query(
        "SELECT operation_type, status, xml_unsigned FROM dere_event WHERE entity_id=? AND event_type=? ORDER BY id DESC",
        (rs, row) -> new EventoEnviado(rs.getString("operation_type"), rs.getString("status"), rs.getString("xml_unsigned")),
        idEntidade, leiaute);
    for (var evento : linhas) {
      if (!enviado(evento.status())) continue;
      var chave = chaveDe(leiaute, evento.xml());
      if (chave == null || !chave.mesma(cnpj, iniValid, fimValid)) continue;
      return "1".equals(evento.operacao()) || "2".equals(evento.operacao());
    }
    return false;
  }

  private Chave chaveDe(String leiaute, String gravado) {
    var xml = artefatos.ler(gravado);
    if (xml == null || xml.isBlank()) return null;
    try {
      if ("D-1011".equals(leiaute)) {
        var modelo = csvD1011.fromXml(xml);
        return new Chave(modelo.cnpjRoot(), modelo.validFrom(), modelo.validTo());
      }
      var modelo = csvD1001.fromXml(xml);
      return new Chave(modelo.cnpjRoot(), modelo.validFrom(), modelo.validTo());
    } catch (Exception ex) {
      return null;
    }
  }

  private static boolean enviado(String status) {
    return status != null && ENVIADOS.contains(status);
  }

  private record EventoEnviado(String operacao, String status, String xml) {}

  private record Chave(String cnpj, LocalDate ini, LocalDate fim) {
    boolean mesma(String outroCnpj, LocalDate outroIni, LocalDate outroFim) {
      return Objects.equals(cnpj, outroCnpj) && Objects.equals(ini, outroIni) && Objects.equals(fim, outroFim);
    }
  }
}
