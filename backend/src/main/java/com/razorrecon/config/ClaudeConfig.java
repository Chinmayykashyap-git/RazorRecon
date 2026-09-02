package com.razorrecon.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClaudeConfig {

	@Value("${claude.api-key:}")
	private String apiKey;

	@Value("${claude.enabled:false}")
	private boolean enabled;

	@Value("${claude.model:claude-sonnet-4-20250514}")
	private String model;

	public String getApiKey() {
		return apiKey;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public String getModel() {
		return model;
	}
}
