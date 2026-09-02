package com.razorrecon.matcher;

import org.springframework.stereotype.Component;

import com.razorrecon.model.BankTransaction;
import com.razorrecon.model.LedgerEntry;
import com.razorrecon.model.MatchResult;
import com.razorrecon.rules.DateWindowRule;
import com.razorrecon.rules.ReferenceSimilarityRule;

@Component
public class FuzzyMatcher implements Matcher {

    private final DateWindowRule dateWindowRule;
    private final ReferenceSimilarityRule referenceRule;

    public FuzzyMatcher(
            DateWindowRule dateWindowRule,
            ReferenceSimilarityRule referenceRule) {

        this.dateWindowRule = dateWindowRule;
        this.referenceRule = referenceRule;
    }

    @Override
    public MatchResult match(
            BankTransaction transaction,
            LedgerEntry ledgerEntry) {

        boolean referenceMatch =
                referenceRule.matches(
                        transaction.getReference(),
                        ledgerEntry.getReference()
                );

        boolean dateMatch =
                dateWindowRule.matches(
                        transaction.getTransactionDate(),
                        ledgerEntry.getEntryDate()
                );

        boolean amountMatch =
                transaction.getAmount() != null &&
                ledgerEntry.getAmount() != null &&
                transaction.getAmount()
                        .compareTo(ledgerEntry.getAmount()) == 0;

        if (referenceMatch && amountMatch && dateMatch) {

            return new MatchResult(
                    MatchResult.Status.MATCHED,
                    transaction.getTransactionId(),
                    ledgerEntry.getLedgerId(),
                    "Fuzzy reference, amount and date-window match",
                    0.90,
                    "TIER_2"
            );
        }

        if (referenceMatch && dateMatch) {

            return new MatchResult(
                    MatchResult.Status.AMOUNT_MISMATCH,
                    transaction.getTransactionId(),
                    ledgerEntry.getLedgerId(),
                    "Reference and settlement window match, but amount differs",
                    0.75,
                    "TIER_2"
            );
        }

        return new MatchResult(
                MatchResult.Status.UNMATCHED,
                transaction.getTransactionId(),
                null,
                "No rule-based fuzzy match",
                0.0,
                "TIER_2"
        );
    }
}