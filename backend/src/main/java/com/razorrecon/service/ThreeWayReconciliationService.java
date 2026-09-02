package com.razorrecon.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.razorrecon.model.LedgerEntry;
import com.razorrecon.model.Payment;
import com.razorrecon.model.Settlement;
import com.razorrecon.model.ThreeWayReconciliationResult;
import com.razorrecon.model.ThreeWayReconciliationResult.Status;

@Service
public class ThreeWayReconciliationService {

    public List<ThreeWayReconciliationResult> reconcile(
            List<Payment> payments,
            List<Settlement> settlements,
            List<LedgerEntry> ledgerEntries) {

        List<ThreeWayReconciliationResult> results = new ArrayList<>();
        Map<String, List<Settlement>> settlementsByReference = indexSettlements(settlements);
        Map<String, LedgerEntry> ledgerByReference = indexLedgerEntries(ledgerEntries);
        Set<String> matchedSettlementIds = new HashSet<>();
        Set<String> matchedLedgerIds = new HashSet<>();

        for (Payment payment : safe(payments)) {
            if (!valid(payment)) {
                results.add(invalidPayment(payment));
                continue;
            }

            List<Settlement> candidates = settlementsByReference.getOrDefault(
                        matchingKey(payment.getReference(), payment.getPaymentId()), List.of());
            if (candidates.isEmpty()) {
                results.add(result(payment, null, findLedger(payment, ledgerByReference),
                        Status.MISSING_SETTLEMENT, payment.getAmount(), null,
                        payment.getAmount(), 0.0, "No settlement matched the payment reference"));
                continue;
            }
            if (candidates.size() > 1) {
                results.add(result(payment, candidates.get(0), findLedger(payment, ledgerByReference),
                        Status.DUPLICATE, payment.getAmount(), candidates.get(0).getAmount(),
                        difference(payment.getAmount(), candidates.get(0).getAmount()), 0.0,
                        "Multiple settlements matched the same payment reference"));
                continue;
            }

            Settlement settlement = candidates.get(0);
            LedgerEntry ledger = findLedger(payment, ledgerByReference);
            matchedSettlementIds.add(settlement.getSettlementId());
            if (ledger != null) {
                matchedLedgerIds.add(ledger.getLedgerId());
            }

            BigDecimal difference = difference(payment.getAmount(), settlement.getAmount());
            Status status = difference.signum() == 0 ? Status.MATCHED
                    : settlement.getAmount().compareTo(payment.getAmount()) < 0
                    ? Status.PARTIAL_SETTLEMENT : Status.AMOUNT_MISMATCH;
            if (ledger == null && status == Status.MATCHED) {
                status = Status.MANUAL_REVIEW;
            }
            results.add(result(payment, settlement, ledger, status, payment.getAmount(),
                    settlement.getAmount(), difference, status == Status.MATCHED ? 1.0 : 0.98,
                    reason(status, ledger)));
        }

        for (Settlement settlement : safe(settlements)) {
            if (settlement != null && !matchedSettlementIds.contains(settlement.getSettlementId())
                    && valid(settlement)) {
                results.add(new ThreeWayReconciliationResult(null, settlement.getSettlementId(), null,
                        Status.MISSING_PAYMENT, null, settlement.getAmount(), settlement.getAmount(),
                        0.0, "Settlement has no matching payment"));
            }
        }
        for (LedgerEntry ledger : safe(ledgerEntries)) {
            if (ledger != null && !matchedLedgerIds.contains(ledger.getLedgerId()) && valid(ledger)) {
                results.add(new ThreeWayReconciliationResult(null, null, ledger.getLedgerId(),
                        Status.MISSING_PAYMENT, null, ledger.getAmount(), ledger.getAmount(),
                        0.0, "Ledger entry has no matching payment"));
            }
        }
        return results;
    }

    private Map<String, List<Settlement>> indexSettlements(List<Settlement> settlements) {
        Map<String, List<Settlement>> index = new HashMap<>();
        for (Settlement settlement : safe(settlements)) {
            if (settlement != null && valid(settlement)) {
                index.computeIfAbsent(matchingKey(settlement.getReference(), settlement.getSettlementId()),
                        ignored -> new ArrayList<>()).add(settlement);
            }
        }
        return index;
    }

    private Map<String, LedgerEntry> indexLedgerEntries(List<LedgerEntry> ledgerEntries) {
        Map<String, LedgerEntry> index = new HashMap<>();
        for (LedgerEntry ledger : safe(ledgerEntries)) {
            if (ledger != null && valid(ledger)) {
                index.putIfAbsent(matchingKey(ledger.getReference(), ledger.getLedgerId()), ledger);
            }
        }
        return index;
    }

    private LedgerEntry findLedger(Payment payment, Map<String, LedgerEntry> ledgerByReference) {
        LedgerEntry byReference = ledgerByReference.get(matchingKey(payment.getReference(), payment.getPaymentId()));
        if (byReference != null) {
            return byReference;
        }
        return ledgerByReference.get(payment.getPaymentId());
    }

    private String matchingKey(String reference, String id) {
        return reference != null ? reference : id;
    }

    private boolean valid(Payment payment) {
        return payment != null && payment.getPaymentId() != null && payment.getAmount() != null
                && payment.getCurrency() != null && payment.getCreatedAt() != null;
    }

    private boolean valid(Settlement settlement) {
        return settlement != null && (settlement.getSettlementId() != null || settlement.getReference() != null)
                && settlement.getAmount() != null && settlement.getSettlementDate() != null;
    }

    private boolean valid(LedgerEntry ledger) {
        return ledger != null && (ledger.getLedgerId() != null || ledger.getReference() != null)
                && ledger.getAmount() != null && ledger.getEntryDate() != null;
    }

    private ThreeWayReconciliationResult invalidPayment(Payment payment) {
        return new ThreeWayReconciliationResult(payment == null ? null : payment.getPaymentId(), null, null,
                Status.INVALID_RECORD, null, null, null, 0.0,
                "Payment is missing a required identifier, amount, currency, or timestamp");
    }

    private ThreeWayReconciliationResult result(Payment payment, Settlement settlement, LedgerEntry ledger,
            Status status, BigDecimal expected, BigDecimal actual, BigDecimal difference,
            double confidence, String reason) {
        return new ThreeWayReconciliationResult(payment.getPaymentId(),
                settlement == null ? null : settlement.getSettlementId(),
                ledger == null ? null : ledger.getLedgerId(), status, expected, actual, difference,
                confidence, reason);
    }

    private BigDecimal difference(BigDecimal expected, BigDecimal actual) {
        return expected.subtract(actual);
    }

    private String reason(Status status, LedgerEntry ledger) {
        if (status == Status.MATCHED && ledger != null) {
            return "Exact payment, settlement, and ledger amount match";
        }
        if (status == Status.MANUAL_REVIEW) {
            return "Payment and settlement match, but no ledger entry was found";
        }
        return "Settlement amount differs from payment amount";
    }

    private <T> List<T> safe(List<T> values) {
        return values == null ? List.of() : values;
    }
}
