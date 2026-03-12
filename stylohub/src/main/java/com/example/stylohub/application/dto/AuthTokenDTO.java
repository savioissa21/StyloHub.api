package com.example.stylohub.application.dto;

public record AuthTokenDTO(
        String accessToken,
        String tokenType,
        long expiresIn
) {
    public AuthTokenDTO(String accessToken, long expiresIn) {
        this(accessToken, "Bearer", expiresIn);
    }
}
