CREATE TABLE dere_entity_certificate (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  entity_id BIGINT NOT NULL,
  label VARCHAR(150) NOT NULL,
  p12_ciphertext BLOB NOT NULL,
  password_ciphertext VARCHAR(1000) NOT NULL,
  alias_name VARCHAR(150),
  valid_until TIMESTAMP NULL,
  active BOOLEAN NOT NULL DEFAULT TRUE,
  created_at TIMESTAMP NOT NULL,
  FOREIGN KEY (entity_id) REFERENCES dere_entity(id)
);
