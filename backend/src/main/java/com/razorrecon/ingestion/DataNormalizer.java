package com.razorrecon.ingestion;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

@Component
public class DataNormalizer {

    public String normalizeReference(String reference) {

        if (reference == null) {
            return "";
        }

        return reference
                .trim()
                .toUpperCase()
                .replaceAll("[^A-Z0-9]", "");
    }

    public BigDecimal normalizeAmount(String amount) {

        if (amount == null || amount.isBlank()) {
            return BigDecimal.ZERO;
        }

        String cleaned = amount
                .replace("₹", "")
                .replace(",", "")
                .trim();

        return new BigDecimal(cleaned);
    }
}