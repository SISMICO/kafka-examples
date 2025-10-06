package org.example.kafkadlt.kafkadlt.api

import org.example.kafkadlt.kafkadlt.model.Pizza
import org.example.kafkadlt.kafkadlt.model.Size
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/pizzas")
class PizzaController(
    private val kafkaTemplate: KafkaTemplate<String, Pizza>,
    @Value("\${app.topic.pizza-created}") private val topic: String
) {

    data class CreatePizzaRequest(
        val name: String,
        val size: Size
    )

    @PostMapping
    fun create(@RequestBody req: CreatePizzaRequest): ResponseEntity<Pizza> {
        val pizza = Pizza(UUID.randomUUID(), req.name, req.size)
        kafkaTemplate.send(topic, pizza.id.toString(), pizza)
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(pizza)
    }
}
