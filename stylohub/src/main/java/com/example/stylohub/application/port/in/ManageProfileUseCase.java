package com.example.stylohub.application.port.in;

import com.example.stylohub.application.command.AddWidgetCommand;
import com.example.stylohub.application.command.CreateProfileCommand;
import com.example.stylohub.application.command.UpdateThemeCommand;
import com.example.stylohub.application.command.UpdateWidgetCommand;
import com.example.stylohub.domain.model.PlanType;
import com.example.stylohub.domain.model.Profile;

import java.util.List;
import java.util.UUID;

public interface ManageProfileUseCase {
    Profile createProfile(CreateProfileCommand command);
    Profile updateTheme(UUID profileId, UpdateThemeCommand command);
    Profile addWidget(UUID profileId, AddWidgetCommand command);
    Profile updateWidget(UUID profileId, UUID widgetId, UpdateWidgetCommand command);
    Profile removeWidget(UUID profileId, UUID widgetId);
    Profile reorderWidgets(UUID profileId, List<UUID> orderedWidgetIds);
    Profile toggleWidgetVisibility(UUID profileId, UUID widgetId);
    Profile updateAvatarUrl(UUID profileId, String avatarUrl);
    Profile upgradeSubscription(UUID profileId, PlanType newPlan);
    Profile getProfileByUsername(String username);
    Profile getProfileByUserId(UUID userId);
}
