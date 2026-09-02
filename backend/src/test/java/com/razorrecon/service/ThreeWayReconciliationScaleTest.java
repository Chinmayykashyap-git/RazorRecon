package com.razorrecon.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import com.razorrecon.model.LedgerEntry;
import com.razorrecon.model.Payment;
import com.razorrecon.model.Settlement;
import com.razorrecon.model.ThreeWayReconciliationResult.Status;

class ThreeWayReconciliationScaleTest {

    @Test
    void reconcilesFiftyThousandThreeWayRecordsWithIndexedLookups() {
        int recordCount = 50_000;
        LocalDateTime timestamp = LocalDateTime.of(2026, 8, 21, 10, 0);
        List<Payment> payments = new ArrayList<>(recordCount);
        List<Settlement> settlements = new ArrayList<>(recordCount);
        List<LedgerEntry> ledger = new ArrayList<>(recordCount);

        for (int index = 0; index < recordCount; index++) {
            String sequence = String.valueOf(index);
            BigDecimal amount = BigDecimal.valueOf(1000L + index);
            payments.add(new Payment("PAY-" + sequence, "ORDER-" + sequence, amount,
                    "INR", "SUCCESS", timestamp, "REF-" + sequence));
            settlements.add(new Settlement("SET-" + sequence, "REF-" + sequence, amount,
                    timestamp.plusMinutes(2), "SETTLED"));
            ledger.add(new LedgerEntry("LED-" + sequence, "REF-" + sequence, amount,
                    timestamp.plusMinutes(3), "CREDIT"));
        }

        long started = System.nanoTime();
        var results = new ThreeWayReconciliationService().reconcile(payments, settlements, ledger);
        long elapsedMillis = (System.nanoTime() - started) / 1_000_000;

        assertEquals(recordCount, results.size());
        assertTrue(results.stream().allMatch(result -> result.status() == Status.MATCHED));
        System.out.println("Reconciled " + recordCount + " three-way records in " + elapsedMillis + " ms");
    }
}
