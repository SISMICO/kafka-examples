package org.example.kafkadlt.kafkadlt

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.kafka.annotation.EnableKafka

@EnableKafka
@SpringBootApplication
class KafkaDltApplication

fun main(args: Array<String>) {
    runApplication<KafkaDltApplication>(*args)
}
