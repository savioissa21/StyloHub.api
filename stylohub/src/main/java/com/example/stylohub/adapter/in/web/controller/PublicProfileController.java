package com.example.stylohub.adapter.in.web.controller;

import com.example.stylohub.adapter.in.web.dto.ProfileResponse;
import com.example.stylohub.adapter.in.web.mapper.WebProfileMapper;
import com.example.stylohub.application.port.in.ManageProfileUseCase;
import com.example.stylohub.application.port.in.TrackAnalyticsUseCase;
import com.example.stylohub.domain.model.Profile;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/p")
public class PublicProfileController {

    private final ManageProfileUseCase profileUseCase;
    private final TrackAnalyticsUseCase analyticsUseCase;
    private final WebProfileMapper mapper;

    public PublicProfileController(ManageProfileUseCase profileUseCase,
                                   TrackAnalyticsUseCase analyticsUseCase,
                                   WebProfileMapper mapper) {
        this.profileUseCase = profileUseCase;
        this.analyticsUseCase = analyticsUseCase;
        this.mapper = mapper;
    }

    @GetMapping("/{username}")
    ProfileResponse getPublicProfile(@PathVariable String username) {
        Profile profile = profileUseCase.getProfileByUsername(username);
        analyticsUseCase.recordProfileView(profile.getId());
        return mapper.toPublicResponse(profile);
    }

    @PostMapping("/{username}/widgets/{widgetId}/click")
    void recordWidgetClick(@PathVariable String username, @PathVariable UUID widgetId) {
        Profile profile = profileUseCase.getProfileByUsername(username);
        analyticsUseCase.recordWidgetClick(profile.getId(), widgetId);
    }
}
