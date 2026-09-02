package com.razorrecon.model;

import java.math.BigDecimal;

public record ThreeWayReconciliationResult(
        String paymentId,
        String settlementId,
        String ledgerId,
        Status status,
        BigDecimal expectedAmount,
        BigDecimal actualAmount,
        BigDecimal difference,
        double confidence,
        String reason) {

    public enum Status {
        MATCHED,
        PROBABLE_MATCH,
        AMOUNT_MISMATCH,
        MISSING_PAYMENT,
        MISSING_SETTLEMENT,
        DUPLICATE,
        PARTIAL_SETTLEMENT,
        INVALID_RECORD,
        MANUAL_REVIEW
    }
}
