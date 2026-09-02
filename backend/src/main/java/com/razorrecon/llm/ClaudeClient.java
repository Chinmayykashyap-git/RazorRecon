package com.razorrecon.llm;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.razorrecon.config.ClaudeConfig;

@Component
public class ClaudeClient {

	private final ClaudeConfig config;
	private final ObjectMapper objectMapper;
	private final HttpClient httpClient;

	public ClaudeClient(ClaudeConfig config, ObjectMapper objectMapper) {
		this.config = config;
		this.objectMapper = objectMapper;
		this.httpClient = HttpClient.newHttpClient();
	}

	public String investigate(String prompt) {
		if (!config.isEnabled()) {
			return mockResponse();
		}

		if (config.getApiKey() == null || config.getApiKey().isBlank()) {
			throw new IllegalStateException("Claude is enabled but claude.api-key is missing");
		}

		try {
			String requestBody = objectMapper.writeValueAsString(Map.of(
					"model", config.getModel(),
					"max_tokens", 512,
					"messages", List.of(Map.of("role", "user", "content", prompt))
			));

			HttpRequest request = HttpRequest.newBuilder()
					.uri(URI.create("https://api.anthropic.com/v1/messages"))
					.header("Content-Type", "application/json")
					.header("x-api-key", config.getApiKey())
					.header("anthropic-version", "2023-06-01")
					.POST(HttpRequest.BodyPublishers.ofString(requestBody))
					.build();

			HttpResponse<String> response = httpClient.send(
					request,
					HttpResponse.BodyHandlers.ofString()
			);

			if (response.statusCode() < 200 || response.statusCode() >= 300) {
				throw new RuntimeException("Claude API returned HTTP " + response.statusCode());
			}

			JsonNode root = objectMapper.readTree(response.body());
			return root.path("content").path(0).path("text").asText();
		} catch (Exception exception) {
			throw new RuntimeException("Claude investigation failed", exception);
		}
	}

	private String mockResponse() {
		return """
				{
				  "decision": "INVESTIGATE",
				  "confidence": 0.50,
				  "matchedId": null,
				  "reasoning": "Mock LLM response. Claude integration is disabled."
				}
				""";
	}
}
