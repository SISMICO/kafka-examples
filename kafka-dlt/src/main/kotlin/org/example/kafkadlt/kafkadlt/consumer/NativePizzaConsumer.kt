package org.example.kafkadlt.kafkadlt.consumer

import org.example.kafkadlt.kafkadlt.model.Pizza
import org.example.kafkadlt.kafkadlt.service.ProbabilityFailureService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.annotation.RetryableTopic
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.messaging.handler.annotation.Header
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component

@Component
class NativePizzaConsumer(
    private val probabilityFailureService: ProbabilityFailureService,
    @Value("\${app.topic.pizza-created}") private val topic: String,
    @Value("\${app.kafka.native.group:pizza-native-group}") private val group: String
) {
    private val log = LoggerFactory.getLogger(javaClass)

    @RetryableTopic(
        attempts = "\${app.retry.attempts:3}",
        dltTopicSuffix = "-dlt-native"
    )
    @KafkaListener(topics = ["\${app.topic.pizza-created}"], groupId = "\${app.kafka.native.group:pizza-native-group}")
    fun onMessage(
        @Payload pizza: Pizza,
        @Header(name = KafkaHeaders.DELIVERY_ATTEMPT, required = false) attemptHeader: Int?
    ) {
        val attempt = attemptHeader ?: 1
        log.info("[NativeConsumer] Received pizza: {} (attempt={})", pizza, attempt)
        probabilityFailureService.maybeFail("NativeKafkaConsumer for ${'$'}{pizza.id}")
        val retries = attempt - 1
        log.info("[NativeConsumer] Processed pizza {} successfully after {} attempt(s) (retries={})", pizza.id, attempt, retries)
    }
}