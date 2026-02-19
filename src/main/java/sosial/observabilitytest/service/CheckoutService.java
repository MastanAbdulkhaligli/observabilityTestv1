package sosial.observabilitytest.service;

import org.springframework.stereotype.Service;
import sosial.observabilitytest.repository.OrderRepository;
import sosial.observabilitytest.web.dto.CheckoutRequest;
import sosial.observabilitytest.web.dto.CheckoutResponse;
import sosial.observabilitytest.web.error.BusinessException;

import java.util.UUID;

@Service
public class CheckoutService {

    private final OrderRepository repo;
    private final PaymentGatewayClient paymentClient;

    public CheckoutService(OrderRepository repo, PaymentGatewayClient paymentClient) {
        this.repo = repo;
        this.paymentClient = paymentClient;
    }

    public void touchOk() {
        repo.touch(0);
    }

    public CheckoutResponse checkout(CheckoutRequest req) {
        // 1) Optional simulated DB latency
        long dbDelayMs = req.simulate() != null ? req.simulate().dbDelayMs() : 0;
        repo.touch(dbDelayMs);

        // 2) Optional simulated business failure (4xx style)
        boolean businessFail = req.simulate() != null && req.simulate().businessFail();
        if (businessFail) {
            throw new BusinessException("INSUFFICIENT_FUNDS", "User has insufficient funds");
        }

        // 3) Simulated external call (sometimes fails + has latency)
        long externalBaseMs = req.simulate() != null ? req.simulate().externalBaseMs() : 200;
        double externalFailRate = req.simulate() != null ? req.simulate().externalFailRate() : 0.0;

        String paymentRef = paymentClient.charge(req.userId(), req.amount(), externalBaseMs, externalFailRate);

        // 4) Return OK
        return new CheckoutResponse(
                UUID.randomUUID().toString(),
                "APPROVED",
                paymentRef
        );
    }
}
