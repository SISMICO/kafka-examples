package org.example.kafkadlt.kafkadlt.service

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.concurrent.ThreadLocalRandom

@Service
class ProbabilityFailureService(
    @Value("\${app.failure.probability:0.0}") private val failureProbability: Double
) {
    private val log = LoggerFactory.getLogger(javaClass)

    fun maybeFail(context: String) {
        val r = ThreadLocalRandom.current().nextDouble(0.0, 1.0)
        val shouldFail = r < failureProbability
        if (shouldFail) {
            log.warn("Failing on purpose in {}. Random={} < prob={}", context, r, failureProbability)
            throw RuntimeException("Simulated random failure for DLT testing")
        } else {
            log.info("Processed successfully in {}. Random={} >= prob={}", context, r, failureProbability)
        }
    }
}