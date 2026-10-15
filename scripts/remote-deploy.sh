#!/usr/bin/env bash
set -euo pipefail
JAR_SRC=/tmp/dere-poc-deploy.jar
if [[ ! -f "$JAR_SRC" ]]; then
  echo "JAR ausente em $JAR_SRC" >&2
  exit 1
fi

deploy_one() {
  local dir="$1"
  local unit="$2"
  local port="$3"
  echo ===== "$unit" port="$port" =====
  sudo cp "$JAR_SRC" "$dir/dere-poc.jar"
  sudo chown ubuntu:ubuntu "$dir/dere-poc.jar"
  sudo systemctl restart "$unit"
  for i in $(seq 1 40); do
    if curl -sf "http://127.0.0.1:${port}/" >/dev/null; then
      echo OK "$unit"
      return 0
    fi
    sleep 3
  done
  echo FAIL "$unit"
  sudo journalctl -u "$unit" -n 60 --no-pager || true
  return 1
}

deploy_one /opt/apps/dere-staging/current dere-staging.service 8091
deploy_one /opt/apps/dere-hml/current dere-hml.service 8092
deploy_one /opt/apps/dere/current dere.service 8090
rm -f "$JAR_SRC"
echo ALL_DEPLOYED
