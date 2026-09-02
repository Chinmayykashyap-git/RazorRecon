package com.razorrecon.model;

public class ExceptionCase {

    public enum ExceptionType {
        NO_COUNTERPART,
        AMBIGUOUS_MULTIPLE_CANDIDATES,
        AMOUNT_MISMATCH_UNEXPLAINED,
        DATE_MISMATCH,
        REFERENCE_MISMATCH
    }

    private String transactionId;
    private ExceptionType type;
    private String description;
    private String recommendedAction;

    public ExceptionCase() {}

    public ExceptionCase(String transactionId,
                          ExceptionType type,
                          String description,
                          String recommendedAction) {
        this.transactionId = transactionId;
        this.type = type;
        this.description = description;
        this.recommendedAction = recommendedAction;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public ExceptionType getType() {
        return type;
    }

    public void setType(ExceptionType type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRecommendedAction() {
        return recommendedAction;
    }

    public void setRecommendedAction(String recommendedAction) {
        this.recommendedAction = recommendedAction;
    }
}