package com.sismico.kafka_spring.streams

import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.kstream.JoinWindows
import org.apache.kafka.streams.kstream.KStream
import org.apache.kafka.streams.kstream.StreamJoined
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafkaStreams
import java.time.Duration

@Configuration
@EnableKafkaStreams
class PaymentInventoryStreamConfig(
    @Value("\${app.kafka.topic.payments}") private val paymentsTopic: String,
    @Value("\${app.kafka.topic.inventory}") private val inventoryTopic: String,
    @Value("\${app.kafka.topic.delivery}") private val deliveryTopic: String,
) {
    private val log = LoggerFactory.getLogger(PaymentInventoryStreamConfig::class.java)

    @Bean
    fun paymentInventoryJoin(builder: StreamsBuilder): KStream<String, String> {
        val payments: KStream<String, String> = builder.stream(paymentsTopic)
        val inventory: KStream<String, String> = builder.stream(inventoryTopic)

        val joined: KStream<String, String> = payments.join(
            inventory,
            { payment, inv ->
                // Combine values from payments and inventory
                "{" +
                        "\"payment\":\"$payment\"," +
                        "\"inventory\":\"$inv\"" +
                        "}"
            },
            // 1-minute window for matching by key
            JoinWindows.ofTimeDifferenceWithNoGrace(Duration.ofMinutes(1)),
            StreamJoined.with(Serdes.String(), Serdes.String(), Serdes.String())
        )

        joined.peek { key, value ->
            log.info("Joined payment/inventory for key={} -> {}", key, value)
        }.to(deliveryTopic)

        return joined
    }
}
