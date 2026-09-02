package com.razorrecon.engine;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.razorrecon.model.BankTransaction;
import com.razorrecon.model.LedgerEntry;
import com.razorrecon.rules.DateWindowRule;
import com.razorrecon.rules.ReferenceSimilarityRule;

@Component
public class CandidateGenerator {

    private final ReferenceSimilarityRule referenceSimilarityRule;
    private final DateWindowRule dateWindowRule;

    public CandidateGenerator(
            ReferenceSimilarityRule referenceSimilarityRule,
            DateWindowRule dateWindowRule) {

        this.referenceSimilarityRule = referenceSimilarityRule;
        this.dateWindowRule = dateWindowRule;
    }

    public List<LedgerEntry> generateCandidates(
            BankTransaction transaction,
            List<LedgerEntry> ledgerEntries) {

        List<LedgerEntry> candidates = new ArrayList<>();

        if (transaction == null || ledgerEntries == null) {
            return candidates;
        }

        for (LedgerEntry entry : ledgerEntries) {

            boolean referenceMatch =
                    referenceSimilarityRule.matches(
                            transaction.getReference(),
                            entry.getReference()
                    );

            boolean dateMatch =
                    dateWindowRule.matches(
                            transaction.getTransactionDate(),
                            entry.getEntryDate()
                    );

            boolean amountMatch =
                    transaction.getAmount() != null
                            && entry.getAmount() != null
                            && transaction.getAmount()
                            .compareTo(entry.getAmount()) == 0;

            /*
             * A candidate doesn't need to satisfy every condition.
             *
             * Candidate generation is deliberately broader than
             * final matching.
             */

            if (referenceMatch || dateMatch || amountMatch) {
                candidates.add(entry);
            }
        }

        return candidates;
    }
}