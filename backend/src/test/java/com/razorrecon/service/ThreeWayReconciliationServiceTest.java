package com.razorrecon.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import com.razorrecon.model.LedgerEntry;
import com.razorrecon.model.Payment;
import com.razorrecon.model.Settlement;
import com.razorrecon.model.ThreeWayReconciliationResult.Status;

class ThreeWayReconciliationServiceTest {

    private final ThreeWayReconciliationService service = new ThreeWayReconciliationService();
    private final LocalDateTime timestamp = LocalDateTime.of(2026, 8, 21, 10, 0);

    @Test
    void matchesPaymentSettlementAndLedgerBySharedReference() {
        var results = service.reconcile(List.of(payment("TXN-1", "REF-1", "100.00")),
                List.of(settlement("SET-1", "REF-1", "100.00")),
                List.of(ledger("LED-1", "REF-1", "100.00")));

        assertEquals(Status.MATCHED, results.get(0).status());
        assertEquals(1.0, results.get(0).confidence());
    }

    @Test
    void classifiesPartialSettlement() {
        var results = service.reconcile(List.of(payment("TXN-1", "REF-1", "100.00")),
                List.of(settlement("SET-1", "REF-1", "97.00")), List.of());

        assertEquals(Status.PARTIAL_SETTLEMENT, results.get(0).status());
        assertEquals(new BigDecimal("3.00"), results.get(0).difference());
    }

    @Test
    void classifiesMissingAndDuplicateRecords() {
        var results = service.reconcile(List.of(payment("TXN-1", "REF-1", "100.00")),
                List.of(settlement("SET-1", "REF-1", "100.00"), settlement("SET-2", "REF-1", "100.00")),
                List.of());

        assertEquals(Status.DUPLICATE, results.get(0).status());
        assertEquals(Status.MISSING_PAYMENT, results.get(1).status());
        assertEquals(Status.MISSING_PAYMENT, results.get(2).status());
    }

    @Test
    void classifiesInvalidPaymentWithoutCrashing() {
        var invalid = new Payment();
        var results = service.reconcile(List.of(invalid), List.of(), List.of());

        assertEquals(Status.INVALID_RECORD, results.get(0).status());
    }

    private Payment payment(String id, String reference, String amount) {
        return new Payment(id, "ORDER-1", new BigDecimal(amount), "INR", "SUCCESS", timestamp, reference);
    }

    private Settlement settlement(String id, String reference, String amount) {
        return new Settlement(id, reference, new BigDecimal(amount), timestamp.plusMinutes(2), "SETTLED");
    }

    private LedgerEntry ledger(String id, String reference, String amount) {
        return new LedgerEntry(id, reference, new BigDecimal(amount), timestamp.plusMinutes(3), "CREDIT");
    }
}
