package com.razorrecon.integration.razorpay;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.razorrecon.model.BankTransaction;

@Component
public class RazorpayAdapter {

    public BankTransaction toBankTransaction(RazorpayTransaction transaction) {
        return new BankTransaction(
                transaction.id(),
                transaction.orderId(),
                transaction.amount(),
                transaction.createdAt(),
                transaction.status()
        );
    }

    public RazorpayTransaction fromWebhook(
            String paymentId,
            String orderId,
            BigDecimal amount,
            String currency,
            String status) {
        return new RazorpayTransaction(
                paymentId,
                orderId,
                amount,
                currency,
                status,
                LocalDateTime.now()
        );
    }
}
