package com.example.stylohub.domain.model;

/**
 * Define a origem do registo/login do utilizador.
 */
public enum OAuthProvider {
    LOCAL,  // Email e senha tradicionais
    GOOGLE,
    APPLE
}