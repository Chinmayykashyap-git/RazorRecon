package com.razorrecon.rules;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.stereotype.Component;

@Component
public class TdsRule {

    private static final BigDecimal TDS_RATE =
            new BigDecimal("0.01");

    private static final BigDecimal TOLERANCE =
            new BigDecimal("0.01");

    public boolean matches(
            BigDecimal originalAmount,
            BigDecimal settledAmount) {

        if (originalAmount == null || settledAmount == null) {
            return false;
        }

        BigDecimal tds =
                originalAmount
                        .multiply(TDS_RATE)
                        .setScale(2, RoundingMode.HALF_UP);

        BigDecimal expected =
                originalAmount.subtract(tds);

        return expected
                .subtract(settledAmount)
                .abs()
                .compareTo(TOLERANCE) <= 0;
    }

    public BigDecimal calculateTds(BigDecimal amount) {

        return amount
                .multiply(TDS_RATE)
                .setScale(2, RoundingMode.HALF_UP);
    }
}