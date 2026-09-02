package com.razorrecon.integration.razorpay;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record RazorpayTransaction(
        String id,
        String orderId,
        BigDecimal amount,
        String currency,
        String status,
        LocalDateTime createdAt) {
}
