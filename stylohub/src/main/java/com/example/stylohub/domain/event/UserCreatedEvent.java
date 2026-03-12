package com.example.stylohub.domain.event;

import com.example.stylohub.domain.model.OAuthProvider;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserCreatedEvent(
        UUID userId,
        String email,
        OAuthProvider provider,
        LocalDateTime occurredOn
) implements DomainEvent {}
