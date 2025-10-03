package com.sismico.kafka_spring.order

import org.apache.kafka.clients.admin.NewTopic
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class KafkaTopicConfig {

    @Bean
    fun ordersTopic(@Value("\${app.kafka.topic.orders}") topic: String): NewTopic {
        // single partition, replication factor 1
        return NewTopic(topic, 3, 1)
    }
}