package com.razorrecon.engine;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import com.razorrecon.model.BankTransaction;
import com.razorrecon.model.LedgerEntry;
import com.razorrecon.rules.DateWindowRule;
import com.razorrecon.rules.ReferenceSimilarityRule;

@Component
public class ScoringEngine {

    private final ReferenceSimilarityRule referenceRule;
    private final DateWindowRule dateWindowRule;

    public ScoringEngine(
            ReferenceSimilarityRule referenceRule,
            DateWindowRule dateWindowRule) {

        this.referenceRule = referenceRule;
        this.dateWindowRule = dateWindowRule;
    }

    public double calculateScore(
            BankTransaction transaction,
            LedgerEntry entry) {

        if (transaction == null || entry == null) {
            return 0.0;
        }

        double score = 0.0;

        // Reference similarity = 40 points
        if (referenceRule.matches(
                transaction.getReference(),
                entry.getReference())) {

            score += 40;
        }

        // Exact amount = 35 points
        if (amountMatches(transaction, entry)) {
            score += 35;
        }

        // Settlement date window = 25 points
        if (dateWindowRule.matches(
                transaction.getTransactionDate(),
                entry.getEntryDate())) {

            score += 25;
        }

        return score;
    }

    private boolean amountMatches(
            BankTransaction transaction,
            LedgerEntry entry) {

        BigDecimal transactionAmount =
                transaction.getAmount();

        BigDecimal ledgerAmount =
                entry.getAmount();

        return transactionAmount != null
                && ledgerAmount != null
                && transactionAmount.compareTo(ledgerAmount) == 0;
    }
}