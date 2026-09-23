package br.gov.dere.application.certificate;

import br.gov.dere.integration.certificate.PocSecretBox;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "dere.certificate.password-store", havingValue = "local", matchIfMissing = true)
public class LocalCertificatePasswordStore implements CertificatePasswordStore {
  private final Path raiz;
  private final PocSecretBox box;

  @Autowired
  public LocalCertificatePasswordStore(
      @Value("${dere.certificate.secret-dir:data/cert-secrets}") String diretorio,
      @Value("${dere.certificate.encryption-key}") String chave) {
    this(Path.of(diretorio), chave);
  }

  LocalCertificatePasswordStore(Path raiz, String chave) {
    this.raiz = raiz;
    this.box = new PocSecretBox(chave);
  }

  @Override
  public String guardar(Long entityId, String password) throws Exception {
    Files.createDirectories(raiz);
    var nome = (entityId == null ? "x" : entityId) + "-" + UUID.randomUUID() + ".enc";
    var caminho = raiz.resolve(nome);
    Files.write(caminho, box.encrypt(password.getBytes(StandardCharsets.UTF_8)));
    return "local:" + nome;
  }

  @Override
  public String ler(String referencia) throws Exception {
    var arquivo = arquivoDe(referencia);
    return new String(box.decrypt(Files.readAllBytes(arquivo)), StandardCharsets.UTF_8);
  }

  @Override
  public void apagar(String referencia) {
    try {
      Files.deleteIfExists(arquivoDe(referencia));
    } catch (Exception ignorado) {
    }
  }

  private Path arquivoDe(String referencia) {
    if (referencia == null || !referencia.startsWith("local:")) {
      throw new IllegalArgumentException("Referência de senha local inválida");
    }
    var relativo = referencia.substring(6);
    if (relativo.isBlank() || relativo.contains("/") || relativo.contains("\\") || relativo.contains("..")) {
      throw new IllegalArgumentException("Referência de senha local inválida");
    }
    var nome = Path.of(relativo).getFileName();
    if (nome == null || !nome.toString().equals(relativo)) {
      throw new IllegalArgumentException("Referência de senha local inválida");
    }
    return raiz.resolve(nome);
  }
}
