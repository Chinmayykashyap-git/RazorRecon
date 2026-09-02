package com.razorrecon.integration.razorpay;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/razorpay")
public class RazorpayWebhookController {

    private final RazorpayAdapter razorpayAdapter;

    public RazorpayWebhookController(RazorpayAdapter razorpayAdapter) {
        this.razorpayAdapter = razorpayAdapter;
    }

    @PostMapping("/webhook")
    public ResponseEntity<Map<String, Object>> webhook(
            @RequestBody RazorpayWebhookRequest request) {
        RazorpayTransaction transaction = razorpayAdapter.fromWebhook(
                request.paymentId(),
                request.orderId(),
                request.amount(),
                request.currency(),
                request.status()
        );

        return ResponseEntity.ok(Map.of(
                "received", true,
                "paymentId", transaction.id(),
                "status", transaction.status()
        ));
    }

    public record RazorpayWebhookRequest(
            String paymentId,
            String orderId,
            BigDecimal amount,
            String currency,
            String status) {
    }
}
