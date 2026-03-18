package com.example.stylohub.adapter.in.web.dto;

import com.example.stylohub.domain.model.Lead;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

public record LeadResponse(
        UUID id,
        UUID widgetId,
        String widgetTitle,
        String email,
        Map<String, String> fields,
        LocalDateTime capturedAt
) {
    public static LeadResponse from(Lead lead) {
        return new LeadResponse(
                lead.getId(),
                lead.getWidgetId(),
                lead.getWidgetTitle(),
                lead.getEmail(),
                lead.getFields(),
                lead.getCapturedAt()
        );
    }
}
