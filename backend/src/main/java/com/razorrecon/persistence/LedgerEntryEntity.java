package com.razorrecon.persistence;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "ledger_entries")
public class LedgerEntryEntity {
    @Id @GeneratedValue(strategy = GenerationType.UUID) private UUID id;
    @Column(name = "ledger_id", nullable = false, length = 100) private String ledgerId;
    @Column(nullable = false, length = 100) private String reference;
    @Column(nullable = false, precision = 14, scale = 2) private BigDecimal amount;
    @Column(nullable = false) private LocalDateTime timestamp;
    @Column(name = "entry_type", nullable = false, length = 50) private String entryType;
    protected LedgerEntryEntity() {}
    public LedgerEntryEntity(String ledgerId, String reference, BigDecimal amount, LocalDateTime timestamp, String entryType) { this.ledgerId = ledgerId; this.reference = reference; this.amount = amount; this.timestamp = timestamp; this.entryType = entryType; }
    public UUID getId() { return id; }
    public String getLedgerId() { return ledgerId; }
}
