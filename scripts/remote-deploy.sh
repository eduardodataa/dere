#!/usr/bin/env bash
set -euo pipefail
ROOT="$(cd "$(dirname "$0")/.." && pwd)"
JAR_SRC=/tmp/dere-poc-deploy.jar
if [[ ! -f "$JAR_SRC" ]]; then
  echo "JAR ausente em $JAR_SRC" >&2
  exit 1
fi

bash "$ROOT/scripts/deploy/deploy.sh" "$ROOT/scripts/config/dere-staging.conf" "$JAR_SRC"
bash "$ROOT/scripts/deploy/deploy.sh" "$ROOT/scripts/config/dere-hml.conf" "$JAR_SRC"
bash "$ROOT/scripts/deploy/deploy.sh" "$ROOT/scripts/config/dere-prod.conf" "$JAR_SRC"
rm -f "$JAR_SRC"
echo ALL_DEPLOYED
