package com.razorrecon.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.razorrecon.service.ReconciliationStatsService;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final ReconciliationStatsService statsService;

    public DashboardController(ReconciliationStatsService statsService) {
        this.statsService = statsService;
    }

    @GetMapping("/health")
    public Map<String, Object> health() {

        return Map.of(
                "application", "RazorRecon",
                "status", "UP",
                "engine", "Deterministic + Rule Based",
                "llm", "Not invoked in Phase 10"
        );
    }

    @GetMapping("/stats")
    public Map<String, Object> stats() {
        return Map.of(
                "message",
                "Statistics become available after a reconciliation batch."
        );
    }
}