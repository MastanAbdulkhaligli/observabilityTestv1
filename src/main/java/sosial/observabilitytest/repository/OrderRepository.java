package sosial.observabilitytest.repository;

import org.springframework.stereotype.Repository;

@Repository
public class OrderRepository {

    public void touch(long delayMs) {
        sleep(delayMs);
        System.out.println("I am in Repository (simulated DB)");
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
