#!/usr/bin/env bash
set -euo pipefail
echo === env ===
for f in /opt/apps/dere/dere.env /opt/apps/dere-hml/dere.env /opt/apps/dere-staging/dere.env; do
  if sudo test -f "$f"; then
    echo FOUND "$f"
    sudo grep -E '^(DERE_DB_|DERE_CERTIFICATE_|AWS_|DERE_MOCK)' "$f" | sed -E 's/(PASSWORD|SECRET|KEY)=.*/\1=***/'
  fi
done
echo === systemd ===
for u in dere.service dere-hml.service dere-staging.service; do
  echo ---- "$u" ----
  sudo grep -E 'EnvironmentFile|ExecStart' /etc/systemd/system/"$u" || true
done
echo === dbs ===
sudo mysql -N -e "SHOW DATABASES LIKE 'dere%'"
for db in dere-stg dere-hml dere; do
  echo ===== "$db" =====
  sudo mysql -N -e "SELECT SCHEMA_NAME FROM information_schema.SCHEMATA WHERE SCHEMA_NAME='$db'"
  sudo mysql -N -e "SELECT table_name FROM information_schema.tables WHERE table_schema='$db' AND table_name='dere_app_setting'"
  sudo mysql -N -e "SELECT column_name, is_nullable, column_type FROM information_schema.columns WHERE table_schema='$db' AND table_name='dere_entity_certificate' AND column_name IN ('secret_ref','password_ciphertext')"
  sudo mysql -N -e "SELECT column_name FROM information_schema.columns WHERE table_schema='$db' AND table_name='dere_user' AND column_name='is_master'"
done
