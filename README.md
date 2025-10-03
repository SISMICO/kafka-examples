# kafka-examples
Examples to learn and try tools for Kafka

## Kafka Spring: Simple Publisher
This module exposes an endpoint to publish 50 Kafka messages with a random key (1..10) and a random text value.

- Topic: `orders` (ensure the topic exists; docker-compose also creates `order` as an example)
- Key: String (numeric text 1..10)
- Value: String (random UUID text)
- Delay: 500ms between each message

### How to run
1. Start Kafka via Docker Compose at the project root:
   docker compose up -d broker schema-registry

2. Start the Spring Boot app:
   cd kafka-spring
   ./gradlew bootRun

3. Trigger publishing 50 messages:
   curl -X POST http://localhost:8080/order

4. Inspect messages (optional) using Confluent Control Center: http://localhost:9021 (if you started the full stack),
   or use any Kafka CLI/consumer on topic `orders` against `localhost:9092`.

### CLI Consumers
- Plain String consumer (inside Docker):
  ./scripts/order-subscriber.sh [topic]

- Avro consumer (inside Docker, requires Schema Registry):
  ./scripts/order-subscriber-avro.sh [topic]
  
  Environment overrides:
  - COMPOSE_NETWORK (default: kafka-examples_default)
  - BOOTSTRAP_SERVERS (default: broker:29092)
  - SCHEMA_REGISTRY_URL (default: http://schema-registry:8081)
  - GROUP_ID (default: orders-subscriber-avro)
  - FROM_BEGINNING_FLAG (set to --from-beginning to read all messages)
