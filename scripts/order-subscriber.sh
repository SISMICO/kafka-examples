#!/usr/bin/env bash
set -euo pipefail

# order-subscriber.sh
# Subscribe to a Kafka topic (default: "orders") using a Dockerized Kafka console consumer.
# It connects to the broker within the docker-compose network.
#
# Usage:
#   ./scripts/order-subscriber.sh [topic]
#
# Optional environment variables:
#   COMPOSE_NETWORK     Docker Compose network name (default: kafka-examples_default)
#   BOOTSTRAP_SERVERS   Kafka bootstrap servers (default: broker:29092)
#   CONFLUENT_IMAGE     Docker image to use (default: confluentinc/cp-kafka:7.9.3)
#   FROM_BEGINNING_FLAG Flag to control reading from beginning (default: --from-beginning; set to empty to tail new only)
#   GROUP_ID            Kafka consumer group id (default: orders-subscriber)

TOPIC="${1:-orders}"
IMAGE="${CONFLUENT_IMAGE:-confluentinc/cp-kafka:7.9.3}"
NETWORK="${COMPOSE_NETWORK:-kafka-examples_default}"
BOOTSTRAP="${BOOTSTRAP_SERVERS:-broker:29092}"
CONSUMER_GROUP="${GROUP_ID:-orders-subscriber}"

echo "Subscribing to topic '$TOPIC' on bootstrap servers '$BOOTSTRAP' using image '$IMAGE' in group '$CONSUMER_GROUP'..."

exec docker run --rm -it \
  --network "${NETWORK}" \
  "${IMAGE}" \
  kafka-console-consumer \
  --bootstrap-server "${BOOTSTRAP}" \
  --topic "${TOPIC}" \
  --group "${CONSUMER_GROUP}"
