package com.razorrecon.persistence;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.*;

@Entity
@Table(name = "reconciliation_requests")
public class ReconciliationRequestEntity {
    @Id @GeneratedValue(strategy = GenerationType.UUID) private UUID id;
    @Column(name = "request_hash", nullable = false, unique = true, length = 128) private String requestHash;
    @Column(nullable = false, length = 50) private String status;
    @Column(nullable = false) private LocalDateTime createdAt = LocalDateTime.now();
    protected ReconciliationRequestEntity() {}
    public ReconciliationRequestEntity(String requestHash, String status) { this.requestHash = requestHash; this.status = status; }
    public UUID getId() { return id; }
}