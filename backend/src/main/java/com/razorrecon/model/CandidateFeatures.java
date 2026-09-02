package com.razorrecon.model;

import java.math.BigDecimal;

public record CandidateFeatures(
        BigDecimal amountDifference,
        long timestampDifferenceSeconds,
        boolean merchantMatch,
        boolean currencyMatch,
        boolean paymentMethodMatch,
        double referenceSimilarity) {

    public double[] asVector() {
        return new double[] {
                amountDifference.doubleValue(),
                timestampDifferenceSeconds,
                merchantMatch ? 1.0 : 0.0,
                currencyMatch ? 1.0 : 0.0,
                paymentMethodMatch ? 1.0 : 0.0,
                referenceSimilarity
        };
    }
}
