#!/usr/bin/env bash
set -euo pipefail

# Simple script to publish pizzas to the existing POST /pizzas endpoint.
# Usage:
#   scripts/publish-pizzas.sh [COUNT]
# Env vars:
#   PIZZA_API_URL - override the endpoint URL (default: http://localhost:8080/pizzas)
# Examples:
#   scripts/publish-pizzas.sh           # posts 10 pizzas
#   scripts/publish-pizzas.sh 25        # posts 25 pizzas
#   PIZZA_API_URL=http://host:port/pizzas scripts/publish-pizzas.sh

URL="${PIZZA_API_URL:-http://localhost:8080/pizzas}"
COUNT="${1:-10}"

SIZES=(P M G)

if ! command -v curl >/dev/null 2>&1; then
  echo "Error: curl is required to run this script." >&2
  exit 1
fi

echo "Posting ${COUNT} pizzas to ${URL}"
for i in $(seq 1 "${COUNT}"); do
  size_index=$((RANDOM % ${#SIZES[@]}))
  size="${SIZES[$size_index]}"
  name="Pizza #${i}"
  payload=$(printf '{"name":"%s","size":"%s"}' "$name" "$size")
  echo "[${i}/${COUNT}] -> ${payload}"
  # Send request
  http_code=$(curl -sS -o /tmp/pizza_response.json -w "%{http_code}" -X POST "$URL" \
    -H "Content-Type: application/json" \
    -d "$payload")
  if [[ "$http_code" =~ ^2 ]]; then
    echo "  Response (${http_code}): $(cat /tmp/pizza_response.json)"
  else
    echo "  Error (${http_code}): $(cat /tmp/pizza_response.json)" >&2
  fi
  rm -f /tmp/pizza_response.json || true
 done

echo "Done."