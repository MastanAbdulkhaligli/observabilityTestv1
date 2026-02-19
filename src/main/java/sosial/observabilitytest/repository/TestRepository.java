package sosial.observabilitytest.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

@Repository
public class TestRepository {

    private static final Logger log = LoggerFactory.getLogger(TestRepository.class);

    public void touch() {
        log.info("repo.touch");
    }

    /**
     * Simulate a DB read. Delay makes it show up as a slow nested operation.
     */
    public void findUserSimulated(String userId, long dbDelayMs) {
        log.info("repo.findUser start userId={} dbDelayMs={}", userId, dbDelayMs);
        sleepQuietly(dbDelayMs);
        log.info("repo.findUser end userId={}", userId);
    }

    /**
     * Simulate a DB write.
     */
    public void saveOrderSimulated(String userId, long amount) {
        log.info("repo.saveOrder start userId={} amount={}", userId, amount);
        // keep it small but non-zero so it appears
        sleepQuietly(30);
        log.info("repo.saveOrder end userId={}", userId);
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
