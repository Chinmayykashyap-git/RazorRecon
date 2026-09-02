package com.razorrecon.engine;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.razorrecon.model.Payment;
import com.razorrecon.model.Settlement;

class SettlementCandidateGeneratorTest {

    private final SettlementCandidateGenerator generator = new SettlementCandidateGenerator();
    private final LocalDateTime timestamp = LocalDateTime.of(2026, 8, 21, 10, 0);

    @Test
    void returnsOnlyCandidatesWithMatchingMerchantCurrencyTimeAndAmount() {
        Payment payment = new Payment("TXN-1", "ORD-1", "M001", new BigDecimal("5000.00"),
                "INR", "SUCCESS", timestamp, "UPI", "REF-1");
        List<Settlement> settlements = List.of(
                new Settlement("SET-1", "REF-X", "M001", new BigDecimal("5000.00"), "INR",
                        timestamp.plusSeconds(18), "SETTLED"),
                new Settlement("SET-2", "REF-Y", "M001", new BigDecimal("5000.00"), "INR",
                        timestamp.plusMinutes(6), "SETTLED"),
                new Settlement("SET-3", "REF-Z", "M002", new BigDecimal("5000.00"), "INR",
                        timestamp.plusSeconds(20), "SETTLED"),
                new Settlement("SET-4", "REF-A", "M001", new BigDecimal("5200.00"), "INR",
                        timestamp.plusSeconds(20), "SETTLED"));

        assertEquals(List.of("SET-1"), generator.generate(payment, settlements).stream()
                .map(candidate -> candidate.settlementId()).toList());
    }

    @Test
    void allowsReasonableAmountDifferenceForFeeAdjustedCandidates() {
        Payment payment = new Payment("TXN-1", "ORD-1", "M001", new BigDecimal("5000.00"),
                "INR", "SUCCESS", timestamp, "UPI", "REF-1");
        Settlement settlement = new Settlement("SET-1", "REF-X", "M001", new BigDecimal("4975.00"),
                "INR", timestamp.plusMinutes(1), "SETTLED");

        assertEquals(1, generator.generate(payment, List.of(settlement)).size());
    }
}
