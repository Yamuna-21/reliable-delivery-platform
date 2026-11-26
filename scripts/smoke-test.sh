#!/usr/bin/env bash
set -euo pipefail
BASE_URL="${1:-http://localhost:8080}"
echo "Checking health endpoint: ${BASE_URL}/actuator/health"
curl --fail --show-error --silent "${BASE_URL}/actuator/health"
echo
echo "Checking status endpoint: ${BASE_URL}/api/status"
curl --fail --show-error --silent "${BASE_URL}/api/status"
echo
echo "Smoke tests passed."
