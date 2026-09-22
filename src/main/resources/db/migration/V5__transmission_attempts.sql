CREATE TABLE dere_transmission_attempt (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  event_id BIGINT NOT NULL,
  operation VARCHAR(40) NOT NULL,
  attempt INT NOT NULL,
  started_at TIMESTAMP NOT NULL,
  finished_at TIMESTAMP NULL,
  http_status INT NULL,
  government_code VARCHAR(100) NULL,
  duration_ms BIGINT NULL,
  correlation_id VARCHAR(100) NOT NULL,
  request_reference VARCHAR(255) NULL,
  response_reference VARCHAR(255) NULL,
  outcome VARCHAR(40) NOT NULL,
  error_message LONGTEXT NULL,
  CONSTRAINT fk_transmission_attempt_event FOREIGN KEY (event_id) REFERENCES dere_event(id),
  CONSTRAINT uk_transmission_attempt UNIQUE (event_id, operation, attempt)
);
CREATE INDEX ix_transmission_attempt_correlation ON dere_transmission_attempt(correlation_id);
