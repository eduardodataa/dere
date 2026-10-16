package br.gov.dere.api;

import br.gov.dere.application.transmissao.ProtocoloPdf;
import br.gov.dere.application.transmissao.RequisicaoEnvioSimulado;
import br.gov.dere.application.transmissao.ResultadoEnvioSimulado;
import br.gov.dere.application.transmissao.TransmissaoSimuladaService;
import br.gov.dere.application.transmissao.VisaoTransmissao;
import br.gov.dere.persistence.DereTentativaTransmissaoEntity;
import br.gov.dere.persistence.DereTentativaTransmissaoRepository;
import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transmissions")
public class TransmissaoController {
  private final DereTentativaTransmissaoRepository repository;
  private final TransmissaoSimuladaService simulated;

  public TransmissaoController(DereTentativaTransmissaoRepository repository, TransmissaoSimuladaService simulated) {
    this.repository = repository;
    this.simulated = simulated;
  }

  @PostMapping("/simulate")
  public ResultadoEnvioSimulado simulate(
      @RequestBody RequisicaoEnvioSimulado request,
      @RequestHeader("X-User-Id") Long userId,
      @RequestHeader("X-Entity-Id") Long entityId) {
    return simulated.simulate(userId, entityId, request);
  }

  @PostMapping("/{batchId}/query")
  public VisaoTransmissao query(
      @PathVariable Long batchId,
      @RequestHeader("X-User-Id") Long userId,
      @RequestHeader("X-Entity-Id") Long entityId) {
    return simulated.query(userId, entityId, batchId);
  }

  @GetMapping
  public List<VisaoTransmissao> listar(
      @RequestHeader("X-User-Id") Long userId,
      @RequestHeader("X-Entity-Id") Long entityId) {
    return simulated.listar(userId, entityId);
  }

  @GetMapping("/{batchId}")
  public VisaoTransmissao detalhe(
      @PathVariable Long batchId,
      @RequestHeader("X-User-Id") Long userId,
      @RequestHeader("X-Entity-Id") Long entityId) {
    return simulated.detalhe(userId, entityId, batchId);
  }

  @DeleteMapping("/{batchId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void excluir(
      @PathVariable Long batchId,
      @RequestHeader("X-User-Id") Long userId,
      @RequestHeader("X-Entity-Id") Long entityId) {
    simulated.excluir(userId, entityId, batchId);
  }

  @GetMapping("/events/{eventId}/attempts")
  public List<DereTentativaTransmissaoEntity> attempts(@PathVariable Long eventId) {
    return repository.findByEventIdOrderByAttemptAsc(eventId);
  }

  @GetMapping("/{batchId}/protocolo.pdf")
  public ResponseEntity<byte[]> protocolo(
      @PathVariable Long batchId,
      @RequestHeader("X-User-Id") Long userId,
      @RequestHeader("X-Entity-Id") Long entityId) {
    var detalhe = simulated.detalhe(userId, entityId, batchId);
    var nome = "protocolo-" + (detalhe.protocol() == null ? batchId : detalhe.protocol().replace('.', '-')) + ".pdf";
    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + nome)
        .contentType(MediaType.APPLICATION_PDF)
        .body(ProtocoloPdf.protocolo(detalhe));
  }

  @PostMapping("/relatorio.pdf")
  public ResponseEntity<byte[]> relatorio(
      @RequestBody List<VisaoTransmissao> linhas,
      @RequestHeader("X-User-Id") Long userId,
      @RequestHeader("X-Entity-Id") Long entityId) {
    simulated.listar(userId, entityId);
    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=relatorio-transmissoes.pdf")
        .contentType(MediaType.APPLICATION_PDF)
        .body(ProtocoloPdf.relatorio(linhas == null ? List.of() : linhas));
  }
}
