package com.example.stylohub.adapter.in.web.controller;

import com.example.stylohub.adapter.in.web.cache.ProfileCacheService;
import com.example.stylohub.adapter.in.web.dto.ProfileResponse;
import com.example.stylohub.adapter.in.web.dto.SubmitLeadRequest;
import com.example.stylohub.application.port.in.ManageLeadsUseCase;
import com.example.stylohub.application.port.in.TrackAnalyticsUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/p")
@Tag(name = "Public Profiles", description = "Acesso público aos perfis dos criadores")
public class PublicProfileController {

    private final ProfileCacheService profileCacheService;
    private final TrackAnalyticsUseCase analyticsUseCase;
    private final ManageLeadsUseCase manageLeadsUseCase;

    public PublicProfileController(ProfileCacheService profileCacheService,
                                   TrackAnalyticsUseCase analyticsUseCase,
                                   ManageLeadsUseCase manageLeadsUseCase) {
        this.profileCacheService = profileCacheService;
        this.analyticsUseCase = analyticsUseCase;
        this.manageLeadsUseCase = manageLeadsUseCase;
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

    @PostMapping("/{username}/leads")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Captura um lead submetido via formulário da página pública")
    void submitLead(@PathVariable String username, @Valid @RequestBody SubmitLeadRequest request) {
        manageLeadsUseCase.captureLead(username, request.widgetId(), request.fields());
    }
}
