package com.example.stylohub.application.dto;

import java.util.Map;
import java.util.UUID;

public record DashboardStatsDTO(
        UUID profileId,
        long totalViews,
        long totalClicks,
        double clickThroughRate,
        Map<UUID, Long> clicksPerWidget
) {}
