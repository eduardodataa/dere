#!/usr/bin/env bash
set -euo pipefail
sudo grep -E '^(DERE_DB|SPRING_DATASOURCE|DB_)' /opt/apps/dere/dere.env | sed -E 's/(PASSWORD|SECRET|KEY)=.*/\1=***/'
echo ---
for db in dere-stg dere-hml dere; do
  echo ===== "$db" =====
  sudo mysql -N -e "SELECT COUNT(*) FROM information_schema.tables WHERE table_schema='$db' AND table_name='dere_app_setting'"
  sudo mysql -N -e "SHOW TABLES FROM \`$db\`" | head -40
done
