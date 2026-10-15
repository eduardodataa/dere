#!/usr/bin/env bash
set -uo pipefail
echo === full env keys ===
sudo grep -E '^[A-Za-z0-9_]+=' /opt/apps/dere/dere.env | cut -d= -f1 | sort
echo === datasource related ===
sudo grep -Ei 'datasource|jdbc|mariadb|mysql|DERE_DB' /opt/apps/dere/dere.env | sed -E 's/(PASSWORD|SECRET|KEY|TOKEN)=.*/\1=***/' || true
echo === start script ===
sudo sed -n '1,160p' /opt/apps/dere/bin/dere-start.sh || true
echo === applying schema ===
SQL='CREATE TABLE IF NOT EXISTS dere_app_setting (
  setting_key VARCHAR(100) NOT NULL,
  setting_value LONGTEXT NULL,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (setting_key)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;'
for db in dere-stg dere-hml dere; do
  echo ===== "$db" =====
  sudo mysql "$db" -e "$SQL"
  sudo mysql -N -e "SELECT COUNT(*) FROM information_schema.tables WHERE table_schema='$db' AND table_name='dere_app_setting'"
done
echo DONE
