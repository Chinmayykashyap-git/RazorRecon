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
@Table(name = "reconciliation_results")
public class ReconciliationResultEntity {
    @Id @GeneratedValue(strategy = GenerationType.UUID) private UUID id;
    @Column(name = "reconciliation_id", nullable = false) private UUID reconciliationId;
    @Column(name = "payment_id") private UUID paymentId;
    @Column(name = "settlement_id") private UUID settlementId;
    @Column(name = "ledger_id") private UUID ledgerId;
    @Column(nullable = false, length = 50) private String status;
    @Column(name = "expected_amount", precision = 14, scale = 2) private BigDecimal expectedAmount;
    @Column(name = "actual_amount", precision = 14, scale = 2) private BigDecimal actualAmount;
    @Column(precision = 14, scale = 2) private BigDecimal difference;
    @Column(nullable = false) private double confidence;
    @Column(nullable = false, length = 500) private String reason;
    @Column(nullable = false) private LocalDateTime createdAt = LocalDateTime.now();
    protected ReconciliationResultEntity() {}
    public ReconciliationResultEntity(UUID reconciliationId, UUID paymentId, UUID settlementId, UUID ledgerId, String status, BigDecimal expectedAmount, BigDecimal actualAmount, BigDecimal difference, double confidence, String reason) { this.reconciliationId = reconciliationId; this.paymentId = paymentId; this.settlementId = settlementId; this.ledgerId = ledgerId; this.status = status; this.expectedAmount = expectedAmount; this.actualAmount = actualAmount; this.difference = difference; this.confidence = confidence; this.reason = reason; }
    public UUID getId() { return id; }
    public UUID getReconciliationId() { return reconciliationId; }
    public String getStatus() { return status; }
    public String getReason() { return reason; }
    public UUID getPaymentId() { return paymentId; }
    public UUID getSettlementId() { return settlementId; }
    public UUID getLedgerId() { return ledgerId; }
    public BigDecimal getExpectedAmount() { return expectedAmount; }
    public BigDecimal getActualAmount() { return actualAmount; }
    public BigDecimal getDifference() { return difference; }
    public double getConfidence() { return confidence; }
}
