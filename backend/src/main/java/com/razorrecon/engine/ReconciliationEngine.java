package com.razorrecon.engine;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.razorrecon.model.BankTransaction;
import com.razorrecon.model.LedgerEntry;
import com.razorrecon.model.MatchResult;
import com.razorrecon.llm.LLMMatcher;
import com.razorrecon.llm.LLMResponse;

@Component
public class ReconciliationEngine {

    private final CandidateGenerator candidateGenerator;
    private final ScoringEngine scoringEngine;
    private final DecisionEngine decisionEngine;
        private final LLMMatcher llmMatcher;

    public ReconciliationEngine(
            CandidateGenerator candidateGenerator,
            ScoringEngine scoringEngine,
            DecisionEngine decisionEngine,
            LLMMatcher llmMatcher) {

        this.candidateGenerator = candidateGenerator;
        this.scoringEngine = scoringEngine;
        this.decisionEngine = decisionEngine;
        this.llmMatcher = llmMatcher;
    }

    public MatchResult reconcile(
            BankTransaction transaction,
            List<LedgerEntry> ledgerEntries) {

        /*
         * STEP 1
         * Generate a smaller candidate set.
         */
        List<LedgerEntry> candidates =
                candidateGenerator.generateCandidates(
                        transaction,
                        ledgerEntries
                );

        /*
         * STEP 2
         * Score every candidate.
         */
        List<Double> scores = new ArrayList<>();

        for (LedgerEntry candidate : candidates) {

            double score =
                    scoringEngine.calculateScore(
                            transaction,
                            candidate
                    );

            scores.add(score);
        }

        /*
         * STEP 3
         * Make the deterministic decision.
         */
        MatchResult decision = decisionEngine.decide(
                transaction,
                candidates,
                scores
        );

        if (decision.getStatus() == MatchResult.Status.AMBIGUOUS) {
            LLMResponse response = llmMatcher.investigate(transaction, candidates);
            if ("MATCH".equalsIgnoreCase(response.getDecision())
                    && response.getMatchedId() != null) {
                return new MatchResult(
                        MatchResult.Status.MATCHED,
                        transaction.getTransactionId(),
                        response.getMatchedId(),
                        response.getReasoning(),
                        response.getConfidence(),
                        "TIER_3_LLM"
                );
            }
        }

        return decision;
    }

    public List<MatchResult> reconcileAll(
            List<BankTransaction> transactions,
            List<LedgerEntry> ledgerEntries) {

        List<MatchResult> results =
                new ArrayList<>();

        if (transactions == null) {
            return results;
        }

        for (BankTransaction transaction : transactions) {

            results.add(
                    reconcile(
                            transaction,
                            ledgerEntries
                    )
            );
        }

        return results;
    }
}