package com.example.stylohub.domain.event;

import java.time.LocalDateTime;
import java.util.UUID;

public record WidgetRemovedEvent(
        UUID profileId,
        UUID widgetId,
        LocalDateTime occurredOn
) implements DomainEvent {}
