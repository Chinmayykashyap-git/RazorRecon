package com.razorrecon.matcher;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import com.razorrecon.model.BankTransaction;
import com.razorrecon.model.LedgerEntry;
import com.razorrecon.model.MatchResult;
import com.razorrecon.rules.DateWindowRule;
import com.razorrecon.rules.FeeRule;
import com.razorrecon.rules.ReferenceSimilarityRule;
import com.razorrecon.rules.TdsRule;

@Component
public class RuleBasedMatcher implements Matcher {

    private final DateWindowRule dateWindowRule;
    private final ReferenceSimilarityRule referenceRule;
    private final FeeRule feeRule;
    private final TdsRule tdsRule;

    public RuleBasedMatcher(
            DateWindowRule dateWindowRule,
            ReferenceSimilarityRule referenceRule,
            FeeRule feeRule,
            TdsRule tdsRule) {

        this.dateWindowRule = dateWindowRule;
        this.referenceRule = referenceRule;
        this.feeRule = feeRule;
        this.tdsRule = tdsRule;
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

        BigDecimal transactionAmount =
                transaction.getAmount();

        BigDecimal ledgerAmount =
                ledgerEntry.getAmount();

        if (referenceMatch && dateMatch) {

            if (transactionAmount.compareTo(ledgerAmount) == 0) {

                return new MatchResult(
                        MatchResult.Status.MATCHED,
                        transaction.getTransactionId(),
                        ledgerEntry.getLedgerId(),
                        "Reference, amount and date-window matched",
                        0.95,
                        "TIER_2"
                );
            }

            if (feeRule.matches(
                    transactionAmount,
                    ledgerAmount)) {

                return new MatchResult(
                        MatchResult.Status.MATCHED,
                        transaction.getTransactionId(),
                        ledgerEntry.getLedgerId(),
                        "Amount difference explained by standard processing fee",
                        0.88,
                        "TIER_2"
                );
            }

            if (tdsRule.matches(
                    transactionAmount,
                    ledgerAmount)) {

                return new MatchResult(
                        MatchResult.Status.MATCHED,
                        transaction.getTransactionId(),
                        ledgerEntry.getLedgerId(),
                        "Amount difference explained by TDS deduction",
                        0.88,
                        "TIER_2"
                );
            }

            return new MatchResult(
                    MatchResult.Status.AMOUNT_MISMATCH,
                    transaction.getTransactionId(),
                    ledgerEntry.getLedgerId(),
                    "Reference and date match but amount difference is unexplained",
                    0.65,
                    "TIER_2"
            );
        }

        return new MatchResult(
                MatchResult.Status.UNMATCHED,
                transaction.getTransactionId(),
                null,
                "No rule-based match",
                0.0,
                "TIER_2"
        );
    }
}