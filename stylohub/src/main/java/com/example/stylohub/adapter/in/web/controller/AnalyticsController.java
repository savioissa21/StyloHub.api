package com.example.stylohub.adapter.in.web.controller;

import com.example.stylohub.application.dto.DashboardStatsDTO;
import com.example.stylohub.application.port.in.ManageProfileUseCase;
import com.example.stylohub.application.port.in.TrackAnalyticsUseCase;
import com.example.stylohub.infrastructure.security.StyloHubUserPrincipal;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/creator/analytics")
public class AnalyticsController {

    private final TrackAnalyticsUseCase analyticsUseCase;
    private final ManageProfileUseCase profileUseCase;

    public AnalyticsController(TrackAnalyticsUseCase analyticsUseCase,
                               ManageProfileUseCase profileUseCase) {
        this.analyticsUseCase = analyticsUseCase;
        this.profileUseCase = profileUseCase;
    }

    @GetMapping
    DashboardStatsDTO getDashboardStats(@AuthenticationPrincipal StyloHubUserPrincipal principal) {
        var profile = profileUseCase.getProfileByUserId(principal.getUserIdAsUUID());
        return analyticsUseCase.getDashboardStats(profile.getId());
    }
}
