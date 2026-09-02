package com.razorrecon.model;

public record ReconciliationStats(
        int total,
        int matched,
        int ambiguous,
        int unmatched,
        int llmInvestigations,
        double automationRate) {
}
