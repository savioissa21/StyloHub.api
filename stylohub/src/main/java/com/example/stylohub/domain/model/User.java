package com.example.stylohub.domain.model;

import java.util.UUID;
import java.util.regex.Pattern;

/**
 * Aggregate Root responsável pela identidade, segurança e acesso do criador à plataforma.
 */
public class User extends AggregateRoot {

    private final UUID id;
    private String email;
    private String passwordHash; // O domínio NUNCA deve conhecer senhas em texto limpo
    private final OAuthProvider provider;
    private boolean isActive;

    // Regex simples para validar o formato do e-mail
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

    /**
     * Construtor para utilizadores tradicionais (Email e Senha).
     */
    public User(UUID id, String email, String passwordHash) {
        this.validateEmail(email);
        if (passwordHash == null || passwordHash.isBlank()) {
            throw new IllegalArgumentException("A hash da senha é obrigatória para registos locais.");
        }
        
        this.id = id;
        this.email = email.toLowerCase();
        this.passwordHash = passwordHash;
        this.provider = OAuthProvider.LOCAL;
        this.isActive = true;
    }

    /**
     * Construtor para utilizadores de Login Social (Google/Apple).
     */
    public User(UUID id, String email, OAuthProvider provider) {
        this.validateEmail(email);
        if (provider == null || provider == OAuthProvider.LOCAL) {
            throw new IllegalArgumentException("Provedor OAuth inválido para este tipo de registo.");
        }

        this.id = id;
        this.email = email.toLowerCase();
        this.passwordHash = null; // Logins sociais não têm senha na nossa base de dados
        this.provider = provider;
        this.isActive = true;
    }

    /**
     * Regra de negócio: Atualização de e-mail.
     */
    public void changeEmail(String newEmail) {
        this.validateEmail(newEmail);
        this.email = newEmail.toLowerCase();
        // Aqui poderíamos disparar um evento: this.registerEvent(new EmailChangedEvent(...));
    }

    /**
     * Regra de negócio: Alteração de senha.
     */
    public void changePassword(String newPasswordHash) {
        if (this.provider != OAuthProvider.LOCAL) {
            throw new IllegalStateException("Contas criadas via " + this.provider + " não podem alterar a senha diretamente.");
        }
        if (newPasswordHash == null || newPasswordHash.isBlank()) {
            throw new IllegalArgumentException("A nova hash de senha é obrigatória.");
        }
        this.passwordHash = newPasswordHash;
    }

    /**
     * Desativa a conta do utilizador (Soft Delete).
     */
    public void deactivate() {
        this.isActive = false;
        // Disparar evento para cancelar a Subscription e desativar o Profile associado!
    }

    private void validateEmail(String emailToValidate) {
        if (emailToValidate == null || emailToValidate.isBlank()) {
            throw new IllegalArgumentException("O e-mail não pode estar vazio.");
        }
        if (!EMAIL_PATTERN.matcher(emailToValidate).matches()) {
            throw new IllegalArgumentException("Formato de e-mail inválido.");
        }
    }

    // --- GETTERS ---
    
    public UUID getId() { return id; }
    public String getEmail() { return email; }
    public String getPasswordHash() { return passwordHash; }
    public OAuthProvider getProvider() { return provider; }
    public boolean isActive() { return isActive; }
}