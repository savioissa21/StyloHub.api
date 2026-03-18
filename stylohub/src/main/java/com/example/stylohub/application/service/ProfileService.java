package com.example.stylohub.application.service;

import com.example.stylohub.application.command.AddWidgetCommand;
import com.example.stylohub.application.command.CreateProfileCommand;
import com.example.stylohub.application.command.UpdateThemeCommand;
import com.example.stylohub.application.command.UpdateWidgetCommand;
import com.example.stylohub.application.port.in.ManageProfileUseCase;
import com.example.stylohub.application.port.out.EventPublisherPort;
import com.example.stylohub.application.port.out.ProfileRepositoryPort;
import com.example.stylohub.domain.exception.BusinessRuleViolationException;
import com.example.stylohub.domain.exception.ResourceNotFoundException;
import com.example.stylohub.domain.model.*;
import com.example.stylohub.domain.model.config.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class ProfileService implements ManageProfileUseCase {

    private final ProfileRepositoryPort profileRepo;
    private final EventPublisherPort eventPublisher;

    public ProfileService(ProfileRepositoryPort profileRepo, EventPublisherPort eventPublisher) {
        this.profileRepo = profileRepo;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public Profile createProfile(CreateProfileCommand command) {
        if (profileRepo.existsByUsername(command.username())) {
            throw new BusinessRuleViolationException(
                "O username '" + command.username() + "' já está em uso."
            );
        }

        Theme defaultTheme = new Theme(
                BackgroundType.SOLID_COLOR, "#09090B",
                "#D4AF37", "#FFFFFF",
                ButtonStyle.ROUNDED, false,
                "#D4AF37", ShadowStyle.NONE
        );

        Profile profile = new Profile(
                UUID.randomUUID(),
                command.userId(),
                command.username(),
                defaultTheme,
                new Subscription(command.plan() != null ? command.plan() : PlanType.FREE)
        );

        Profile saved = profileRepo.save(profile);
        publishAndClear(saved);
        return saved;
    }

    @Override
    public Profile updateTheme(UUID profileId, UpdateThemeCommand command) {
        Profile profile = loadProfile(profileId);

        Theme newTheme = new Theme(
                command.bgType(),
                command.bgValue(),
                command.primaryColor(),
                command.textColor(),
                command.buttonStyle(),
                command.isCustom(),
                command.borderColor(),
                command.shadowStyle()
        );
        profile.updateTheme(newTheme);

        Profile saved = profileRepo.save(profile);
        publishAndClear(saved);
        return saved;
    }

    @Override
    public Profile addWidget(UUID profileId, AddWidgetCommand command) {
        Profile profile = loadProfile(profileId);
        WidgetConfig config = buildWidgetConfig(command);
        profile.addWidget(config, command.order());

        Profile saved = profileRepo.save(profile);
        publishAndClear(saved);
        return saved;
    }

    @Override
    public Profile updateWidget(UUID profileId, UUID widgetId, UpdateWidgetCommand command) {
        Profile profile = loadProfile(profileId);

        Widget widget = profile.getWidgets().stream()
                .filter(w -> w.getId().equals(widgetId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Widget", widgetId));

        WidgetConfig updatedConfig = buildUpdatedConfig(widget, command);
        widget.updateConfig(updatedConfig);

        Profile saved = profileRepo.save(profile);
        publishAndClear(saved);
        return saved;
    }

    @Override
    public Profile removeWidget(UUID profileId, UUID widgetId) {
        Profile profile = loadProfile(profileId);
        profile.removeWidget(widgetId);

        Profile saved = profileRepo.save(profile);
        publishAndClear(saved);
        return saved;
    }

    @Override
    public Profile reorderWidgets(UUID profileId, List<UUID> orderedWidgetIds) {
        Profile profile = loadProfile(profileId);
        profile.reorderWidgets(orderedWidgetIds);

        Profile saved = profileRepo.save(profile);
        publishAndClear(saved);
        return saved;
    }

    @Override
    public Profile toggleWidgetVisibility(UUID profileId, UUID widgetId) {
        Profile profile = loadProfile(profileId);

        profile.getWidgets().stream()
                .filter(w -> w.getId().equals(widgetId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Widget", widgetId))
                .toggleVisibility();

        return profileRepo.save(profile);
    }

    @Override
    public Profile upgradeSubscription(UUID profileId, PlanType newPlan) {
        Profile profile = loadProfile(profileId);
        profile.upgradeSubscription(new Subscription(newPlan));
        return profileRepo.save(profile);
    }

    @Override
    @Transactional(readOnly = true)
    public Profile getProfileByUsername(String username) {
        return profileRepo.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Profile", username));
    }

    @Override
    @Transactional(readOnly = true)
    public Profile getProfileByUserId(UUID userId) {
        return profileRepo.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Profile", userId));
    }

    // --- Helpers privados ---

    private Profile loadProfile(UUID profileId) {
        return profileRepo.findById(profileId)
                .orElseThrow(() -> new ResourceNotFoundException("Profile", profileId));
    }

    private void publishAndClear(Profile profile) {
        eventPublisher.publishAll(profile.getDomainEvents());
        profile.clearEvents();
    }

    private WidgetConfig buildWidgetConfig(AddWidgetCommand cmd) {
        return switch (cmd.type()) {
            case LINK -> new LinkConfig(cmd.title(), cmd.url());
            case YOUTUBE -> new YoutubeConfig(
                    cmd.videoId(),
                    Boolean.TRUE.equals(cmd.autoPlay()),
                    Boolean.TRUE.equals(cmd.showControls())
            );
            case SPOTIFY -> new SpotifyConfig(
                    cmd.spotifyUri(),
                    Boolean.TRUE.equals(cmd.compact())
            );
            case IMAGE -> new ImageConfig(cmd.imageUrl(), cmd.altText(), cmd.linkUrl());
            case TEXT -> new TextConfig(cmd.title(), cmd.content());
            case LEAD_FORM -> new LeadFormConfig(
                    cmd.title(),
                    cmd.buttonLabel(),
                    cmd.successMessage(),
                    cmd.formFields()
            );
        };
    }

    private WidgetConfig buildUpdatedConfig(Widget widget, UpdateWidgetCommand cmd) {
        return switch (widget.getConfig().getType()) {
            case LINK -> new LinkConfig(cmd.title(), cmd.url());
            case YOUTUBE -> new YoutubeConfig(
                    cmd.videoId(),
                    Boolean.TRUE.equals(cmd.autoPlay()),
                    Boolean.TRUE.equals(cmd.showControls())
            );
            case SPOTIFY -> new SpotifyConfig(
                    cmd.spotifyUri(),
                    Boolean.TRUE.equals(cmd.compact())
            );
            case IMAGE -> new ImageConfig(cmd.imageUrl(), cmd.altText(), cmd.linkUrl());
            case TEXT -> new TextConfig(cmd.title(), cmd.content());
            case LEAD_FORM -> new LeadFormConfig(
                    cmd.title(),
                    cmd.buttonLabel(),
                    cmd.successMessage(),
                    cmd.formFields()
            );
        };
    }
}
