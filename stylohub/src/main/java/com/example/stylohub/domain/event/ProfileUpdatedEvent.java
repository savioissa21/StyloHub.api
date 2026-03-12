package com.example.stylohub.domain.event;

import java.time.LocalDateTime;
import java.util.UUID;

public class ProfileUpdatedEvent implements DomainEvent {
    
    private final UUID profileId;
    private final String username;
    private final LocalDateTime occurredOn;

    public ProfileUpdatedEvent(UUID profileId, String username, LocalDateTime occurredOn) {
        this.profileId = profileId;
        this.username = username;
        this.occurredOn = occurredOn;
    }

    public UUID getProfileId() {
        return profileId;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public LocalDateTime getOccurredOn() {
        return occurredOn;
    }
}