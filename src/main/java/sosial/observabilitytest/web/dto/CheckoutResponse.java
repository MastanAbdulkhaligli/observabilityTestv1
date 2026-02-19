package sosial.observabilitytest.web.dto;

public record CheckoutResponse(
        String orderId,
        String status,
        String paymentRef
) {}
