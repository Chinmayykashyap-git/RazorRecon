package com.razorrecon.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.razorrecon.model.MatchResult;
import com.razorrecon.model.ReconciliationStats;

@Service
public class ReconciliationStatsService {

    public ReconciliationStats calculate(List<MatchResult> results) {
        if (results == null || results.isEmpty()) {
            return new ReconciliationStats(0, 0, 0, 0, 0, 0.0);
        }

        int matched = 0;
        int ambiguous = 0;
        int unmatched = 0;
        int llm = 0;

        for (MatchResult result : results) {
            if (result == null) {
                continue;
            }
            if (result.getStatus() == MatchResult.Status.MATCHED) {
                matched++;
            }
            if (result.getStatus() == MatchResult.Status.AMBIGUOUS) {
                ambiguous++;
            }
            if (result.getStatus() == MatchResult.Status.UNMATCHED) {
                unmatched++;
            }
            if ("TIER_3_LLM".equals(result.getTier())) {
                llm++;
            }
        }

        double automationRate = ((double) matched / results.size()) * 100;
        return new ReconciliationStats(
                results.size(), matched, ambiguous, unmatched, llm, automationRate
        );
    }
}
