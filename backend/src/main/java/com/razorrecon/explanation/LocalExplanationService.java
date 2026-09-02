package com.razorrecon.explanation;

import java.util.List;

import org.springframework.stereotype.Service;

import com.razorrecon.model.ThreeWayReconciliationResult;

@Service
public class LocalExplanationService implements ExplanationService {

    @Override
    public Explanation explain(ThreeWayReconciliationResult result) {
        if (result == null) {
            return new Explanation("No reconciliation evidence was supplied.", "Unknown",
                    List.of(), "Review the source records.", "FALLBACK");
        }
        return switch (result.status()) {
            case PARTIAL_SETTLEMENT -> new Explanation(
                    "Settlement is lower than the payment by " + result.difference().abs() + ".",
                    "Possible fee adjustment or partial settlement.",
                    List.of("Payment and settlement records were found", "Settlement amount differs"),
                    "Verify the corresponding settlement fee record.", "LOCAL_FALLBACK");
            case AMOUNT_MISMATCH -> new Explanation(
                    "Payment and settlement amounts differ by " + result.difference().abs() + ".",
                    "Amount mismatch requires investigation.",
                    List.of("Both records were found", "Amounts differ"),
                    "Compare gateway and settlement reports.", "LOCAL_FALLBACK");
            default -> new Explanation("Deterministic status: " + result.status(),
                    "No additional explanation required.", List.of(result.reason()),
                    "No action required unless an operator disagrees.", "LOCAL_FALLBACK");
        };
    }
}
