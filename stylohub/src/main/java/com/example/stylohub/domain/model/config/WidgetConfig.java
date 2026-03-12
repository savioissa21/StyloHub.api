package com.example.stylohub.domain.model.config;

import com.example.stylohub.domain.model.WidgetType;

/**
 * Value Object base para as configurações específicas de cada widget.
 */
public interface WidgetConfig {
    
    /**
     * Retorna o tipo deste widget para facilitar a serialização/deserialização (JSON).
     */
    WidgetType getType();

    /**
     * Regra de Negócio: Cada widget sabe como validar os seus próprios dados.
     * Deve lançar uma excepção (ex: IllegalArgumentException) se os dados forem inválidos.
     */
    void validate();
}