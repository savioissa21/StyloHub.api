package com.example.stylohub.domain.model;

import com.example.stylohub.domain.event.DomainEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Classe base para todos os Agregados do sistema (ex: Profile, User).
 * Responsável por gerenciar os eventos de domínio que ocorrem dentro do ciclo de vida da entidade.
 */
public abstract class AggregateRoot {

    // Lista interna e privada. Ninguém de fora pode dar um .add() diretamente.
    private final List<DomainEvent> domainEvents = new ArrayList<>();

    /**
     * Registra um novo evento de domínio neste Aggregate.
     * O modificador é 'protected' porque APENAS a própria entidade (ex: Profile) 
     * ou suas filhas têm permissão para dizer que um evento aconteceu.
     *
     * @param event O evento que acabou de ocorrer.
     */
    protected void registerEvent(DomainEvent event) {
        if (event != null) {
            this.domainEvents.add(event);
        }
    }

    /**
     * Retorna os eventos acumulados.
     * Retorna uma lista IMUTÁVEL (unmodifiableList) para garantir que nenhuma
     * outra classe altere a lista de eventos sem passar pelo método registerEvent.
     */
    public List<DomainEvent> getDomainEvents() {
        return Collections.unmodifiableList(domainEvents);
    }

    /**
     * Limpa a fila de eventos.
     * O Application Service (ex: ProfileService) é obrigado a chamar este método
     * logo após publicar os eventos na mensageria (Kafka/Spring Events) para evitar 
     * que sejam publicados em duplicidade caso a instância continue na memória.
     */
    public void clearEvents() {
        this.domainEvents.clear();
    }
}