package com.razorrecon.rules;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

@Component
public class RefundRule {

    public boolean isRefundRelated(
            BigDecimal originalAmount,
            BigDecimal refundAmount) {

        if (originalAmount == null || refundAmount == null) {
            return false;
        }

        if (refundAmount.compareTo(BigDecimal.ZERO) <= 0) {
            return false;
        }

        return refundAmount.compareTo(originalAmount) <= 0;
    }
}