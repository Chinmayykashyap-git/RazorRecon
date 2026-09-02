package com.razorrecon.llm;

import java.util.List;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.razorrecon.model.BankTransaction;
import com.razorrecon.model.LedgerEntry;

@Component
public class LLMMatcher {

    private final ClaudeClient claudeClient;
    private final LLMResponseValidator validator;
    private final ObjectMapper objectMapper;

    public LLMMatcher(
            ClaudeClient claudeClient,
            LLMResponseValidator validator,
            ObjectMapper objectMapper) {
        this.claudeClient = claudeClient;
        this.validator = validator;
        this.objectMapper = objectMapper;
    }

    public LLMResponse investigate(
            BankTransaction transaction,
            List<LedgerEntry> candidates) {
        try {
            String rawResponse = claudeClient.investigate(buildPrompt(transaction, candidates));
            return validator.sanitize(objectMapper.readValue(rawResponse, LLMResponse.class));
        } catch (Exception exception) {
            return new LLMResponse(
                    "INVESTIGATE",
                    0.0,
                    null,
                    "Unable to parse LLM response safely"
            );
        }
    }

    private String buildPrompt(
            BankTransaction transaction,
            List<LedgerEntry> candidates) {
        StringBuilder prompt = new StringBuilder("""
                You are a payment reconciliation investigator.
                You MUST NOT invent transaction data.
                Analyze only the transaction and candidates supplied below.
                Return ONLY valid JSON in this exact structure:
                {"decision":"MATCH","confidence":0.0,"matchedId":"ledger-id-or-null","reasoning":"short evidence-based explanation"}
                Allowed decisions: MATCH, NO_MATCH, INVESTIGATE.
                Transaction:
                """);

        if (transaction != null) {
            prompt.append("\nTransaction ID: ").append(transaction.getTransactionId())
                    .append("\nReference: ").append(transaction.getReference())
                    .append("\nAmount: ").append(transaction.getAmount())
                    .append("\nDate: ").append(transaction.getTransactionDate());
        }

        prompt.append("\n\nCandidates:\n");
        if (candidates != null) {
            for (LedgerEntry candidate : candidates) {
                prompt.append("\nLedger ID: ").append(candidate.getLedgerId())
                        .append("\nReference: ").append(candidate.getReference())
                        .append("\nAmount: ").append(candidate.getAmount())
                        .append("\nDate: ").append(candidate.getEntryDate())
                        .append("\n---");
            }
        }
        return prompt.toString();
    }
}