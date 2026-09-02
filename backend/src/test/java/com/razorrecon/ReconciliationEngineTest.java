package com.razorrecon;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.razorrecon.engine.ReconciliationEngine;
import com.razorrecon.model.BankTransaction;
import com.razorrecon.model.LedgerEntry;
import com.razorrecon.model.MatchResult;

@SpringBootTest
class ReconciliationEngineTest {

    @Autowired
    private ReconciliationEngine engine;

    @Test
    void exactMatchShouldBeMatched() {
        BankTransaction transaction = new BankTransaction(
                "TXN-001", "INV-001", BigDecimal.valueOf(1000), LocalDateTime.now(), "PAID"
        );
        LedgerEntry ledger = new LedgerEntry(
                "LEDGER-001", "INV-001", BigDecimal.valueOf(1000), LocalDateTime.now(), "PAYMENT"
        );

        MatchResult result = engine.reconcile(transaction, List.of(ledger));

        assertEquals(MatchResult.Status.MATCHED, result.getStatus());
    }

    @Test
    void emptyCandidatesShouldNotCrash() {
        BankTransaction transaction = new BankTransaction(
                "TXN-002", "INV-002", BigDecimal.valueOf(1000), LocalDateTime.now(), "PAID"
        );

        MatchResult result = engine.reconcile(transaction, List.of());

        assertNotNull(result);
        assertEquals(MatchResult.Status.UNMATCHED, result.getStatus());
    }

    @Test
    void completelyDifferentTransactionShouldNotCrash() {
        BankTransaction transaction = new BankTransaction(
                "TXN-003", "UNKNOWN", BigDecimal.valueOf(5000), LocalDateTime.now(), "PAID"
        );
        LedgerEntry ledger = new LedgerEntry(
                "LEDGER-003", "OTHER", BigDecimal.valueOf(100),
                LocalDateTime.now().minusDays(20), "PAYMENT"
        );

        MatchResult result = engine.reconcile(transaction, List.of(ledger));

        assertNotNull(result);
    }

    @Test
    void nullLedgerListShouldNotCrash() {
        BankTransaction transaction = new BankTransaction(
                "TXN-004", "INV-004", BigDecimal.valueOf(1000), LocalDateTime.now(), "PAID"
        );

        MatchResult result = engine.reconcile(transaction, null);

        assertNotNull(result);
    }
}
