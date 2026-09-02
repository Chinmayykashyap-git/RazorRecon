package com.razorrecon.explanation;

import com.razorrecon.model.ThreeWayReconciliationResult;

public interface ExplanationService {
    Explanation explain(ThreeWayReconciliationResult result);
}
