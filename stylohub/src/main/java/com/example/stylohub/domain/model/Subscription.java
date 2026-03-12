package com.example.stylohub.domain.model;

import com.example.stylohub.domain.exception.DomainValidationException;

public class Subscription {

    private static final int FREE_MAX_WIDGETS = 5;
    private static final int PRO_MAX_WIDGETS = 100;

    private final PlanType plan;
    private final int maxWidgets;

    public Subscription(PlanType plan) {
        if (plan == null) {
            throw new DomainValidationException("O plano da subscrição não pode ser nulo.");
        }
        this.plan = plan;
        this.maxWidgets = (plan == PlanType.FREE) ? FREE_MAX_WIDGETS : PRO_MAX_WIDGETS;
    }

    public boolean canAddWidget(int currentWidgetCount) {
        return currentWidgetCount < this.maxWidgets;
    }

    public boolean canUseCustomTheme() {
        return this.plan == PlanType.PRO;
    }

    public boolean canAccessAnalytics() {
        return this.plan == PlanType.PRO;
    }

    public PlanType getPlan() { return plan; }
    public int getMaxWidgets() { return maxWidgets; }
}
