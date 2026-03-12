package com.example.stylohub.adapter.in.web.controller;

import com.example.stylohub.adapter.in.web.cache.ProfileCacheService;
import com.example.stylohub.adapter.in.web.dto.ProfileResponse;
import com.example.stylohub.application.port.in.TrackAnalyticsUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/p")
@Tag(name = "Public Profiles", description = "Acesso público aos perfis dos criadores")
public class PublicProfileController {

    private final ProfileCacheService profileCacheService;
    private final TrackAnalyticsUseCase analyticsUseCase;

    public PublicProfileController(ProfileCacheService profileCacheService,
                                   TrackAnalyticsUseCase analyticsUseCase) {
        this.profileCacheService = profileCacheService;
        this.analyticsUseCase = analyticsUseCase;
    }

    @GetMapping("/{username}")
    @Operation(summary = "Retorna o perfil público de um criador pelo username")
    ProfileResponse getPublicProfile(@PathVariable String username) {
        ProfileResponse response = profileCacheService.getPublicProfile(username);
        analyticsUseCase.recordProfileView(response.id());
        return response;
    }

    @PostMapping("/{username}/widgets/{widgetId}/click")
    @Operation(summary = "Registra um clique num widget (para analytics)")
    void recordWidgetClick(@PathVariable String username, @PathVariable UUID widgetId) {
        ProfileResponse response = profileCacheService.getPublicProfile(username);
        analyticsUseCase.recordWidgetClick(response.id(), widgetId);
    }
}
