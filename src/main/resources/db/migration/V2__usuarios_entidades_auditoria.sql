CREATE TABLE dere_user (id BIGINT AUTO_INCREMENT PRIMARY KEY, login VARCHAR(100) NOT NULL UNIQUE, password_hash VARCHAR(255) NOT NULL, display_name VARCHAR(150) NOT NULL, active BOOLEAN NOT NULL DEFAULT TRUE, created_at TIMESTAMP NOT NULL);
CREATE TABLE dere_entity (id BIGINT AUTO_INCREMENT PRIMARY KEY, cnpj_root VARCHAR(8) NOT NULL UNIQUE, legal_name VARCHAR(255) NOT NULL, active BOOLEAN NOT NULL DEFAULT TRUE, created_at TIMESTAMP NOT NULL);
CREATE TABLE dere_user_entity (user_id BIGINT NOT NULL, entity_id BIGINT NOT NULL, PRIMARY KEY(user_id,entity_id), FOREIGN KEY(user_id) REFERENCES dere_user(id), FOREIGN KEY(entity_id) REFERENCES dere_entity(id));
CREATE TABLE dere_validation_audit (id BIGINT AUTO_INCREMENT PRIMARY KEY, user_id BIGINT NOT NULL, entity_id BIGINT NOT NULL, operation VARCHAR(20) NOT NULL, event_type VARCHAR(20) NOT NULL, valid BOOLEAN NOT NULL, errors LONGTEXT, payload_xml LONGTEXT, payload_csv LONGTEXT, batch_size_bytes BIGINT, created_at TIMESTAMP NOT NULL, FOREIGN KEY(user_id) REFERENCES dere_user(id), FOREIGN KEY(entity_id) REFERENCES dere_entity(id));
ALTER TABLE dere_event ADD COLUMN user_id BIGINT;
ALTER TABLE dere_event ADD COLUMN entity_id BIGINT;
ALTER TABLE dere_batch ADD COLUMN user_id BIGINT;
ALTER TABLE dere_batch ADD COLUMN entity_id BIGINT;
