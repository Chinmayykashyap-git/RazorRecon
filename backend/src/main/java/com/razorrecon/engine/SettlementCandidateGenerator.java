package com.razorrecon.engine;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.razorrecon.model.Payment;
import com.razorrecon.model.Settlement;
import com.razorrecon.model.SettlementCandidate;

@Component
public class SettlementCandidateGenerator {

    private static final Duration MAX_TIME_DIFFERENCE = Duration.ofMinutes(5);
    private static final BigDecimal MAX_AMOUNT_DIFFERENCE = new BigDecimal("100.00");

    public List<SettlementCandidate> generate(Payment payment, List<Settlement> settlements) {
        List<SettlementCandidate> candidates = new ArrayList<>();
        if (payment == null || payment.getAmount() == null || payment.getCreatedAt() == null
                || settlements == null) {
            return candidates;
        }
        for (Settlement settlement : settlements) {
            if (settlement == null || settlement.getSettlementId() == null
                    || settlement.getAmount() == null || settlement.getSettlementDate() == null
                    || !sameIfPresent(payment.getCurrency(), settlement.getCurrency())
                    || !sameIfPresent(payment.getMerchantId(), settlement.getMerchantId())) {
                continue;
            }
            long seconds = Math.abs(Duration.between(payment.getCreatedAt(), settlement.getSettlementDate()).getSeconds());
            BigDecimal amountDifference = payment.getAmount().subtract(settlement.getAmount()).abs();
            if (seconds <= MAX_TIME_DIFFERENCE.toSeconds()
                    && amountDifference.compareTo(MAX_AMOUNT_DIFFERENCE) <= 0) {
                candidates.add(new SettlementCandidate(settlement.getSettlementId(),
                        amountDifference.doubleValue(), seconds));
            }
        }
        return candidates;
    }

    private boolean sameIfPresent(String expected, String actual) {
        return expected == null || actual == null || expected.equalsIgnoreCase(actual);
    }
}