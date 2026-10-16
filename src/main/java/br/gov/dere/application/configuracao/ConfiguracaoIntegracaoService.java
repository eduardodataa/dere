package br.gov.dere.application.configuracao;

import br.gov.dere.integration.certificado.PocSecretBox;
import br.gov.dere.persistence.DereAppSettingEntity;
import br.gov.dere.persistence.DereAppSettingRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ConfiguracaoIntegracaoService {
  public static final String REAL_ADAPTER = "integration.real_adapter_enabled";
  public static final String MOCK = "integration.mock_enabled";
  public static final String BASE_URL = "integration.base_url";
  public static final String TOKEN_URL = "integration.token_url";
  public static final String CLIENT_ID = "integration.client_id";
  public static final String CLIENT_SECRET = "integration.client_secret";
  public static final String ENVIRONMENT = "integration.environment";
  public static final String SCHEMA = "integration.schema_version";

  private final DereAppSettingRepository repo;
  private final PocSecretBox box;
  private final boolean defaultMock;
  private final String defaultBaseUrl;
  private final String defaultTokenUrl;
  private final String defaultClientId;
  private final String defaultClientSecret;
  private final String defaultEnvironment;
  private final String defaultSchema;
  private final String implantacao;

  public ConfiguracaoIntegracaoService(
      DereAppSettingRepository repo,
      @Value("${dere.certificate.encryption-key}") String key,
      @Value("${dere.mock.enabled:true}") boolean defaultMock,
      @Value("${dere.api.base-url:https://api.receitafederal.gov.br/dere}") String defaultBaseUrl,
      @Value("${dere.api.token-url:https://api.receitafederal.gov.br/token}") String defaultTokenUrl,
      @Value("${dere.auth.client-id:}") String defaultClientId,
      @Value("${dere.auth.client-secret:}") String defaultClientSecret,
      @Value("${dere.environment:restricted-production}") String defaultEnvironment,
      @Value("${dere.schema-version:nota_2026_001}") String defaultSchema,
      @Value("${dere.deployment:local}") String implantacao) {
    this.repo = repo;
    this.box = new PocSecretBox(key);
    this.defaultMock = defaultMock;
    this.defaultBaseUrl = defaultBaseUrl;
    this.defaultTokenUrl = defaultTokenUrl;
    this.defaultClientId = defaultClientId;
    this.defaultClientSecret = defaultClientSecret;
    this.defaultEnvironment = defaultEnvironment;
    this.defaultSchema = defaultSchema;
    this.implantacao = implantacao;
  }

  public ConfiguracaoIntegracao current() {
    return PoliticaAmbiente.aplicar(implantacao, lida());
  }

  private ConfiguracaoIntegracao lida() {
    var secret = ler(CLIENT_SECRET, null);
    var secretPlain = secret == null || secret.isBlank() ? defaultClientSecret : decryptOrPlain(secret);
    return new ConfiguracaoIntegracao(
        booleanOf(REAL_ADAPTER, false),
        booleanOf(MOCK, defaultMock),
        textOf(BASE_URL, defaultBaseUrl),
        textOf(TOKEN_URL, defaultTokenUrl),
        textOf(CLIENT_ID, defaultClientId),
        secretPlain,
        secretPlain != null && !secretPlain.isBlank(),
        textOf(ENVIRONMENT, defaultEnvironment),
        textOf(SCHEMA, defaultSchema));
  }

  public ConfiguracaoIntegracao publicView() {
    var atual = current();
    return new ConfiguracaoIntegracao(
        atual.realAdapterEnabled(),
        atual.mockEnabled(),
        atual.baseUrl(),
        atual.tokenUrl(),
        atual.clientId(),
        null,
        atual.clientSecretConfigured(),
        atual.environment(),
        atual.schemaVersion());
  }

  @Transactional
  public ConfiguracaoIntegracao save(AtualizacaoConfiguracaoIntegracao update) throws Exception {
    if (update.realAdapterEnabled() != null) gravar(REAL_ADAPTER, String.valueOf(update.realAdapterEnabled()));
    if (update.mockEnabled() != null) gravar(MOCK, String.valueOf(update.mockEnabled()));
    if (update.baseUrl() != null) gravar(BASE_URL, update.baseUrl().trim());
    if (update.tokenUrl() != null) gravar(TOKEN_URL, update.tokenUrl().trim());
    if (update.clientId() != null) gravar(CLIENT_ID, update.clientId().trim());
    if (update.clientSecret() != null && !update.clientSecret().isBlank()) {
      gravar(CLIENT_SECRET, box.encrypt(update.clientSecret().trim()));
    }
    if (update.environment() != null) gravar(ENVIRONMENT, update.environment().trim());
    if (update.schemaVersion() != null) gravar(SCHEMA, update.schemaVersion().trim());
    return publicView();
  }

  private boolean booleanOf(String key, boolean padrao) {
    var valor = ler(key, null);
    if (valor == null || valor.isBlank()) return padrao;
    return Boolean.parseBoolean(valor);
  }

  private String textOf(String key, String padrao) {
    var valor = ler(key, null);
    if (valor == null || valor.isBlank()) return padrao == null ? "" : padrao;
    return valor;
  }

  private String ler(String key, String padrao) {
    return repo.findById(key).map(DereAppSettingEntity::getSettingValue).orElse(padrao);
  }

  private void gravar(String key, String value) {
    var entity = repo.findById(key).orElseGet(() -> new DereAppSettingEntity(key, value));
    entity.value(value);
    repo.save(entity);
  }

  private String decryptOrPlain(String value) {
    try {
      return box.decrypt(value);
    } catch (Exception ignored) {
      return value;
    }
  }
}
