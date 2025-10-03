package com.sismico.kafka_spring

import org.apache.kafka.clients.admin.NewTopic
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class KafkaTopicConfig {

    @Bean
    fun ordersTopic(@Value("\${app.kafka.topic.orders}") topic: String): NewTopic {
        return NewTopic(topic, 3, 1)
    }

    @Bean
    fun paymentsTopic(@Value("\${app.kafka.topic.payments}") topic: String): NewTopic {
        return NewTopic(topic, 3, 1)
    }

    @Bean
    fun inventoryTopic(@Value("\${app.kafka.topic.inventory}") topic: String): NewTopic {
        return NewTopic(topic, 3, 1)
    }

    @Bean
    fun deliveryTopic(@Value("\${app.kafka.topic.delivery}") topic: String): NewTopic {
        return NewTopic(topic, 3, 1)
    }
}