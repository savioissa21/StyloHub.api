package com.example.stylohub.adapter.in.web.controller;

import com.example.stylohub.adapter.in.web.dto.*;
import com.example.stylohub.adapter.in.web.mapper.WebProfileMapper;
import com.example.stylohub.application.port.in.ManageProfileUseCase;
import com.example.stylohub.domain.model.Profile;
import com.example.stylohub.infrastructure.security.StyloHubUserPrincipal;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/creator")
public class CreatorPanelController {

    private final ManageProfileUseCase profileUseCase;
    private final WebProfileMapper mapper;

    public CreatorPanelController(ManageProfileUseCase profileUseCase, WebProfileMapper mapper) {
        this.profileUseCase = profileUseCase;
        this.mapper = mapper;
    }

    @GetMapping("/profile")
    ProfileResponse getMyProfile(@AuthenticationPrincipal StyloHubUserPrincipal principal) {
        Profile profile = profileUseCase.getProfileByUserId(principal.getUserIdAsUUID());
        return mapper.toCreatorResponse(profile);
    }

    @PutMapping("/profile/theme")
    ProfileResponse updateTheme(@AuthenticationPrincipal StyloHubUserPrincipal principal,
                                @Valid @RequestBody UpdateThemeRequest request) {
        Profile profile = profileUseCase.getProfileByUserId(principal.getUserIdAsUUID());
        Profile updated = profileUseCase.updateTheme(profile.getId(), mapper.toThemeCommand(request));
        return mapper.toCreatorResponse(updated);
    }

    @PostMapping("/profile/widgets")
    @ResponseStatus(HttpStatus.CREATED)
    ProfileResponse addWidget(@AuthenticationPrincipal StyloHubUserPrincipal principal,
                              @Valid @RequestBody AddWidgetRequest request) {
        Profile profile = profileUseCase.getProfileByUserId(principal.getUserIdAsUUID());
        Profile updated = profileUseCase.addWidget(profile.getId(), mapper.toAddCommand(request));
        return mapper.toCreatorResponse(updated);
    }

    @PutMapping("/profile/widgets/{widgetId}")
    ProfileResponse updateWidget(@AuthenticationPrincipal StyloHubUserPrincipal principal,
                                 @PathVariable UUID widgetId,
                                 @RequestBody UpdateWidgetRequest request) {
        Profile profile = profileUseCase.getProfileByUserId(principal.getUserIdAsUUID());
        Profile updated = profileUseCase.updateWidget(profile.getId(), widgetId, mapper.toUpdateCommand(request));
        return mapper.toCreatorResponse(updated);
    }

    @DeleteMapping("/profile/widgets/{widgetId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void removeWidget(@AuthenticationPrincipal StyloHubUserPrincipal principal,
                      @PathVariable UUID widgetId) {
        Profile profile = profileUseCase.getProfileByUserId(principal.getUserIdAsUUID());
        profileUseCase.removeWidget(profile.getId(), widgetId);
    }

    @PatchMapping("/profile/widgets/{widgetId}/visibility")
    ProfileResponse toggleVisibility(@AuthenticationPrincipal StyloHubUserPrincipal principal,
                                     @PathVariable UUID widgetId) {
        Profile profile = profileUseCase.getProfileByUserId(principal.getUserIdAsUUID());
        Profile updated = profileUseCase.toggleWidgetVisibility(profile.getId(), widgetId);
        return mapper.toCreatorResponse(updated);
    }

    @PutMapping("/profile/widgets/reorder")
    ProfileResponse reorderWidgets(@AuthenticationPrincipal StyloHubUserPrincipal principal,
                                   @Valid @RequestBody ReorderWidgetsRequest request) {
        Profile profile = profileUseCase.getProfileByUserId(principal.getUserIdAsUUID());
        Profile updated = profileUseCase.reorderWidgets(profile.getId(), request.orderedWidgetIds());
        return mapper.toCreatorResponse(updated);
    }
}
