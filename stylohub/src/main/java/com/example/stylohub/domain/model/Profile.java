package com.example.stylohub.domain.model;

import com.example.stylohub.domain.event.ThemeUpdatedEvent;
import com.example.stylohub.domain.event.WidgetAddedEvent;
import com.example.stylohub.domain.event.WidgetRemovedEvent;
import com.example.stylohub.domain.exception.BusinessRuleViolationException;
import com.example.stylohub.domain.exception.DomainValidationException;
import com.example.stylohub.domain.exception.ResourceNotFoundException;
import com.example.stylohub.domain.model.config.WidgetConfig;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class Profile extends AggregateRoot {

    private final UUID id;
    private final UUID userId;
    private String username;
    private Theme theme;
    private Subscription subscription;
    private final List<Widget> widgets;

    public Profile(UUID id, UUID userId, String username, Theme theme, Subscription subscription) {
        if (id == null || userId == null || username == null || username.isBlank()) {
            throw new DomainValidationException("Os campos id, userId e username são obrigatórios.");
        }
        if (subscription == null) {
            throw new DomainValidationException("O perfil deve ter sempre uma subscrição.");
        }
        this.id = id;
        this.userId = userId;
        this.username = username;
        this.theme = theme;
        this.subscription = subscription;
        this.widgets = new ArrayList<>();
    }

    public void updateTheme(Theme newTheme) {
        if (newTheme == null) {
            throw new DomainValidationException("O novo tema não pode ser nulo.");
        }
        if (!this.subscription.canUseCustomTheme() && newTheme.isCustom()) {
            throw new BusinessRuleViolationException(
                "O teu plano atual não permite o uso de temas personalizados. Faz upgrade para PRO."
            );
        }
        this.theme = newTheme;
        this.registerEvent(new ThemeUpdatedEvent(this.id, this.username, LocalDateTime.now()));
    }

    public Widget addWidget(WidgetConfig config, int order) {
        if (config == null) {
            throw new DomainValidationException("A configuração do widget é obrigatória.");
        }
        if (!this.subscription.canAddWidget(this.widgets.size())) {
            throw new BusinessRuleViolationException(
                "Atingiste o limite de " + this.subscription.getMaxWidgets() +
                " widgets para o plano " + this.subscription.getPlan() + ". Faz upgrade para PRO."
            );
        }
        config.validate();

        Widget newWidget = new Widget(UUID.randomUUID(), config, order);
        this.widgets.add(newWidget);
        this.registerEvent(new WidgetAddedEvent(this.id, newWidget.getId(), config.getType(), LocalDateTime.now()));
        return newWidget;
    }

    public void removeWidget(UUID widgetId) {
        if (widgetId == null) {
            throw new DomainValidationException("O ID do widget é obrigatório.");
        }
        Widget widget = findWidgetById(widgetId);
        this.widgets.remove(widget);
        this.registerEvent(new WidgetRemovedEvent(this.id, widgetId, LocalDateTime.now()));
    }

    public void reorderWidgets(List<UUID> orderedWidgetIds) {
        if (orderedWidgetIds == null || orderedWidgetIds.size() != this.widgets.size()) {
            throw new DomainValidationException("A lista de reordenação deve conter todos os widgets do perfil.");
        }
        for (int i = 0; i < orderedWidgetIds.size(); i++) {
            Widget widget = findWidgetById(orderedWidgetIds.get(i));
            widget.setOrderIndex(i);
        }
    }

    public void upgradeSubscription(Subscription newSubscription) {
        if (newSubscription == null) {
            throw new DomainValidationException("A nova subscrição não pode ser nula.");
        }
        this.subscription = newSubscription;
    }

    private Widget findWidgetById(UUID widgetId) {
        return this.widgets.stream()
                .filter(w -> w.getId().equals(widgetId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Widget", widgetId));
    }

    /**
     * Factory para reconstituição a partir da persistência.
     * Bypassa validações de negócio de criação e não dispara eventos.
     */
    public static Profile reconstitute(UUID id, UUID userId, String username,
                                       Theme theme, Subscription subscription,
                                       List<Widget> widgets) {
        Profile profile = new Profile(id, userId, username, theme, subscription);
        profile.widgets.addAll(widgets);
        return profile;
    }

    public UUID getId() { return id; }
    public UUID getUserId() { return userId; }
    public String getUsername() { return username; }
    public Theme getTheme() { return theme; }
    public Subscription getSubscription() { return subscription; }
    public List<Widget> getWidgets() { return Collections.unmodifiableList(widgets); }
}
