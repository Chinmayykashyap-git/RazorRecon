package com.razorrecon.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.razorrecon.model.LedgerEntry;
import com.razorrecon.model.Payment;
import com.razorrecon.model.Settlement;
import com.razorrecon.model.ThreeWayReconciliationResult;
import com.razorrecon.persistence.AuditLogEntity;
import com.razorrecon.persistence.AuditLogRepository;
import com.razorrecon.persistence.ExceptionEntity;
import com.razorrecon.persistence.ExceptionPersistenceRepository;
import com.razorrecon.persistence.LedgerEntryEntity;
import com.razorrecon.persistence.LedgerEntryPersistenceRepository;
import com.razorrecon.persistence.PaymentEntity;
import com.razorrecon.persistence.PaymentPersistenceRepository;
import com.razorrecon.persistence.ReconciliationRequestEntity;
import com.razorrecon.persistence.ReconciliationRequestRepository;
import com.razorrecon.persistence.ReconciliationResultEntity;
import com.razorrecon.persistence.ReconciliationResultRepository;
import com.razorrecon.persistence.SettlementEntity;
import com.razorrecon.persistence.SettlementPersistenceRepository;

@Service
public class PersistentReconciliationService {

    private final ThreeWayReconciliationService reconciliationService;
    private final PaymentPersistenceRepository payments;
    private final SettlementPersistenceRepository settlements;
    private final LedgerEntryPersistenceRepository ledgerEntries;
    private final ReconciliationResultRepository results;
    private final ExceptionPersistenceRepository exceptions;
    private final AuditLogRepository auditLogs;
    private final ReconciliationRequestRepository requests;

    public PersistentReconciliationService(ThreeWayReconciliationService reconciliationService,
            PaymentPersistenceRepository payments, SettlementPersistenceRepository settlements,
            LedgerEntryPersistenceRepository ledgerEntries, ReconciliationResultRepository results,
            ExceptionPersistenceRepository exceptions, AuditLogRepository auditLogs,
            ReconciliationRequestRepository requests) {
        this.reconciliationService = reconciliationService;
        this.payments = payments;
        this.settlements = settlements;
        this.ledgerEntries = ledgerEntries;
        this.results = results;
        this.exceptions = exceptions;
        this.auditLogs = auditLogs;
        this.requests = requests;
    }

    @Transactional
    public ReconciliationResponse reconcile(List<Payment> paymentRecords,
            List<Settlement> settlementRecords, List<LedgerEntry> ledgerRecords) {
        String requestHash = requestHash(paymentRecords, settlementRecords, ledgerRecords);
        var existing = requests.findByRequestHash(requestHash);
        if (existing.isPresent()) {
            UUID reconciliationId = existing.get().getId();
            return new ReconciliationResponse(reconciliationId, "ALREADY_PROCESSED",
                    decisionsFor(reconciliationId));
        }
        UUID reconciliationId = requests.save(new ReconciliationRequestEntity(requestHash, "PROCESSING")).getId();
        audit(reconciliationId, null, "INGESTION_STARTED", "STARTED", "Reconciliation request accepted");
        Map<String, PaymentEntity> paymentEntities = persistPayments(paymentRecords);
        audit(reconciliationId, null, "PAYMENT_PERSISTED", "SUCCESS", "Payment records persisted idempotently");
        Map<String, SettlementEntity> settlementEntities = persistSettlements(settlementRecords);
        audit(reconciliationId, null, "SETTLEMENT_PERSISTED", "SUCCESS", "Settlement records persisted");
        Map<String, LedgerEntryEntity> ledgerEntities = persistLedgerEntries(ledgerRecords);
        audit(reconciliationId, null, "LEDGER_PERSISTED", "SUCCESS", "Ledger records persisted");
        audit(reconciliationId, null, "RECONCILIATION_STARTED", "STARTED", "Deterministic reconciliation started");
        List<ThreeWayReconciliationResult> decisions = reconciliationService.reconcile(
                paymentRecords, settlementRecords, ledgerRecords);

        for (ThreeWayReconciliationResult decision : decisions) {
            results.save(new ReconciliationResultEntity(
                    reconciliationId,
                    idOf(paymentEntities, decision.paymentId()),
                    idOfSettlement(settlementEntities, decision.settlementId()),
                    idOfLedger(ledgerEntities, decision.ledgerId()),
                    decision.status().name(), decision.expectedAmount(), decision.actualAmount(),
                    decision.difference(), decision.confidence(), decision.reason()));
            audit(reconciliationId, decision.paymentId(), "MATCH_ATTEMPT", decision.status().name(), decision.reason());
            if (decision.status() != ThreeWayReconciliationResult.Status.MATCHED) {
                exceptions.save(new ExceptionEntity(reconciliationId, decision.paymentId(),
                        decision.status().name(), decision.reason()));
                audit(reconciliationId, decision.paymentId(), "EXCEPTION_CREATED", decision.status().name(), decision.reason());
            }
        }
        audit(reconciliationId, null, "RECONCILIATION_COMPLETED", "SUCCESS", "Reconciliation completed");
        return new ReconciliationResponse(reconciliationId, "CREATED", decisions);
    }

    public List<ReconciliationResultEntity> findResults() {
        return results.findAll();
    }

    public List<ExceptionEntity> findExceptions() {
        return exceptions.findAllByOrderByCreatedAtDesc();
    }

    public List<AuditLogEntity> findAudit(UUID reconciliationId) {
        return auditLogs.findByReconciliationIdOrderByCreatedAtAsc(reconciliationId);
    }

    private List<ThreeWayReconciliationResult> decisionsFor(UUID reconciliationId) {
        return results.findByReconciliationId(reconciliationId).stream()
            .map(result -> new ThreeWayReconciliationResult(
                result.getPaymentId() == null ? null : result.getPaymentId().toString(),
                result.getSettlementId() == null ? null : result.getSettlementId().toString(),
                result.getLedgerId() == null ? null : result.getLedgerId().toString(),
                ThreeWayReconciliationResult.Status.valueOf(result.getStatus()),
                result.getExpectedAmount(), result.getActualAmount(), result.getDifference(),
                result.getConfidence(), result.getReason()))
            .toList();
    }

    private void audit(UUID reconciliationId, String transactionId, String action, String status, String reason) {
        auditLogs.save(new AuditLogEntity(reconciliationId, transactionId, action, status, reason));
    }

    private String requestHash(List<Payment> payments, List<Settlement> settlements, List<LedgerEntry> ledger) {
        StringBuilder canonical = new StringBuilder();
        for (Payment payment : payments == null ? List.<Payment>of() : payments) {
            canonical.append("P|").append(payment == null ? "null" : String.join("|",
                    String.valueOf(payment.getPaymentId()), String.valueOf(payment.getReference()),
                    String.valueOf(payment.getAmount()), String.valueOf(payment.getCurrency()),
                    String.valueOf(payment.getCreatedAt()))).append('\n');
        }
        for (Settlement settlement : settlements == null ? List.<Settlement>of() : settlements) {
            canonical.append("S|").append(settlement == null ? "null" : String.join("|",
                    String.valueOf(settlement.getSettlementId()), String.valueOf(settlement.getReference()),
                    String.valueOf(settlement.getAmount()), String.valueOf(settlement.getSettlementDate()))).append('\n');
        }
        for (LedgerEntry entry : ledger == null ? List.<LedgerEntry>of() : ledger) {
            canonical.append("L|").append(entry == null ? "null" : String.join("|",
                    String.valueOf(entry.getLedgerId()), String.valueOf(entry.getReference()),
                    String.valueOf(entry.getAmount()), String.valueOf(entry.getEntryDate()))).append('\n');
        }
        return hash(canonical.toString());
    }

    public record ReconciliationResponse(UUID reconciliationId, String status,
            List<ThreeWayReconciliationResult> results) {}

    private Map<String, PaymentEntity> persistPayments(List<Payment> records) {
        Map<String, PaymentEntity> entities = new HashMap<>();
        if (records == null) return entities;
        for (Payment payment : records) {
            if (payment == null || payment.getPaymentId() == null || payment.getAmount() == null
                    || payment.getCreatedAt() == null) continue;
            String hash = hash(payment.getPaymentId(), payment.getAmount().toPlainString(), payment.getCreatedAt().toString());
            PaymentEntity entity = payments.findByTransactionHash(hash).orElseGet(() -> payments.save(
                    new PaymentEntity(payment.getPaymentId(), payment.getOrderId(), payment.getAmount(),
                            payment.getCurrency(), payment.getStatus(), payment.getCreatedAt(), hash)));
            entities.put(payment.getPaymentId(), entity);
        }
        return entities;
    }

    private Map<String, SettlementEntity> persistSettlements(List<Settlement> records) {
        Map<String, SettlementEntity> entities = new HashMap<>();
        if (records == null) return entities;
        for (Settlement settlement : records) {
            if (settlement == null || settlement.getSettlementId() == null || settlement.getAmount() == null
                    || settlement.getSettlementDate() == null) continue;
            SettlementEntity entity = settlements.save(new SettlementEntity(settlement.getSettlementId(),
                    settlement.getReference(), settlement.getAmount(), settlement.getSettlementDate(), settlement.getStatus()));
            entities.put(settlement.getSettlementId(), entity);
        }
        return entities;
    }

    private Map<String, LedgerEntryEntity> persistLedgerEntries(List<LedgerEntry> records) {
        Map<String, LedgerEntryEntity> entities = new HashMap<>();
        if (records == null) return entities;
        for (LedgerEntry ledger : records) {
            if (ledger == null || ledger.getLedgerId() == null || ledger.getAmount() == null
                    || ledger.getEntryDate() == null) continue;
            LedgerEntryEntity entity = ledgerEntries.save(new LedgerEntryEntity(ledger.getLedgerId(),
                    ledger.getReference(), ledger.getAmount(), ledger.getEntryDate(), ledger.getType()));
            entities.put(ledger.getLedgerId(), entity);
        }
        return entities;
    }

    private UUID idOf(Map<String, PaymentEntity> entities, String key) {
        return key == null || entities.get(key) == null ? null : entities.get(key).getId();
    }

    private UUID idOfSettlement(Map<String, SettlementEntity> entities, String key) {
        return key == null || entities.get(key) == null ? null : entities.get(key).getId();
    }

    private UUID idOfLedger(Map<String, LedgerEntryEntity> entities, String key) {
        return key == null || entities.get(key) == null ? null : entities.get(key).getId();
    }

    private String hash(String... values) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] bytes = digest.digest(String.join("|", values).getBytes(StandardCharsets.UTF_8));
            StringBuilder output = new StringBuilder();
            for (byte value : bytes) output.append(String.format("%02x", value));
            return output.toString();
        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException("SHA-256 is unavailable", exception);
        }
    }
}
