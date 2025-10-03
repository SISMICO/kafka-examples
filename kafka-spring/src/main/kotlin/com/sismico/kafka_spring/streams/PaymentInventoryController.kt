package com.sismico.kafka_spring.streams

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping
class PaymentInventoryController(
    private val producerService: PaymentInventoryProducerService
) {
    private val log = LoggerFactory.getLogger(PaymentInventoryController::class.java)

    @PostMapping("/payment")
    fun publishPayment(@RequestParam("id") id: Int): ResponseEntity<String> {
        log.info("Received request to publish payment message for id={}", id)
        producerService.publishPayment(id)
        return ResponseEntity.status(HttpStatus.ACCEPTED)
            .body("Payment message published for id=$id")
    }

    @PostMapping("/inventory")
    fun publishInventory(@RequestParam("id") id: Int): ResponseEntity<String> {
        log.info("Received request to publish inventory message for id={}", id)
        producerService.publishInventory(id)
        return ResponseEntity.status(HttpStatus.ACCEPTED)
            .body("Inventory message published for id=$id")
    }
}
