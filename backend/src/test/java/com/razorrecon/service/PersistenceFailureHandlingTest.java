package com.razorrecon.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.transaction.annotation.Transactional;

import com.razorrecon.ingestion.CsvImportResult;
import com.razorrecon.ingestion.CsvImporter;
import com.razorrecon.model.Payment;
import com.razorrecon.persistence.AuditLogEntity;
import com.razorrecon.persistence.AuditLogRepository;
import com.razorrecon.persistence.ExceptionPersistenceRepository;
import com.razorrecon.persistence.LedgerEntryPersistenceRepository;
import com.razorrecon.persistence.PaymentEntity;
import com.razorrecon.persistence.PaymentPersistenceRepository;
import com.razorrecon.persistence.ReconciliationRequestEntity;
import com.razorrecon.persistence.ReconciliationRequestRepository;
import com.razorrecon.persistence.ReconciliationResultEntity;
import com.razorrecon.persistence.ReconciliationResultRepository;
import com.razorrecon.persistence.SettlementPersistenceRepository;

class PersistenceFailureHandlingTest {

    private final LocalDateTime timestamp = LocalDateTime.of(2026, 8, 21, 10, 0);

    @Test
    void duplicateRequestIsIdempotent() {
        ReconciliationRequestRepository requests = mock(ReconciliationRequestRepository.class);
        ReconciliationRequestEntity existing = mock(ReconciliationRequestEntity.class);
        UUID id = UUID.randomUUID();
        when(existing.getId()).thenReturn(id);
        when(requests.findByRequestHash(anyString())).thenReturn(Optional.of(existing));
        PersistentReconciliationService service = service(requests);

        PersistentReconciliationService.ReconciliationResponse response = service.reconcile(
                List.of(payment("TXN-1", "REF-1", "100.00")), List.of(), List.of());

        assertEquals("ALREADY_PROCESSED", response.status());
        assertEquals(id, response.reconciliationId());
    }

    @Test
    void malformedRecordDoesNotBreakValidRecords() throws Exception {
        java.nio.file.Path file = java.nio.file.Files.createTempFile("razor-recon", ".csv");
        java.nio.file.Files.writeString(file, "id,amount\nvalid,100\ninvalid,\n");
        CsvImportResult result = new CsvImporter().readValidated(file, 2);

        assertEquals(1, result.validRecords().size());
        assertEquals(1, result.invalidRecords().size());
        java.nio.file.Files.deleteIfExists(file);
    }

    @Test
    void missingSettlementCreatesExceptionDecision() {
        var results = new ThreeWayReconciliationService().reconcile(
                List.of(payment("TXN-1", "REF-1", "100.00")), List.of(), List.of());

        assertEquals("MISSING_SETTLEMENT", results.get(0).status().name());
    }

    @Test
    void transactionRollsBackOnFailureBoundary() throws Exception {
        assertTrue(PersistentReconciliationService.class.getMethod("reconcile", List.class, List.class, List.class)
                .isAnnotationPresent(Transactional.class));
    }

    @Test
    void auditEventsArePersisted() {
        ReconciliationRequestRepository requests = mock(ReconciliationRequestRepository.class);
        ReconciliationRequestEntity request = mock(ReconciliationRequestEntity.class);
        when(request.getId()).thenReturn(UUID.randomUUID());
        when(requests.findByRequestHash(anyString())).thenReturn(Optional.empty());
        when(requests.save(any())).thenReturn(request);
        PaymentPersistenceRepository payments = mock(PaymentPersistenceRepository.class);
        PaymentEntity payment = mock(PaymentEntity.class);
        when(payment.getId()).thenReturn(UUID.randomUUID());
        when(payments.findByTransactionHash(anyString())).thenReturn(Optional.empty());
        when(payments.save(any())).thenReturn(payment);
        SettlementPersistenceRepository settlements = mock(SettlementPersistenceRepository.class);
        LedgerEntryPersistenceRepository ledger = mock(LedgerEntryPersistenceRepository.class);
        ReconciliationResultRepository results = mock(ReconciliationResultRepository.class);
        when(results.save(any())).thenReturn(mock(ReconciliationResultEntity.class));
        AuditLogRepository audits = mock(AuditLogRepository.class);
        PersistentReconciliationService service = new PersistentReconciliationService(
                new ThreeWayReconciliationService(), payments, settlements, ledger, results,
                mock(ExceptionPersistenceRepository.class), audits, requests);

        service.reconcile(List.of(payment("TXN-1", "REF-1", "100.00")), List.of(), List.of());

        verify(audits, atLeast(5)).save(any(AuditLogEntity.class));
    }

    private PersistentReconciliationService service(ReconciliationRequestRepository requests) {
        return new PersistentReconciliationService(new ThreeWayReconciliationService(),
                mock(PaymentPersistenceRepository.class), mock(SettlementPersistenceRepository.class),
                mock(LedgerEntryPersistenceRepository.class), mock(ReconciliationResultRepository.class),
                mock(ExceptionPersistenceRepository.class), mock(AuditLogRepository.class), requests);
    }

    private Payment payment(String id, String reference, String amount) {
        return new Payment(id, "ORDER-1", new BigDecimal(amount), "INR", "SUCCESS", timestamp, reference);
    }
}
