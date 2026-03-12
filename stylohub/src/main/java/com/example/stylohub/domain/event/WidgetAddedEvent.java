package com.example.stylohub.domain.event;

import com.example.stylohub.domain.model.WidgetType;

import java.time.LocalDateTime;
import java.util.UUID;

public record WidgetAddedEvent(
        UUID profileId,
        String username,
        UUID widgetId,
        WidgetType widgetType,
        LocalDateTime occurredOn
) implements DomainEvent {}
