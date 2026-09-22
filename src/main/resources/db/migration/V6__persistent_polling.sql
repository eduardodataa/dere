ALTER TABLE dere_batch ADD COLUMN version BIGINT NOT NULL DEFAULT 0;
ALTER TABLE dere_batch ADD COLUMN next_query_at TIMESTAMP NULL;
ALTER TABLE dere_batch ADD COLUMN lease_until TIMESTAMP NULL;
ALTER TABLE dere_batch ADD COLUMN query_attempts INT NOT NULL DEFAULT 0;
CREATE INDEX ix_dere_batch_polling ON dere_batch(status, next_query_at);
