package com.razorrecon.engine;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import com.razorrecon.model.Payment;
import com.razorrecon.model.Settlement;

class CandidateFeatureExtractorTest {

    private final CandidateFeatureExtractor extractor = new CandidateFeatureExtractor();
    private final LocalDateTime timestamp = LocalDateTime.of(2026, 8, 21, 10, 0);

    @Test
    void extractsMatchingEvidence() {
        Payment payment = new Payment("TXN-1", "ORD-1", "M001", new BigDecimal("100.00"),
                "INR", "SUCCESS", timestamp, "UPI", "REF-1");
        Settlement settlement = new Settlement("SET-1", "REF-1", "M001", new BigDecimal("97.00"),
                "INR", timestamp.plusSeconds(42), "SETTLED");
        settlement.setPaymentMethod("UPI");

        var features = extractor.extract(payment, settlement);

        assertEquals(new BigDecimal("3.00"), features.amountDifference());
        assertEquals(42, features.timestampDifferenceSeconds());
        assertEquals(true, features.merchantMatch());
        assertEquals(true, features.currencyMatch());
        assertEquals(true, features.paymentMethodMatch());
        assertEquals(1.0, features.referenceSimilarity());
    }

    @Test
    void givesZeroSimilarityForMissingReference() {
        Payment payment = new Payment("TXN-1", "ORD-1", new BigDecimal("100.00"),
                "INR", "SUCCESS", timestamp, null);
        Settlement settlement = new Settlement("SET-1", null, new BigDecimal("100.00"),
                timestamp, "SETTLED");

        assertEquals(0.0, extractor.extract(payment, settlement).referenceSimilarity());
    }
}
