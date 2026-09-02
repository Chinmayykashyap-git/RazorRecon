package com.razorrecon.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Transfer {

    private String transferId;
    private String paymentId;
    private BigDecimal amount;
    private String status;
    private LocalDateTime createdAt;

    public Transfer() {}

    public Transfer(String transferId, String paymentId,
                    BigDecimal amount, String status,
                    LocalDateTime createdAt) {
        this.transferId = transferId;
        this.paymentId = paymentId;
        this.amount = amount;
        this.status = status;
        this.createdAt = createdAt;
    }

    public String getTransferId() {
        return transferId;
    }

    public void setTransferId(String transferId) {
        this.transferId = transferId;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}