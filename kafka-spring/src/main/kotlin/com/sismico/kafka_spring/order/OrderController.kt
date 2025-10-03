package com.sismico.kafka_spring.order

import com.sismico.kafka_spring.order.OrderProducerService
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executors

@RestController
@RequestMapping("/order")
class OrderController(
    private val producerService: OrderProducerService
) {
    private val log = LoggerFactory.getLogger(OrderController::class.java)
    private val executor = Executors.newSingleThreadExecutor { r ->
        Thread(r, "order-publisher-thread").apply { isDaemon = true }
    }

    @PostMapping
    fun publishOrders(): ResponseEntity<String> {
        // Run publishing asynchronously to quickly return 202 to the client
        CompletableFuture.runAsync({
            try {
                producerService.publish50Orders()
            } catch (ex: Exception) {
                log.error("Error while publishing orders", ex)
            }
        }, executor)

        return ResponseEntity.status(HttpStatus.ACCEPTED)
            .body("Publishing 50 order messages started")
    }
}