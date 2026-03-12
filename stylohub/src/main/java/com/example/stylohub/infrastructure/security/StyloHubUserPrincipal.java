package com.example.stylohub.infrastructure.security;

import java.util.UUID;

public record StyloHubUserPrincipal(String userId, String email) {

    public UUID getUserIdAsUUID() {
        return UUID.fromString(userId);
    }
}
