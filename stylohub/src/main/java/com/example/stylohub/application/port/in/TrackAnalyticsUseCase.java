package com.example.stylohub.application.port.in;

import com.example.stylohub.application.dto.DashboardStatsDTO;

import java.util.UUID;

public interface TrackAnalyticsUseCase {
    void recordProfileView(UUID profileId);
    void recordWidgetClick(UUID profileId, UUID widgetId);
    DashboardStatsDTO getDashboardStats(UUID profileId);
}
