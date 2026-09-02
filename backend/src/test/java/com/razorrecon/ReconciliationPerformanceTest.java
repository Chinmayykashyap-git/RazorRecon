package com.razorrecon;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.razorrecon.engine.ReconciliationEngine;
import com.razorrecon.model.BankTransaction;
import com.razorrecon.model.LedgerEntry;
import com.razorrecon.model.MatchResult;

@SpringBootTest
class ReconciliationPerformanceTest {

    @Autowired
    private ReconciliationEngine engine;

    @Test
    void processLargeDataset() {
        List<BankTransaction> transactions = new ArrayList<>();
        List<LedgerEntry> ledger = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        for (int i = 0; i < 1000; i++) {
            transactions.add(new BankTransaction(
                    "TXN-" + i,
                    "INV-" + i,
                    BigDecimal.valueOf(1000L + i),
                    now,
                    "PAID"
            ));
            ledger.add(new LedgerEntry(
                    "LEDGER-" + i,
                    "INV-" + i,
                    BigDecimal.valueOf(1000L + i),
                    now,
                    "PAYMENT"
            ));
        }

        long start = System.currentTimeMillis();
        List<MatchResult> results = engine.reconcileAll(transactions, ledger);
        long duration = System.currentTimeMillis() - start;

        assertEquals(1000, results.size());
        System.out.println("Processed 1000 transactions in " + duration + " ms");
    }
}
