package com.razorrecon.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.razorrecon.engine.ReconciliationEngine;
import com.razorrecon.model.BankTransaction;
import com.razorrecon.model.LedgerEntry;
import com.razorrecon.model.MatchResult;

@RestController
@RequestMapping("/api/reconciliation")
@CrossOrigin(origins = "*")
public class ReconciliationController {

    private final ReconciliationEngine reconciliationEngine;

    public ReconciliationController(
            ReconciliationEngine reconciliationEngine) {

        this.reconciliationEngine = reconciliationEngine;
    }

    @PostMapping("/match")
    public ResponseEntity<MatchResult> match(
            @RequestBody ReconciliationRequest request) {

        MatchResult result =
                reconciliationEngine.reconcile(
                        request.transaction(),
                        request.ledgerEntries()
                );

        return ResponseEntity.ok(result);
    }

    @PostMapping("/batch")
    public ResponseEntity<List<MatchResult>> batch(
            @RequestBody ReconciliationBatchRequest request) {

        List<MatchResult> results =
                reconciliationEngine.reconcileAll(
                        request.transactions(),
                        request.ledgerEntries()
                );

        return ResponseEntity.ok(results);
    }

    public record ReconciliationRequest(
            BankTransaction transaction,
            List<LedgerEntry> ledgerEntries
    ) {}

    public record ReconciliationBatchRequest(
            List<BankTransaction> transactions,
            List<LedgerEntry> ledgerEntries
    ) {}
}