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
@Table(name = "audit_logs")
public class AuditLogEntity {
    @Id @GeneratedValue(strategy = GenerationType.UUID) private UUID id;
    @Column(name = "reconciliation_id", nullable = false) private UUID reconciliationId;
    @Column(name = "transaction_id", length = 100) private String transactionId;
    @Column(nullable = false, length = 100) private String action;
    @Column(nullable = false, length = 50) private String status;
    @Column(nullable = false, length = 500) private String reason;
    @Column(nullable = false) private LocalDateTime createdAt = LocalDateTime.now();
    protected AuditLogEntity() {}
    public AuditLogEntity(UUID reconciliationId, String transactionId, String action, String status, String reason) { this.reconciliationId = reconciliationId; this.transactionId = transactionId; this.action = action; this.status = status; this.reason = reason; }
    public UUID getId() { return id; }
    public UUID getReconciliationId() { return reconciliationId; }
    public String getTransactionId() { return transactionId; }
    public String getAction() { return action; }
    public String getStatus() { return status; }
    public String getReason() { return reason; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
