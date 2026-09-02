package com.razorrecon.engine;

import java.util.List;

import org.springframework.stereotype.Component;

import com.razorrecon.model.BankTransaction;
import com.razorrecon.model.LedgerEntry;
import com.razorrecon.model.MatchResult;

@Component
public class DecisionEngine {

    private static final double AUTO_MATCH_THRESHOLD = 90.0;
    private static final double AMBIGUOUS_THRESHOLD = 70.0;

    public MatchResult decide(
            BankTransaction transaction,
            List<LedgerEntry> candidates,
            List<Double> scores) {

        if (transaction == null || candidates == null
                || candidates.isEmpty()) {

            return new MatchResult(
                    MatchResult.Status.UNMATCHED,
                    transaction != null
                            ? transaction.getTransactionId()
                            : null,
                    null,
                    "No candidate ledger entry found",
                    0.0,
                    "DECISION"
            );
        }

        int bestIndex = 0;
        double bestScore = scores.get(0);

        for (int i = 1; i < scores.size(); i++) {

            if (scores.get(i) > bestScore) {
                bestScore = scores.get(i);
                bestIndex = i;
            }
        }

        LedgerEntry bestCandidate =
                candidates.get(bestIndex);

        /*
         * Check for competing candidates with almost
         * identical scores.
         */
        boolean ambiguous = false;

        for (int i = 0; i < scores.size(); i++) {

            if (i == bestIndex) {
                continue;
            }

            if (Math.abs(bestScore - scores.get(i)) <= 5
                    && scores.get(i) >= AMBIGUOUS_THRESHOLD) {

                ambiguous = true;
                break;
            }
        }

        if (ambiguous) {

            return new MatchResult(
                    MatchResult.Status.AMBIGUOUS,
                    transaction.getTransactionId(),
                    null,
                    "Multiple candidates have similar confidence scores",
                    bestScore / 100.0,
                    "DECISION"
            );
        }

        if (bestScore >= AUTO_MATCH_THRESHOLD) {

            return new MatchResult(
                    MatchResult.Status.MATCHED,
                    transaction.getTransactionId(),
                    bestCandidate.getLedgerId(),
                    "High-confidence deterministic match",
                    bestScore / 100.0,
                    "DECISION"
            );
        }

        if (bestScore >= AMBIGUOUS_THRESHOLD) {

            return new MatchResult(
                    MatchResult.Status.AMBIGUOUS,
                    transaction.getTransactionId(),
                    bestCandidate.getLedgerId(),
                    "Candidate found but confidence requires further investigation",
                    bestScore / 100.0,
                    "DECISION"
            );
        }

        return new MatchResult(
                MatchResult.Status.UNMATCHED,
                transaction.getTransactionId(),
                null,
                "No candidate exceeded the matching threshold",
                bestScore / 100.0,
                "DECISION"
        );
    }
}