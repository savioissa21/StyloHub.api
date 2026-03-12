package com.example.stylohub.domain.model;

/**
 * Value Object que encapsula as regras de negócio baseadas no plano do utilizador.
 */
public class Subscription {

    private final PlanType plan;
    private final int maxWidgets;

    public Subscription(PlanType plan) {
        if (plan == null) {
            throw new IllegalArgumentException("O plano da subscrição não pode ser nulo.");
        }
        this.plan = plan;
        
        // Regras de negócio internas: Quantos blocos cada plano suporta?
        this.maxWidgets = (plan == PlanType.FREE) ? 5 : 100; // FREE tem 5 links, PRO tem 100
    }

    /**
     * Verifica se o utilizador ainda pode adicionar mais blocos.
     */
    public boolean canAddWidget(int currentWidgetCount) {
        return currentWidgetCount < this.maxWidgets;
    }

    /**
     * Verifica se o utilizador tem direito a criar cores e botões 100% personalizados.
     */
    public boolean canUseCustomTheme() {
        return this.plan == PlanType.PRO;
    }

    public PlanType getPlan() {
        return plan;
    }
}