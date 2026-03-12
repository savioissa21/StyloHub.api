package com.example.stylohub.application.port.out;

import com.example.stylohub.domain.event.DomainEvent;

import java.util.List;

public interface EventPublisherPort {
    void publishAll(List<DomainEvent> events);
}
