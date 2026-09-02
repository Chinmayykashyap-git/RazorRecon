package com.razorrecon.model;

public record SettlementCandidate(String settlementId, double amountDifference,
                                  long timestampDifferenceSeconds) {
}