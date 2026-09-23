package br.gov.dere.application.certificate;

import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.CreateSecretRequest;
import software.amazon.awssdk.services.secretsmanager.model.DeleteSecretRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.PutSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.ResourceExistsException;
import software.amazon.awssdk.services.secretsmanager.model.ResourceNotFoundException;

@Component
@ConditionalOnProperty(name = "dere.certificate.password-store", havingValue = "secrets-manager")
public class SecretsManagerCertificatePasswordStore implements CertificatePasswordStore {
  private final SecretsManagerClient cliente;
  private final String prefixo;
  private final String kmsKeyId;

  public SecretsManagerCertificatePasswordStore(
      SecretsManagerClient cliente,
      @Value("${dere.certificate.secret-prefix:dere/cert}") String prefixo,
      @Value("${dere.certificate.kms-key-id:}") String kmsKeyId) {
    this.cliente = cliente;
    this.prefixo = prefixo.endsWith("/") ? prefixo.substring(0, prefixo.length() - 1) : prefixo;
    this.kmsKeyId = kmsKeyId;
  }

  @Override
  public String guardar(Long entityId, String password) {
    var nome = prefixo + "/" + (entityId == null ? "x" : entityId) + "/" + UUID.randomUUID();
    var criar = CreateSecretRequest.builder().name(nome).secretString(password);
    if (kmsKeyId != null && !kmsKeyId.isBlank()) criar.kmsKeyId(kmsKeyId);
    try {
      cliente.createSecret(criar.build());
    } catch (ResourceExistsException existe) {
      cliente.putSecretValue(PutSecretValueRequest.builder().secretId(nome).secretString(password).build());
    }
    return "sm:" + nome;
  }

  @Override
  public String ler(String referencia) {
    var nome = nomeDe(referencia);
    return cliente.getSecretValue(GetSecretValueRequest.builder().secretId(nome).build()).secretString();
  }

  @Override
  public void apagar(String referencia) {
    try {
      cliente.deleteSecret(DeleteSecretRequest.builder().secretId(nomeDe(referencia)).forceDeleteWithoutRecovery(true).build());
    } catch (ResourceNotFoundException ignorado) {
    }
  }

  private static String nomeDe(String referencia) {
    if (referencia == null || !referencia.startsWith("sm:")) {
      throw new IllegalArgumentException("Referência de senha Secrets Manager inválida");
    }
    return referencia.substring(3);
  }
}
