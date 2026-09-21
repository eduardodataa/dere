-- Carga inicial da POC DeRE.
-- Senha informada: eduardo86
-- A senha abaixo está armazenada como BCrypt; não substituir por texto puro.
-- Execute após as migrations V1 e V2.

INSERT INTO dere_user (login, password_hash, display_name, active, created_at)
SELECT 'eduardo', '$2b$12$ngRGz6RP4EK.wZUkMV1vuO59hzdfiq1ILM6kFZF2jidU6AW3Q6ScS', 'Eduardo', TRUE, CURRENT_TIMESTAMP
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM dere_user WHERE login = 'eduardo');

INSERT INTO dere_entity (cnpj_root, legal_name, active, created_at)
SELECT '12345678', 'Entidade Teste EFPC', TRUE, CURRENT_TIMESTAMP
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM dere_entity WHERE cnpj_root = '12345678');

INSERT INTO dere_user_entity (user_id, entity_id)
SELECT u.id, e.id
FROM dere_user u CROSS JOIN dere_entity e
WHERE u.login = 'eduardo' AND e.cnpj_root = '12345678'
  AND NOT EXISTS (
    SELECT 1 FROM dere_user_entity ue
    WHERE ue.user_id = u.id AND ue.entity_id = e.id
  );
