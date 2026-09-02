package com.razorrecon.persistence;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "exceptions")
public class ExceptionEntity {
    @Id @GeneratedValue(strategy = GenerationType.UUID) private UUID id;
    @Column(name = "reconciliation_id", nullable = false) private UUID reconciliationId;
    @Column(name = "transaction_id", length = 100) private String transactionId;
    @Column(nullable = false, length = 50) private String type;
    @Column(nullable = false, length = 500) private String reason;
    @Column(nullable = false) private LocalDateTime createdAt = LocalDateTime.now();
    protected ExceptionEntity() {}
    public ExceptionEntity(UUID reconciliationId, String transactionId, String type, String reason) { this.reconciliationId = reconciliationId; this.transactionId = transactionId; this.type = type; this.reason = reason; }
    public UUID getId() { return id; }
    public UUID getReconciliationId() { return reconciliationId; }
    public String getTransactionId() { return transactionId; }
    public String getType() { return type; }
    public String getReason() { return reason; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
