#!/usr/bin/env bash
set -euo pipefail
BASE_URL="${1:-http://localhost:8080/api/status}"
REQUESTS="${2:-100}"
CONCURRENCY="${3:-10}"
if ! command -v hey >/dev/null 2>&1; then
  echo "Install hey first: https://github.com/rakyll/hey"
  exit 1
fi
hey -n "$REQUESTS" -c "$CONCURRENCY" "$BASE_URL"
