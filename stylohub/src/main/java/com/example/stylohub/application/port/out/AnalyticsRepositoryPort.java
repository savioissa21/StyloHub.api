package com.example.stylohub.application.port.out;

import com.example.stylohub.application.dto.DashboardStatsDTO;

import java.util.UUID;

public interface AnalyticsRepositoryPort {
    void recordProfileView(UUID profileId);
    void recordWidgetClick(UUID profileId, UUID widgetId);
    DashboardStatsDTO getStats(UUID profileId);
}
