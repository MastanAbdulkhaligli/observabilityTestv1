package sosial.observabilitytest.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sosial.observabilitytest.service.CheckoutService;
import sosial.observabilitytest.web.dto.CheckoutRequest;
import sosial.observabilitytest.web.dto.CheckoutResponse;

@RestController
public class CheckoutController {

    private final CheckoutService checkoutService;

    public CheckoutController(CheckoutService checkoutService) {
        this.checkoutService = checkoutService;
    }

    @PostMapping("/checkout")
    public ResponseEntity<CheckoutResponse> checkout(@RequestBody CheckoutRequest req) {
        CheckoutResponse resp = checkoutService.checkout(req);
        return ResponseEntity.ok(resp);
    }

    // keep your old endpoints too
    @GetMapping("/ok")
    public String ok() {
        checkoutService.touchOk();
        return "ok";
    }

    @GetMapping("/fail")
    public String fail() {
        checkoutService.touchOk();
        throw new RuntimeException("boom");
    }
}
