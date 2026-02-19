package sosial.observabilitytest.service;

import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class PaymentGatewayClient {

    public String charge(String userId, long amount, long baseMs, double failRate) {
        // simulate latency
        sleep(baseMs + ThreadLocalRandom.current().nextLong(0, 150));

        // simulate random technical failure
        if (failRate > 0 && ThreadLocalRandom.current().nextDouble() < failRate) {
            throw new RuntimeException("PAYMENT_GATEWAY_TIMEOUT");
        }

        return "pay_" + UUID.randomUUID();
    }

    private static void sleep(long ms) {
        if (ms <= 0) return;
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
    }
}
