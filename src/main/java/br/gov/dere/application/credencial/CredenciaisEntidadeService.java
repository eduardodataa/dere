package br.gov.dere.application.credencial;

import br.gov.dere.application.configuracao.ValidadeCredencial;
import br.gov.dere.integration.certificado.PocSecretBox;
import br.gov.dere.persistence.DereEntityEntity;
import br.gov.dere.persistence.DereEntityRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class CredenciaisEntidadeService {
  private final DereEntityRepository entities;
  private final PocSecretBox box;

  public CredenciaisEntidadeService(
      DereEntityRepository entities,
      @Value("${dere.certificate.encryption-key}") String key) {
    this.entities = entities;
    this.box = new PocSecretBox(key);
  }

  public CredencialEntidade carregar(Long entityId) {
    if (entityId == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Informe a entidade.");
    }
    var entity = entities.findById(entityId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Entidade não encontrada"));
    return de(entity);
  }

  public CredencialEntidade de(DereEntityEntity entity) {
    var cifrado = entity.getClientSecret();
    var secret = cifrado == null || cifrado.isBlank() ? null : decryptOrPlain(cifrado);
    return new CredencialEntidade(
        entity.getId(),
        entity.getClientId(),
        secret,
        secret != null && !secret.isBlank(),
        entity.getValidadeCredencial());
  }

  public void aplicar(DereEntityEntity entity, String clientId, String clientSecret, String validadeCredencial) {
    if (clientId != null) {
      entity.clientId(clientId.trim());
    }
    if (clientSecret != null && !clientSecret.isBlank()) {
      try {
        entity.clientSecret(box.encrypt(clientSecret.trim()));
      } catch (Exception ex) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Não foi possível guardar o client secret.");
      }
    }
    if (validadeCredencial != null) {
      var validade = validadeCredencial.trim();
      if (!validade.isEmpty() && ValidadeCredencial.ler(validade) == null) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A validade da credencial deve estar no formato AAAA-MM-DD.");
      }
      entity.validadeCredencial(validade.isEmpty() ? null : validade);
    }
  }

  private String decryptOrPlain(String value) {
    try {
      return box.decrypt(value);
    } catch (Exception ignored) {
      return value;
    }
  }
}
