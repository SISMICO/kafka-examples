package com.sismico.kafka_spring.order

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import java.util.UUID
import kotlin.random.Random

@Service
class OrderProducerService(
    private val kafkaTemplate: KafkaTemplate<String, String>,
    @Value("\${app.kafka.topic.orders}") private val topic: String
) {
    private val log = LoggerFactory.getLogger(OrderProducerService::class.java)

    fun publish50Orders() {
        // Send 50 messages with 500ms delay between them
        repeat(50) { index ->
            val keyInt = Random.Default.nextInt(1, 11) // 1..10 inclusive upper bound exclusive
            val key = keyInt.toString()
            val value = "$key - ${randomText()}"

            log.info("Sending message {} to topic={} key={} value={}", index + 1, topic, key, value)
            kafkaTemplate.send(topic, key, value)

            try {
                Thread.sleep(500)
            } catch (ie: InterruptedException) {
                Thread.currentThread().interrupt()
                return
            }
        }
        log.info("Finished publishing 50 messages to topic={}", topic)
    }

    private fun randomText(): String = UUID.randomUUID().toString()
}