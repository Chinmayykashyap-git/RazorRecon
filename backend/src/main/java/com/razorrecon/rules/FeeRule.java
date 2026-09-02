package com.razorrecon.rules;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.stereotype.Component;

@Component
public class FeeRule {

    private static final BigDecimal DEFAULT_FEE_RATE =
            new BigDecimal("0.02");

    private static final BigDecimal TOLERANCE =
            new BigDecimal("0.01");

    public boolean matches(
            BigDecimal originalAmount,
            BigDecimal settledAmount) {

        if (originalAmount == null || settledAmount == null) {
            return false;
        }

        BigDecimal expectedFee =
                originalAmount
                        .multiply(DEFAULT_FEE_RATE)
                        .setScale(2, RoundingMode.HALF_UP);

        BigDecimal expectedSettlement =
                originalAmount.subtract(expectedFee);

        BigDecimal difference =
                expectedSettlement
                        .subtract(settledAmount)
                        .abs();

        return difference.compareTo(TOLERANCE) <= 0;
    }

    public BigDecimal calculateFee(BigDecimal amount) {

        return amount
                .multiply(DEFAULT_FEE_RATE)
                .setScale(2, RoundingMode.HALF_UP);
    }
}