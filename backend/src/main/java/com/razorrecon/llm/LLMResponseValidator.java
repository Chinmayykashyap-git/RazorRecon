package com.razorrecon.llm;

import org.springframework.stereotype.Component;

@Component
public class LLMResponseValidator {

    public boolean isValid(LLMResponse response) {
        if (response == null || response.getDecision() == null) {
            return false;
        }

        if (response.getConfidence() < 0.0 || response.getConfidence() > 1.0) {
            return false;
        }

        String decision = response.getDecision().toUpperCase();
        return decision.equals("MATCH")
                || decision.equals("NO_MATCH")
                || decision.equals("INVESTIGATE");
    }

    public LLMResponse sanitize(LLMResponse response) {
        if (!isValid(response)) {
            return new LLMResponse(
                    "INVESTIGATE",
                    0.0,
                    null,
                    "LLM response failed validation"
            );
        }

        response.setDecision(response.getDecision().toUpperCase());
        response.setConfidence(Math.min(1.0, Math.max(0.0, response.getConfidence())));
        return response;
    }
}