package com.razorrecon.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "reconciliation_records")
public class ReconciliationRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sourceId;
    private String matchedId;
    private String status;
    private String reason;
    private double confidence;
    private String tier;
    private LocalDateTime createdAt;

    public ReconciliationRecord() {
    }

    public ReconciliationRecord(
            String sourceId,
            String matchedId,
            String status,
            String reason,
            double confidence,
            String tier) {
        this.sourceId = sourceId;
        this.matchedId = matchedId;
        this.status = status;
        this.reason = reason;
        this.confidence = confidence;
        this.tier = tier;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getSourceId() {
        return sourceId;
    }

    public String getMatchedId() {
        return matchedId;
    }

    public String getStatus() {
        return status;
    }

    public String getReason() {
        return reason;
    }

    public double getConfidence() {
        return confidence;
    }

    public String getTier() {
        return tier;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
