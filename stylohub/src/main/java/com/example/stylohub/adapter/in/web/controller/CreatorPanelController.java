package com.example.stylohub.adapter.in.web.controller;

import com.example.stylohub.adapter.in.web.dto.*;
import com.example.stylohub.adapter.in.web.mapper.WebProfileMapper;
import com.example.stylohub.application.port.in.ManageProfileUseCase;
import com.example.stylohub.application.port.out.ImageStoragePort;
import com.example.stylohub.domain.model.Profile;
import com.example.stylohub.infrastructure.security.StyloHubUserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/creator")
@Tag(name = "Creator Panel", description = "Gerenciamento do perfil do criador")
@SecurityRequirement(name = "bearerAuth")
public class CreatorPanelController {

    private final ManageProfileUseCase profileUseCase;
    private final ImageStoragePort imageStorage;
    private final WebProfileMapper mapper;

    public CreatorPanelController(ManageProfileUseCase profileUseCase,
                                  ImageStoragePort imageStorage,
                                  WebProfileMapper mapper) {
        this.profileUseCase = profileUseCase;
        this.imageStorage = imageStorage;
        this.mapper = mapper;
    }

    @GetMapping("/profile")
    @Operation(summary = "Retorna o perfil completo do criador autenticado")
    ProfileResponse getMyProfile(@AuthenticationPrincipal StyloHubUserPrincipal principal) {
        Profile profile = profileUseCase.getProfileByUserId(principal.getUserIdAsUUID());
        return mapper.toCreatorResponse(profile);
    }

    @PutMapping("/profile/theme")
    @Operation(summary = "Atualiza o tema do perfil")
    ProfileResponse updateTheme(@AuthenticationPrincipal StyloHubUserPrincipal principal,
                                @Valid @RequestBody UpdateThemeRequest request) {
        Profile profile = profileUseCase.getProfileByUserId(principal.getUserIdAsUUID());
        return mapper.toCreatorResponse(
                profileUseCase.updateTheme(profile.getId(), mapper.toThemeCommand(request))
        );
    }

    @PostMapping("/profile/widgets")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Adiciona um novo widget ao perfil")
    ProfileResponse addWidget(@AuthenticationPrincipal StyloHubUserPrincipal principal,
                              @Valid @RequestBody AddWidgetRequest request) {
        Profile profile = profileUseCase.getProfileByUserId(principal.getUserIdAsUUID());
        return mapper.toCreatorResponse(
                profileUseCase.addWidget(profile.getId(), mapper.toAddCommand(request))
        );
    }

    @PutMapping("/profile/widgets/{widgetId}")
    @Operation(summary = "Atualiza a configuração de um widget existente")
    ProfileResponse updateWidget(@AuthenticationPrincipal StyloHubUserPrincipal principal,
                                 @PathVariable UUID widgetId,
                                 @RequestBody UpdateWidgetRequest request) {
        Profile profile = profileUseCase.getProfileByUserId(principal.getUserIdAsUUID());
        return mapper.toCreatorResponse(
                profileUseCase.updateWidget(profile.getId(), widgetId, mapper.toUpdateCommand(request))
        );
    }

    @DeleteMapping("/profile/widgets/{widgetId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Remove um widget do perfil")
    void removeWidget(@AuthenticationPrincipal StyloHubUserPrincipal principal,
                      @PathVariable UUID widgetId) {
        Profile profile = profileUseCase.getProfileByUserId(principal.getUserIdAsUUID());
        profileUseCase.removeWidget(profile.getId(), widgetId);
    }

    @PatchMapping("/profile/widgets/{widgetId}/visibility")
    @Operation(summary = "Alterna a visibilidade de um widget")
    ProfileResponse toggleVisibility(@AuthenticationPrincipal StyloHubUserPrincipal principal,
                                     @PathVariable UUID widgetId) {
        Profile profile = profileUseCase.getProfileByUserId(principal.getUserIdAsUUID());
        return mapper.toCreatorResponse(
                profileUseCase.toggleWidgetVisibility(profile.getId(), widgetId)
        );
    }

    @PutMapping("/profile/widgets/reorder")
    @Operation(summary = "Reordena os widgets (drag and drop)")
    ProfileResponse reorderWidgets(@AuthenticationPrincipal StyloHubUserPrincipal principal,
                                   @Valid @RequestBody ReorderWidgetsRequest request) {
        Profile profile = profileUseCase.getProfileByUserId(principal.getUserIdAsUUID());
        return mapper.toCreatorResponse(
                profileUseCase.reorderWidgets(profile.getId(), request.orderedWidgetIds())
        );
    }

    @PostMapping(value = "/profile/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Faz upload de uma imagem (avatar, background ou widget)")
    Map<String, String> uploadImage(@AuthenticationPrincipal StyloHubUserPrincipal principal,
                                    @RequestParam("file") MultipartFile file,
                                    @RequestParam(value = "folder", defaultValue = "profiles") String folder) {
        String url = imageStorage.upload(file, folder);
        return Map.of("url", url);
    }
}
