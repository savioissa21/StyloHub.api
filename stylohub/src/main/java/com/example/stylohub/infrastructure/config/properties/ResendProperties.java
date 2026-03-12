package com.example.stylohub.infrastructure.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.resend")
public record ResendProperties(
        String apiKey,
        String fromEmail,
        String fromName
) {}
