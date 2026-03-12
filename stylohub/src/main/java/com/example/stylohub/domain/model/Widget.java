package com.example.stylohub.domain.model;

import com.example.stylohub.domain.exception.BusinessRuleViolationException;
import com.example.stylohub.domain.exception.DomainValidationException;
import com.example.stylohub.domain.model.config.WidgetConfig;

import java.util.UUID;

public class Widget {

    private final UUID id;
    private WidgetConfig config;
    private int orderIndex;
    private boolean isActive;

    Widget(UUID id, WidgetConfig config, int orderIndex) {
        if (id == null || config == null) {
            throw new DomainValidationException("ID e configuração são obrigatórios para um Widget.");
        }
        this.id = id;
        this.config = config;
        this.orderIndex = orderIndex;
        this.isActive = true;
    }

    public void updateConfig(WidgetConfig newConfig) {
        if (newConfig == null) {
            throw new DomainValidationException("A nova configuração não pode ser nula.");
        }
        if (this.config.getType() != newConfig.getType()) {
            throw new BusinessRuleViolationException(
                "Não é possível alterar o tipo de um widget existente. Tipo atual: " + this.config.getType()
            );
        }
        newConfig.validate();
        this.config = newConfig;
    }

    public void toggleVisibility() {
        this.isActive = !this.isActive;
    }

    public void setOrderIndex(int newOrderIndex) {
        if (newOrderIndex < 0) {
            throw new DomainValidationException("A ordem do widget não pode ser negativa.");
        }
        this.orderIndex = newOrderIndex;
    }

    /**
     * Factory para reconstituição a partir da persistência.
     * Não dispara eventos, não valida regras de negócio de criação.
     */
    public static Widget reconstitute(UUID id, WidgetConfig config, int orderIndex, boolean isActive) {
        Widget widget = new Widget(id, config, orderIndex);
        if (!isActive) widget.isActive = false;
        return widget;
    }

    public UUID getId() { return id; }
    public WidgetConfig getConfig() { return config; }
    public int getOrderIndex() { return orderIndex; }
    public boolean isActive() { return isActive; }
}
