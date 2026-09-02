package com.razorrecon.rules;

import java.time.Duration;
import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

@Component
public class DateWindowRule {

    private static final long DEFAULT_WINDOW_DAYS = 3;

    public boolean matches(
            LocalDateTime transactionDate,
            LocalDateTime ledgerDate) {

        if (transactionDate == null || ledgerDate == null) {
            return false;
        }

        long days = Math.abs(
                Duration.between(
                        transactionDate,
                        ledgerDate
                ).toDays()
        );

        return days <= DEFAULT_WINDOW_DAYS;
    }
}