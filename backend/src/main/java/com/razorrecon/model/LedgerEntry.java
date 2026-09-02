package com.razorrecon.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class LedgerEntry {

    private String ledgerId;
    private String reference;
    private BigDecimal amount;
    private LocalDateTime entryDate;
    private String type;

    public LedgerEntry() {}

    public LedgerEntry(String ledgerId,
                       String reference,
                       BigDecimal amount,
                       LocalDateTime entryDate,
                       String type) {
        this.ledgerId = ledgerId;
        this.reference = reference;
        this.amount = amount;
        this.entryDate = entryDate;
        this.type = type;
    }

    public String getLedgerId() {
        return ledgerId;
    }

    public void setLedgerId(String ledgerId) {
        this.ledgerId = ledgerId;
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

    public LocalDateTime getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(LocalDateTime entryDate) {
        this.entryDate = entryDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}