package com.example.stylohub.domain.event;

import java.time.LocalDateTime;
import java.util.UUID;

public record ThemeUpdatedEvent(
        UUID profileId,
        String username,
        LocalDateTime occurredOn
) implements DomainEvent {}
