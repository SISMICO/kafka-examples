package com.sismico.kafka_spring.streams

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class PaymentInventoryProducerService(
    private val kafkaTemplate: KafkaTemplate<String, String>,
    @Value("\${app.kafka.topic.payments}") private val paymentsTopic: String,
    @Value("\${app.kafka.topic.inventory}") private val inventoryTopic: String,
) {
    private val log = LoggerFactory.getLogger(PaymentInventoryProducerService::class.java)

    fun publishPayment(id: Int) {
        val key = id.toString()
        val value = "ID=$id, random=${UUID.randomUUID()}"
        log.info("Sending payment message to topic={} key={} value={}", paymentsTopic, key, value)
        kafkaTemplate.send(paymentsTopic, key, value)
    }

    fun publishInventory(id: Int) {
        val key = id.toString()
        val value = "ID=$id, random=${UUID.randomUUID()}"
        log.info("Sending inventory message to topic={} key={} value={}", inventoryTopic, key, value)
        kafkaTemplate.send(inventoryTopic, key, value)
    }
}
