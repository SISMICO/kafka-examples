#!/usr/bin/env bash
set -euo pipefail

# order-subscriber-avro.sh
# Subscribe to a Kafka topic using Avro deserialization via Schema Registry.
# Leverages Confluent's kafka-avro-console-consumer inside Docker.
#
# Usage:
#   ./scripts/order-subscriber-avro.sh [topic]
#
# Optional environment variables:
#   COMPOSE_NETWORK       Docker Compose network name (default: kafka-examples_default)
#   BOOTSTRAP_SERVERS     Kafka bootstrap servers (default: broker:29092)
#   SCHEMA_REGISTRY_URL   Schema Registry URL (default: http://schema-registry:8081)
#   CONFLUENT_IMAGE       Docker image to use (default: confluentinc/cp-schema-registry:7.9.3)
#   FROM_BEGINNING_FLAG   Set to "--from-beginning" to read all messages (default: empty)
#   GROUP_ID              Kafka consumer group id (default: orders-subscriber-avro)
#   PRINT_KEY             Whether to print keys (true|false, default: true)
#   KEY_SEPARATOR         Separator between key and value when printing (default: " | ")

TOPIC="${1:-orders}"
IMAGE="${CONFLUENT_IMAGE:-confluentinc/cp-schema-registry:7.9.3}"
NETWORK="${COMPOSE_NETWORK:-kafka-examples_default}"
BOOTSTRAP="${BOOTSTRAP_SERVERS:-broker:29092}"
SCHEMA_URL="${SCHEMA_REGISTRY_URL:-http://schema-registry:8081}"
CONSUMER_GROUP="${GROUP_ID:-orders-subscriber-avro}"
FROM_BEGINNING_FLAG="${FROM_BEGINNING_FLAG:-}"
PRINT_KEY="${PRINT_KEY:-true}"
KEY_SEPARATOR="${KEY_SEPARATOR:- | }"

echo "Subscribing (Avro) to topic '${TOPIC}' on '${BOOTSTRAP}' with Schema Registry '${SCHEMA_URL}' using image '${IMAGE}' in group '${CONSUMER_GROUP}'..."

exec docker run --rm -it \
  --network "${NETWORK}" \
  "${IMAGE}" \
  kafka-avro-console-consumer \
  --bootstrap-server "${BOOTSTRAP}" \
  --topic "${TOPIC}" \
  --group "${CONSUMER_GROUP}" \
  ${FROM_BEGINNING_FLAG} \
  --property schema.registry.url="${SCHEMA_URL}" \
  --property print.key="${PRINT_KEY}" \
  --property key.separator="${KEY_SEPARATOR}" \
  --property specific.avro.reader=false