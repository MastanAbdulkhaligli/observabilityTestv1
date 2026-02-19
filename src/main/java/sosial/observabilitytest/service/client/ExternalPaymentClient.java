package sosial.observabilitytest.service.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class ExternalPaymentClient {

    private static final Logger log = LoggerFactory.getLogger(ExternalPaymentClient.class);
    private static final Random RND = new Random();

    /**
     * Simulates a remote payment authorization.
     * - sleeps a bit (to show span duration / metrics latency)
     * - fails with probability failRate (0..1)
     */
    public void authorize(String userId, long amount, double failRate, long baseMs) {
        long workMs = Math.max(0, baseMs) + RND.nextInt(120); // add jitter
        log.info("external call start userId={} amount={} plannedMs={} failRate={}", userId, amount, workMs, failRate);

        sleepQuietly(workMs);

        double p = clamp01(failRate);
        if (RND.nextDouble() < p) {
            // simulate a timeout (very common technical failure)
            throw new ExternalTimeoutException("simulated timeout from payment provider");
        }

        log.info("external call success userId={} amount={}", userId, amount);
    }

    public static final class ExternalTimeoutException extends RuntimeException {
        public ExternalTimeoutException(String message) {
            super(message);
        }
    }

    private static double clamp01(double x) {
        if (Double.isNaN(x)) return 0.0;
        if (x < 0.0) return 0.0;
        if (x > 1.0) return 1.0;
        return x;
    }

    private static void sleepQuietly(long ms) {
        if (ms <= 0) return;
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
    }
}
