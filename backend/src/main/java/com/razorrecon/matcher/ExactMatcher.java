package com.razorrecon.matcher;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import com.razorrecon.model.BankTransaction;
import com.razorrecon.model.LedgerEntry;
import com.razorrecon.model.MatchResult;

@Component
public class ExactMatcher implements Matcher {

    @Override
    public MatchResult match(
            BankTransaction transaction,
            LedgerEntry ledgerEntry) {

        if (transaction == null || ledgerEntry == null) {

            return new MatchResult(
                    MatchResult.Status.UNMATCHED,
                    transaction != null
                            ? transaction.getTransactionId()
                            : null,
                    null,
                    "Missing transaction or ledger entry",
                    0.0,
                    "TIER_1"
            );
        }

        boolean referenceMatches =
                transaction.getReference() != null &&
                transaction.getReference()
                        .equalsIgnoreCase(ledgerEntry.getReference());

        boolean amountMatches =
                transaction.getAmount() != null &&
                ledgerEntry.getAmount() != null &&
                transaction.getAmount()
                        .compareTo(ledgerEntry.getAmount()) == 0;

        if (referenceMatches && amountMatches) {

            return new MatchResult(
                    MatchResult.Status.MATCHED,
                    transaction.getTransactionId(),
                    ledgerEntry.getLedgerId(),
                    "Exact reference and amount match",
                    1.0,
                    "TIER_1"
            );
        }

        if (referenceMatches && !amountMatches) {

            return new MatchResult(
                    MatchResult.Status.AMOUNT_MISMATCH,
                    transaction.getTransactionId(),
                    ledgerEntry.getLedgerId(),
                    "Reference matched but amount differs",
                    0.90,
                    "TIER_1"
            );
        }

        return new MatchResult(
                MatchResult.Status.UNMATCHED,
                transaction.getTransactionId(),
                null,
                "No exact reference and amount match",
                0.0,
                "TIER_1"
        );
    }
}