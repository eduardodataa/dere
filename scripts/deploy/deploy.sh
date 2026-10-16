#!/bin/bash

set -euo pipefail

CONFIG_FILE="${1:-}"
JAR_SOURCE="${2:-}"

REQUIRED_VARS=(
    APP_NAME
    APP_HOME
    SERVICE_NAME
    JAR_NAME
    HEALTH_URL
    HEALTHCHECK_MAX_ATTEMPTS
    HEALTHCHECK_INTERVAL
)

usage() {
    echo "Uso: $0 <arquivo-config> <arquivo-jar>"
    echo "Exemplo: $0 scripts/config/dere-staging.conf target/dere-poc-0.0.1-SNAPSHOT.jar"
    exit 1
}

fail() {
    echo "ERRO: $1" >&2
    exit 1
}

if [ -z "$CONFIG_FILE" ] || [ -z "$JAR_SOURCE" ]; then
    usage
fi

if [ ! -f "$CONFIG_FILE" ]; then
    fail "Arquivo de configuração não encontrado: $CONFIG_FILE"
fi

if [ ! -f "$JAR_SOURCE" ]; then
    fail "JAR não encontrado: $JAR_SOURCE"
fi

source "$CONFIG_FILE"

for var in "${REQUIRED_VARS[@]}"; do
    if [ -z "${!var:-}" ]; then
        fail "Variável obrigatória ausente no config: $var"
    fi
done

if [ ! -d "$APP_HOME" ]; then
    fail "Diretório APP_HOME não existe: $APP_HOME"
fi

if ! systemctl cat "$SERVICE_NAME" &>/dev/null; then
    fail "Serviço systemd não encontrado: $SERVICE_NAME"
fi

CURRENT="$APP_HOME/current"
TARGET="$CURRENT/$JAR_NAME"
BACKUP="$APP_HOME/previous/$JAR_NAME"

echo "======================================="
echo "Deploy $APP_NAME"
echo "Config: $CONFIG_FILE"
echo "JAR origem: $JAR_SOURCE"
echo "JAR destino: $TARGET"
echo "Serviço: $SERVICE_NAME"
echo "Health URL: $HEALTH_URL"
echo "======================================="

sudo mkdir -p "$CURRENT" "$APP_HOME/previous"

if [ -f "$TARGET" ]; then
    echo "Guardando JAR atual para rollback."
    sudo cp -a "$TARGET" "$BACKUP"
fi

echo "Publicando JAR..."
sudo cp "$JAR_SOURCE" "$TARGET"
sudo chown ubuntu:ubuntu "$TARGET"

if [ -n "${DEPLOYMENT:-}" ] && [ -f "$APP_HOME/dere.env" ]; then
    echo "Fixando DERE_DEPLOYMENT=$DEPLOYMENT"
    sudo sed -i '/^DERE_DEPLOYMENT=/d' "$APP_HOME/dere.env"
    printf 'DERE_DEPLOYMENT=%s\n' "$DEPLOYMENT" | sudo tee -a "$APP_HOME/dere.env" >/dev/null
fi

echo "Reiniciando serviço $SERVICE_NAME..."
sudo systemctl restart "$SERVICE_NAME"

echo "Executando healthcheck..."
for ((i = 1; i <= HEALTHCHECK_MAX_ATTEMPTS; i++)); do
    if curl -sf "$HEALTH_URL" >/dev/null; then
        echo "======================================="
        echo "Deploy concluído com sucesso."
        echo "======================================="
        exit 0
    fi
    echo "Tentativa $i/$HEALTHCHECK_MAX_ATTEMPTS"
    if [ "$i" -lt "$HEALTHCHECK_MAX_ATTEMPTS" ]; then
        sleep "$HEALTHCHECK_INTERVAL"
    fi
done

echo "======================================="
echo "ERRO: Healthcheck falhou."
echo "Iniciando rollback..."
echo "======================================="

if [ -f "$BACKUP" ]; then
    sudo cp -a "$BACKUP" "$TARGET"
    sudo chown ubuntu:ubuntu "$TARGET"
    sudo systemctl restart "$SERVICE_NAME"
    sleep 5
    if curl -sf "$HEALTH_URL" >/dev/null; then
        echo "Rollback concluído com sucesso."
    else
        echo "AVISO: Rollback executado, mas healthcheck ainda falhou."
    fi
else
    echo "Não há JAR anterior para rollback."
fi

sudo journalctl -u "$SERVICE_NAME" -n 80 --no-pager || true
exit 1
