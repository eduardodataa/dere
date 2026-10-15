#!/usr/bin/env bash
set -uo pipefail
for u in dere.service dere-hml.service dere-staging.service; do
  echo ===== "$u" =====
  sudo systemctl cat "$u"
  echo
done
echo === process args ===
ps auxww | grep -E 'dere-poc|server.port' | grep -v grep || true
