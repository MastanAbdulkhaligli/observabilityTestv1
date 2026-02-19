package sosial.observabilitytest.web.dto;

public record CheckoutRequest(
        String userId,
        long amount,
        Simulate simulate
) {
    public record Simulate(
            long dbDelayMs,
            double externalFailRate,
            boolean businessFail,
            long externalBaseMs
    ) {}
}
