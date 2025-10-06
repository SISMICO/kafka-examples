package org.example.kafkadlt.kafkadlt.consumer

import org.example.kafkadlt.kafkadlt.model.Pizza
import org.example.kafkadlt.kafkadlt.service.ProbabilityFailureService
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.messaging.Message
import java.util.function.Consumer

@Configuration
class StreamPizzaConsumer(
    private val probabilityFailureService: ProbabilityFailureService
) {
    private val log = LoggerFactory.getLogger(javaClass)

    @Bean
    fun pizzaStreamConsumer(): Consumer<Message<Pizza>> = Consumer { message ->
        val pizza = message.payload
        val attemptHeader = message.headers.get(KafkaHeaders.DELIVERY_ATTEMPT, Int::class.java)
            ?: message.headers.get("deliveryAttempt") as? Int
        val attempt = attemptHeader ?: 1
        log.info("[StreamConsumer] Received pizza: {} (attempt={})", pizza, attempt)
        probabilityFailureService.maybeFail("SpringCloudStreamConsumer for ${pizza.id}")
        val retries = attempt - 1
        log.info("[StreamConsumer] Processed pizza {} successfully after {} attempt(s) (retries={})", pizza.id, attempt, retries)
    }
}
