package com.example.stylohub.domain.model;

import com.example.stylohub.domain.event.ProfileUpdatedEvent;
import com.example.stylohub.domain.model.config.WidgetConfig;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Aggregate Root principal do sistema.
 * Gere a página pública do utilizador, os seus widgets e o seu tema,
 * garantindo que as regras do plano (Subscription) não sejam violadas.
 */
public class Profile extends AggregateRoot {

    private final UUID id;
    private final UUID userId;
    private String username;
    private Theme theme;
    private Subscription subscription;
    private final List<Widget> widgets;

    /**
     * Construtor do Domínio.
     * Garante que o Perfil nasce num estado válido (Invariants).
     */
    public Profile(UUID id, UUID userId, String username, Theme theme, Subscription subscription) {
        if (id == null || userId == null || username == null || username.isBlank()) {
            throw new IllegalArgumentException("Os campos id, userId e username são obrigatórios.");
        }
        if (subscription == null) {
            throw new IllegalArgumentException("O perfil deve ter sempre uma subscrição (mesmo que seja FREE).");
        }
        
        this.id = id;
        this.userId = userId;
        this.username = username;
        this.theme = theme;
        this.subscription = subscription;
        this.widgets = new ArrayList<>();
    }

    /**
     * Atualiza o tema do perfil.
     * * @param newTheme O novo tema validado.
     */
    public void updateTheme(Theme newTheme) {
        if (newTheme == null) {
            throw new IllegalArgumentException("O novo tema não pode ser nulo.");
        }

        // Regra de Negócio: O plano atual permite temas personalizados?
        if (!this.subscription.canUseCustomTheme() && newTheme.isCustom()) {
            // No futuro, podemos criar uma DomainValidationException específica
            throw new IllegalStateException("O teu plano atual não permite o uso de temas personalizados.");
        }

        this.theme = newTheme;

        // Dispara o evento: O Perfil foi alterado! (Para limpar a cache Edge/Cloudflare depois)
        this.registerEvent(new ProfileUpdatedEvent(this.id, this.username, LocalDateTime.now()));
    }

    /**
     * Adiciona um novo widget (ex: Link, Youtube, Spotify) ao perfil.
     * * @param config A configuração tipada e validada do widget.
     * @param order A posição do widget na página.
     */
    public void addWidget(WidgetConfig config, int order) {
        if (config == null) {
            throw new IllegalArgumentException("A configuração do widget é obrigatória.");
        }

        // Regra de Negócio: O limite de widgets do plano foi atingido?
        if (!this.subscription.canAddWidget(this.widgets.size())) {
            throw new IllegalStateException("Atingiste o limite de widgets para o teu plano atual.");
        }

        // Delegação: Pede à própria configuração para se validar (ex: "A URL é válida?")
        config.validate();

        // Cria a entidade subordinada
        Widget newWidget = new Widget(UUID.randomUUID(), config, order);
        this.widgets.add(newWidget);

        // Dispara o evento: A página mudou, logo precisamos de limpar a cache!
        this.registerEvent(new ProfileUpdatedEvent(this.id, this.username, LocalDateTime.now()));
    }

    // --- GETTERS ---
    // Nota: NÃO TEMOS SETTERS. Qualquer alteração deve ser feita pelos métodos de negócio acima.

    public UUID getId() {
        return id;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public Theme getTheme() {
        return theme;
    }

    public Subscription getSubscription() {
        return subscription;
    }

    /**
     * Retorna a lista de forma imutável.
     * Assim, nenhum serviço externo consegue fazer profile.getWidgets().add(...) e burlar a regra da Subscrição.
     */
    public List<Widget> getWidgets() {
        return Collections.unmodifiableList(widgets);
    }
}