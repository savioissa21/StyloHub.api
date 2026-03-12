package com.example.stylohub.infrastructure.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.stripe")
public record StripeProperties(
        String secretKey,
        String webhookSecret,
        String proPriceId
) {}
