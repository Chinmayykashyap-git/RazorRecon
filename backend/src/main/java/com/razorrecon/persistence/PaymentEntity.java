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
@Table(name = "payments")
public class PaymentEntity {
    @Id @GeneratedValue(strategy = GenerationType.UUID) private UUID id;
    @Column(name = "transaction_id", nullable = false, length = 100) private String transactionId;
    @Column(name = "order_id", length = 100) private String orderId;
    @Column(nullable = false, precision = 14, scale = 2) private BigDecimal amount;
    @Column(nullable = false, length = 10) private String currency;
    @Column(nullable = false, length = 50) private String status;
    @Column(nullable = false) private LocalDateTime timestamp;
    @Column(length = 50) private String paymentMethod;
    @Column(name = "transaction_hash", unique = true, length = 128) private String transactionHash;
    @Column(nullable = false) private LocalDateTime createdAt = LocalDateTime.now();
    protected PaymentEntity() {}
    public PaymentEntity(String transactionId, String orderId, BigDecimal amount, String currency, String status, LocalDateTime timestamp, String transactionHash) { this.transactionId = transactionId; this.orderId = orderId; this.amount = amount; this.currency = currency; this.status = status; this.timestamp = timestamp; this.transactionHash = transactionHash; }
    public UUID getId() { return id; }
    public String getTransactionId() { return transactionId; }
    public String getTransactionHash() { return transactionHash; }
}
