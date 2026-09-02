package com.razorrecon.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Refund {

    private String refundId;
    private String paymentId;
    private BigDecimal amount;
    private LocalDateTime createdAt;
    private String status;

    public Refund() {}

    public Refund(String refundId, String paymentId,
                  BigDecimal amount, LocalDateTime createdAt,
                  String status) {
        this.refundId = refundId;
        this.paymentId = paymentId;
        this.amount = amount;
        this.createdAt = createdAt;
        this.status = status;
    }

    public String getRefundId() {
        return refundId;
    }

    public void setRefundId(String refundId) {
        this.refundId = refundId;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}