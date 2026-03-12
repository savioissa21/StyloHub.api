package com.example.stylohub.domain.model;

import com.example.stylohub.domain.model.config.WidgetConfig;
import java.util.UUID;

/**
 * Entidade subordinada ao Profile.
 * Representa um bloco visual na página do utilizador.
 */
public class Widget {

    private final UUID id;
    private WidgetConfig config;
    private int orderIndex;
    private boolean isActive;

    /**
     * Construtor restrito (package-private).
     * Só o Profile tem o direito de instanciar um Widget através do método addWidget().
     */
    Widget(UUID id, WidgetConfig config, int orderIndex) {
        if (id == null || config == null) {
            throw new IllegalArgumentException("ID e Configuração são obrigatórios para um Widget.");
        }
        this.id = id;
        this.config = config;
        this.orderIndex = orderIndex;
        this.isActive = true; // Por defeito, um widget nasce ativo
    }

    /**
     * Atualiza a configuração do widget (ex: o utilizador mudou o título do link).
     */
    public void updateConfig(WidgetConfig newConfig) {
        if (newConfig == null) {
            throw new IllegalArgumentException("A nova configuração não pode ser nula.");
        }
        // Garante que não podemos transformar um bloco de Link num bloco de Youtube acidentalmente
        if (this.config.getType() != newConfig.getType()) {
            throw new IllegalStateException("Não é possível alterar o tipo de um widget existente.");
        }
        
        newConfig.validate();
        this.config = newConfig;
    }

    /**
     * Altera a visibilidade do bloco na página pública.
     */
    public void toggleVisibility() {
        this.isActive = !this.isActive;
    }

    /**
     * Atualiza a posição do bloco (Drag and Drop).
     */
    public void setOrderIndex(int newOrderIndex) {
        if (newOrderIndex < 0) {
            throw new IllegalArgumentException("A ordem não pode ser negativa.");
        }
        this.orderIndex = newOrderIndex;
    }

    // --- GETTERS ---

    public UUID getId() {
        return id;
    }

    public WidgetConfig getConfig() {
        return config;
    }

    public int getOrderIndex() {
        return orderIndex;
    }

    public boolean isActive() {
        return isActive;
    }
}