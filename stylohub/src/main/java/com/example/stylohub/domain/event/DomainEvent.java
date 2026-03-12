package com.example.stylohub.domain.event;

import java.time.LocalDateTime;

/**
 * Interface marcadora e contrato base para todos os eventos do nosso domínio.
 */
public interface DomainEvent {

    /**
     * Todo evento precisa saber o momento exato em que ocorreu.
     */
    LocalDateTime getOccurredOn();
}