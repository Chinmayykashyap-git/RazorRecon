package com.razorrecon.llm;

public class LLMResponse {

    private String decision;
    private double confidence;
    private String matchedId;
    private String reasoning;

    public LLMResponse() {
    }

    public LLMResponse(String decision, double confidence, String matchedId, String reasoning) {
        this.decision = decision;
        this.confidence = confidence;
        this.matchedId = matchedId;
        this.reasoning = reasoning;
    }

    public String getDecision() {
        return decision;
    }

    public void setDecision(String decision) {
        this.decision = decision;
    }

    public double getConfidence() {
        return confidence;
    }

    public void setConfidence(double confidence) {
        this.confidence = confidence;
    }

    public String getMatchedId() {
        return matchedId;
    }

    public void setMatchedId(String matchedId) {
        this.matchedId = matchedId;
    }

    public String getReasoning() {
        return reasoning;
    }

    public void setReasoning(String reasoning) {
        this.reasoning = reasoning;
    }
}
