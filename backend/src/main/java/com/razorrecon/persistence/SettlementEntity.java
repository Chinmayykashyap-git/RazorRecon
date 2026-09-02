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
@Table(name = "settlements")
public class SettlementEntity {
    @Id @GeneratedValue(strategy = GenerationType.UUID) private UUID id;
    @Column(name = "settlement_id", nullable = false, length = 100) private String settlementId;
    @Column(nullable = false, length = 100) private String reference;
    @Column(nullable = false, precision = 14, scale = 2) private BigDecimal amount;
    @Column(nullable = false) private LocalDateTime timestamp;
    @Column(nullable = false, length = 50) private String status;
    protected SettlementEntity() {}
    public SettlementEntity(String settlementId, String reference, BigDecimal amount, LocalDateTime timestamp, String status) { this.settlementId = settlementId; this.reference = reference; this.amount = amount; this.timestamp = timestamp; this.status = status; }
    public UUID getId() { return id; }
    public String getSettlementId() { return settlementId; }
    public String getReference() { return reference; }
}
