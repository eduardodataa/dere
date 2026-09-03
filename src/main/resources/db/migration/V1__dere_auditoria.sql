CREATE TABLE dere_event (
  id BIGINT AUTO_INCREMENT PRIMARY KEY, event_type VARCHAR(20) NOT NULL,
  event_identifier VARCHAR(42) NOT NULL, environment VARCHAR(32) NOT NULL,
  reference_period VARCHAR(10), operation_type VARCHAR(2), schema_version VARCHAR(40) NOT NULL,
  status VARCHAR(32) NOT NULL, xml_unsigned LONGTEXT, xml_signed LONGTEXT,
  receipt_number VARCHAR(100), created_at TIMESTAMP NOT NULL, sent_at TIMESTAMP, processed_at TIMESTAMP,
  UNIQUE KEY uk_dere_event_identifier (event_identifier)
);
CREATE TABLE dere_batch (
  id BIGINT AUTO_INCREMENT PRIMARY KEY, protocol VARCHAR(100), status VARCHAR(32) NOT NULL,
  request_xml LONGTEXT NOT NULL, response_xml LONGTEXT, created_at TIMESTAMP NOT NULL,
  sent_at TIMESTAMP, last_query_at TIMESTAMP
);
CREATE TABLE dere_event_error (
  id BIGINT AUTO_INCREMENT PRIMARY KEY, event_id BIGINT NOT NULL, code VARCHAR(50),
  description VARCHAR(1000) NOT NULL, location VARCHAR(255), created_at TIMESTAMP NOT NULL,
  CONSTRAINT fk_dere_event_error_event FOREIGN KEY (event_id) REFERENCES dere_event(id)
);
