package com.razorrecon.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class BankTransaction {

    private String transactionId;
    private String reference;
    private BigDecimal amount;
    private LocalDateTime transactionDate;
    private String description;

    public BankTransaction() {}

    public BankTransaction(String transactionId,
                           String reference,
                           BigDecimal amount,
                           LocalDateTime transactionDate,
                           String description) {
        this.transactionId = transactionId;
        this.reference = reference;
        this.amount = amount;
        this.transactionDate = transactionDate;
        this.description = description;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}