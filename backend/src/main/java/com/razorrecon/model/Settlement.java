package com.razorrecon.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Settlement {

    private String settlementId;
    private String reference;
    private BigDecimal amount;
    private LocalDateTime settlementDate;
    private String status;
    private String merchantId;
    private String currency;
    private String paymentMethod;

    public Settlement() {}

    public Settlement(String settlementId, String reference,
                      BigDecimal amount,
                      LocalDateTime settlementDate,
                      String status) {
        this.settlementId = settlementId;
        this.reference = reference;
        this.amount = amount;
        this.settlementDate = settlementDate;
        this.status = status;
    }

    public Settlement(String settlementId, String reference, String merchantId,
                      BigDecimal amount, String currency, LocalDateTime settlementDate,
                      String status) {
        this(settlementId, reference, amount, settlementDate, status);
        this.merchantId = merchantId;
        this.currency = currency;
    }

    public String getSettlementId() {
        return settlementId;
    }

    public void setSettlementId(String settlementId) {
        this.settlementId = settlementId;
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

    public LocalDateTime getSettlementDate() {
        return settlementDate;
    }

    public void setSettlementDate(LocalDateTime settlementDate) {
        this.settlementDate = settlementDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}