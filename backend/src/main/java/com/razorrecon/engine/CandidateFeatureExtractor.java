package com.razorrecon.engine;

import java.time.Duration;

import org.springframework.stereotype.Component;

import com.razorrecon.model.CandidateFeatures;
import com.razorrecon.model.Payment;
import com.razorrecon.model.Settlement;

@Component
public class CandidateFeatureExtractor {

    public CandidateFeatures extract(Payment payment, Settlement settlement) {
        if (payment == null || settlement == null || payment.getAmount() == null
                || settlement.getAmount() == null || payment.getCreatedAt() == null
                || settlement.getSettlementDate() == null) {
            throw new IllegalArgumentException("Payment and settlement evidence is incomplete");
        }
        return new CandidateFeatures(
                payment.getAmount().subtract(settlement.getAmount()).abs(),
                Math.abs(Duration.between(payment.getCreatedAt(), settlement.getSettlementDate()).getSeconds()),
                same(payment.getMerchantId(), settlement.getMerchantId()),
                same(payment.getCurrency(), settlement.getCurrency()),
                same(payment.getPaymentMethod(), settlement.getPaymentMethod()),
                similarity(payment.getReference(), settlement.getReference()));
    }

    private boolean same(String expected, String actual) {
        return expected != null && actual != null && expected.equalsIgnoreCase(actual);
    }

    private double similarity(String left, String right) {
        if (left == null || right == null) return 0.0;
        if (left.equalsIgnoreCase(right)) return 1.0;
        int[][] distance = new int[left.length() + 1][right.length() + 1];
        for (int i = 0; i <= left.length(); i++) distance[i][0] = i;
        for (int j = 0; j <= right.length(); j++) distance[0][j] = j;
        for (int i = 1; i <= left.length(); i++) {
            for (int j = 1; j <= right.length(); j++) {
                int substitution = distance[i - 1][j - 1] + (Character.toLowerCase(left.charAt(i - 1))
                        == Character.toLowerCase(right.charAt(j - 1)) ? 0 : 1);
                distance[i][j] = Math.min(Math.min(distance[i - 1][j] + 1, distance[i][j - 1] + 1), substitution);
            }
        }
        return 1.0 - ((double) distance[left.length()][right.length()] / Math.max(left.length(), right.length()));
    }
}