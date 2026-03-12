package com.example.stylohub.domain.model;

import com.example.stylohub.domain.event.UserCreatedEvent;
import com.example.stylohub.domain.event.UserDeactivatedEvent;
import com.example.stylohub.domain.exception.BusinessRuleViolationException;
import com.example.stylohub.domain.exception.DomainValidationException;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.regex.Pattern;

public class User extends AggregateRoot {

    private final UUID id;
    private String email;
    private String passwordHash;
    private final OAuthProvider provider;
    private boolean isActive;

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

    public User(UUID id, String email, String passwordHash) {
        validateEmail(email);
        if (passwordHash == null || passwordHash.isBlank()) {
            throw new DomainValidationException("A hash da senha é obrigatória para registos locais.");
        }
        this.id = id;
        this.email = email.toLowerCase();
        this.passwordHash = passwordHash;
        this.provider = OAuthProvider.LOCAL;
        this.isActive = true;
        this.registerEvent(new UserCreatedEvent(this.id, this.email, OAuthProvider.LOCAL, LocalDateTime.now()));
    }

    public User(UUID id, String email, OAuthProvider provider) {
        validateEmail(email);
        if (provider == null || provider == OAuthProvider.LOCAL) {
            throw new DomainValidationException("Provedor OAuth inválido para este tipo de registo.");
        }
        this.id = id;
        this.email = email.toLowerCase();
        this.passwordHash = null;
        this.provider = provider;
        this.isActive = true;
        this.registerEvent(new UserCreatedEvent(this.id, this.email, provider, LocalDateTime.now()));
    }

    public void changeEmail(String newEmail) {
        validateEmail(newEmail);
        this.email = newEmail.toLowerCase();
    }

    public void changePassword(String newPasswordHash) {
        if (this.provider != OAuthProvider.LOCAL) {
            throw new BusinessRuleViolationException(
                "Contas criadas via " + this.provider + " não podem alterar a senha diretamente."
            );
        }
        if (newPasswordHash == null || newPasswordHash.isBlank()) {
            throw new DomainValidationException("A nova hash de senha é obrigatória.");
        }
        this.passwordHash = newPasswordHash;
    }

    public void deactivate() {
        if (!this.isActive) {
            throw new BusinessRuleViolationException("A conta já está desativada.");
        }
        this.isActive = false;
        this.registerEvent(new UserDeactivatedEvent(this.id, this.email, LocalDateTime.now()));
    }

    private void validateEmail(String emailToValidate) {
        if (emailToValidate == null || emailToValidate.isBlank()) {
            throw new DomainValidationException("O e-mail não pode estar vazio.");
        }
        if (!EMAIL_PATTERN.matcher(emailToValidate).matches()) {
            throw new DomainValidationException("Formato de e-mail inválido: " + emailToValidate);
        }
    }

    public UUID getId() { return id; }
    public String getEmail() { return email; }
    public String getPasswordHash() { return passwordHash; }
    public OAuthProvider getProvider() { return provider; }
    public boolean isActive() { return isActive; }
}
