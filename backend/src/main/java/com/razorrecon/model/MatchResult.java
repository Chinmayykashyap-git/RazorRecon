package com.razorrecon.model;

public class MatchResult {

    public enum Status {
        MATCHED,
        UNMATCHED,
        AMOUNT_MISMATCH,
        AMBIGUOUS
    }

    private Status status;
    private String sourceId;
    private String matchedId;
    private String reason;
    private double confidence;
    private String tier;

    public MatchResult() {}

    public MatchResult(Status status,
                       String sourceId,
                       String matchedId,
                       String reason,
                       double confidence,
                       String tier) {
        this.status = status;
        this.sourceId = sourceId;
        this.matchedId = matchedId;
        this.reason = reason;
        this.confidence = confidence;
        this.tier = tier;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getMatchedId() {
        return matchedId;
    }

    public void setMatchedId(String matchedId) {
        this.matchedId = matchedId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public double getConfidence() {
        return confidence;
    }

    public void setConfidence(double confidence) {
        this.confidence = confidence;
    }

    public String getTier() {
        return tier;
    }

    public void setTier(String tier) {
        this.tier = tier;
    }
}