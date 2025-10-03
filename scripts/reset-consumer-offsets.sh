#!/usr/bin/env bash
set -euo pipefail

# reset-consumer-offsets.sh
# Reset Kafka consumer group offsets using dockerized kafka-consumer-groups.
# This script runs the Kafka CLI inside the docker-compose network so you
# don't need Kafka installed locally.
#
# Usage:
#   ./scripts/reset-consumer-offsets.sh <group_id> [topic] [extra reset options]
#
# Examples:
#   # Reset a group to earliest on topic 'order'
#   ./scripts/reset-consumer-offsets.sh my-group
#
#   # Reset to latest on a specific topic
#   ./scripts/reset-consumer-offsets.sh my-group order --to-latest
#
#   # Shift back 10 messages for the group on topic 'order'
#   ./scripts/reset-consumer-offsets.sh my-group order --shift-by -10
#
#   # Reset by time duration (e.g., 24 hours ago)
#   ./scripts/reset-consumer-offsets.sh my-group order --by-duration PT24H
#
# Notes:
#   - You must provide exactly one reset strategy among Kafka's supported options,
#     such as: --to-earliest, --to-latest, --to-offset <n>, --shift-by <n>,
#     --by-duration <duration>, --to-datetime <yyyy-MM-ddTHH:mm:ss.SSS>
#   - This script defaults to --to-earliest if no extra options are provided.
#
# Optional environment variables:
#   COMPOSE_NETWORK     Docker Compose network name (default: kafka-examples_default)
#   BOOTSTRAP_SERVERS   Kafka bootstrap servers (default: broker:29092)
#   CONFLUENT_IMAGE     Docker image to use (default: confluentinc/cp-kafka:7.9.3)

if [[ ${1:-} == "-h" || ${1:-} == "--help" || $# -lt 1 ]]; then
  echo "Usage: $0 <group_id> [topic] [extra reset options]" >&2
  echo "Example: $0 my-group order --to-earliest" >&2
  exit 1
fi

GROUP_ID="$1"
TOPIC="${2:-order}"

# Collect extra options from the 3rd argument onward; default to --to-earliest if none provided.
if [[ $# -ge 3 ]]; then
  # shellcheck disable=SC2206
  EXTRA_OPTS=("${@:3}")
else
  EXTRA_OPTS=("--to-earliest")
fi

IMAGE="${CONFLUENT_IMAGE:-confluentinc/cp-kafka:7.9.3}"
NETWORK="${COMPOSE_NETWORK:-kafka-examples_default}"
BOOTSTRAP="${BOOTSTRAP_SERVERS:-broker:29092}"

echo "Resetting offsets for group '${GROUP_ID}' on topic '${TOPIC}' (bootstrap: ${BOOTSTRAP}) with options: ${EXTRA_OPTS[*]}"

exec docker run --rm -it \
  --network "${NETWORK}" \
  "${IMAGE}" \
  kafka-consumer-groups \
  --bootstrap-server "${BOOTSTRAP}" \
  --group "${GROUP_ID}" \
  --topic "${TOPIC}" \
  --reset-offsets "${EXTRA_OPTS[@]}" \
  --execute
