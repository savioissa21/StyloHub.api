package com.example.stylohub.domain.event;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserDeactivatedEvent(
        UUID userId,
        String email,
        LocalDateTime occurredOn
) implements DomainEvent {}
