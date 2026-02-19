//package sosial.observabilitytest.controller;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import sosial.observabilitytest.service.CheckoutService;
//import sosial.observabilitytest.service.TestService;
//
//@RestController
//public class TestController {
//
//    private static final Logger log = LoggerFactory.getLogger(TestController.class);
//
//    private final TestService service;
//    private final CheckoutService checkoutService;
//
//    public TestController(TestService service, CheckoutService checkoutService) {
//        this.service = service;
//        this.checkoutService = checkoutService;
//    }
//
//    @GetMapping("/ok")
//    public String ok() {
//        return service.ok();
//    }
//
//    @GetMapping("/fail")
//    public String fail() {
//        return service.fail();
//    }
//
//    /**
//     * Scenario endpoint: controller -> service -> repo -> external + retries + slow DB + business error.
//     */
////    @PostMapping("/checkout")
////    public ResponseEntity<?> checkout(@RequestBody CheckoutRequest req) {
////        log.info("checkout request received userId={} amount={}", req.userId(), req.amount());
////
////        CheckoutResponse resp = checkoutService.checkout(req);
////
////        // if business error, return 400 (trace exists, logs exist, metrics exist)
////        if (!resp.success()) {
////            return ResponseEntity.status(resp.httpStatus()).body(resp);
////        }
////        return ResponseEntity.ok(resp);
////    }
//
//    // ---------------- DTOs (kept in same file for convenience) ----------------
//
//    public record CheckoutRequest(
//            String userId,
//            long amount,
//            Simulate simulate
//    ) {
//        public CheckoutRequest {
//            // defaults if simulate is missing
//            if (simulate == null) simulate = new Simulate(0, 0.0, false, 300);
//        }
//    }
//
//    public record Simulate(
//            long dbDelayMs,          // adds latency to "DB"
//            double externalFailRate, // 0.0 .. 1.0
//            boolean businessFail,    // if true -> return 400 (business error)
//            long externalBaseMs      // base duration for external call (ms)
//    ) {
//    }
//
//    public record CheckoutResponse(
//            boolean success,
//            int httpStatus,
//            String outcome,          // SUCCESS | BUSINESS_ERROR | TECHNICAL_ERROR
//            String message,
//            String errorCode,        // e.g. INVALID_COUPON
//            int externalAttempts
//    ) {
//        public static CheckoutResponse success(int attempts) {
//            return new CheckoutResponse(true, 200, "SUCCESS", "checkout completed", null, attempts);
//        }
//
//        public static CheckoutResponse businessError(String code, String msg) {
//            return new CheckoutResponse(false, 400, "BUSINESS_ERROR", msg, code, 0);
//        }
//
//        public static CheckoutResponse technicalError(String msg, int attempts) {
//            return new CheckoutResponse(false, 500, "TECHNICAL_ERROR", msg, null, attempts);
//        }
//    }
//}
