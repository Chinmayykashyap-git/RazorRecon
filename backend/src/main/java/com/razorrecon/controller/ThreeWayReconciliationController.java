package com.razorrecon.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.razorrecon.model.LedgerEntry;
import com.razorrecon.model.Payment;
import com.razorrecon.model.Settlement;
import com.razorrecon.persistence.AuditLogEntity;
import com.razorrecon.service.PersistentReconciliationService;

@RestController
@RequestMapping("/api/v1/reconcile")
@CrossOrigin(origins = "*")
public class ThreeWayReconciliationController {

    private final PersistentReconciliationService service;

    public ThreeWayReconciliationController(PersistentReconciliationService service) {
        this.service = service;
    }

    @PostMapping
    public PersistentReconciliationService.ReconciliationResponse reconcile(@RequestBody ReconciliationRequest request) {
        return service.reconcile(request.payments(), request.settlements(), request.ledger());
    }

    @GetMapping
    public List<com.razorrecon.persistence.ReconciliationResultEntity> results() {
        return service.findResults();
    }

    @GetMapping("/exceptions")
    public List<com.razorrecon.persistence.ExceptionEntity> exceptions() {
        return service.findExceptions();
    }

    @GetMapping("/{reconciliationId}/audit")
    public List<AuditLogEntity> audit(@org.springframework.web.bind.annotation.PathVariable UUID reconciliationId) {
        return service.findAudit(reconciliationId);
    }

    public record ReconciliationRequest(
            List<Payment> payments,
            List<Settlement> settlements,
            List<LedgerEntry> ledger) {}
}
