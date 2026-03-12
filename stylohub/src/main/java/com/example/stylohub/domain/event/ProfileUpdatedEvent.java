package com.example.stylohub.domain.event;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @deprecated Use ThemeUpdatedEvent or WidgetAddedEvent for granular tracking.
 */
@Deprecated
public record ProfileUpdatedEvent(
        UUID profileId,
        String username,
        LocalDateTime occurredOn
) implements DomainEvent {}
