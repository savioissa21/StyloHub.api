package com.example.stylohub.application.service;

import com.example.stylohub.adapter.out.persistence.postgres.SpringDataJpaPasswordResetRepository;
import com.example.stylohub.adapter.out.persistence.postgres.entity.PasswordResetTokenEntity;
import com.example.stylohub.application.port.out.EmailPort;
import com.example.stylohub.application.port.out.UserRepositoryPort;
import com.example.stylohub.domain.exception.BusinessRuleViolationException;
import com.example.stylohub.domain.exception.DomainValidationException;
import com.example.stylohub.domain.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.OffsetDateTime;
import java.util.Base64;
import java.util.UUID;

@Service
@Transactional
public class PasswordResetService {

    private static final int TOKEN_EXPIRY_MINUTES = 30;
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private final UserRepositoryPort userRepo;
    private final SpringDataJpaPasswordResetRepository tokenRepo;
    private final EmailPort emailPort;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.frontend.url}")
    private String frontendUrl;

    public PasswordResetService(UserRepositoryPort userRepo,
                                SpringDataJpaPasswordResetRepository tokenRepo,
                                EmailPort emailPort,
                                PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.tokenRepo = tokenRepo;
        this.emailPort = emailPort;
        this.passwordEncoder = passwordEncoder;
    }

    public void requestReset(String email) {
        // Silencioso mesmo se o email não existir — previne enumeração de contas
        userRepo.findByEmail(email.toLowerCase()).ifPresent(user -> {
            tokenRepo.deleteAllByUserId(user.getId());

            String rawToken = generateSecureToken();
            tokenRepo.save(PasswordResetTokenEntity.builder()
                    .id(UUID.randomUUID())
                    .userId(user.getId())
                    .token(rawToken)
                    .expiresAt(OffsetDateTime.now().plusMinutes(TOKEN_EXPIRY_MINUTES))
                    .used(false)
                    .build());

            String resetLink = frontendUrl + "/reset-password?token=" + rawToken;
            emailPort.sendPasswordReset(email, resetLink);
        });
    }

    public void resetPassword(String token, String newPassword) {
        PasswordResetTokenEntity entity = tokenRepo.findByToken(token)
                .orElseThrow(() -> new DomainValidationException("Token de redefinição inválido."));

        if (entity.isUsed()) {
            throw new BusinessRuleViolationException("Este token já foi utilizado.");
        }
        if (OffsetDateTime.now().isAfter(entity.getExpiresAt())) {
            throw new BusinessRuleViolationException("Token expirado. Solicita um novo.");
        }
        if (newPassword == null || newPassword.length() < 8) {
            throw new DomainValidationException("A nova senha deve ter no mínimo 8 caracteres.");
        }

        User user = userRepo.findById(entity.getUserId())
                .orElseThrow(() -> new DomainValidationException("Utilizador não encontrado."));

        user.changePassword(passwordEncoder.encode(newPassword));
        userRepo.save(user);

        entity.setUsed(true);
        tokenRepo.save(entity);
    }

    private String generateSecureToken() {
        byte[] bytes = new byte[48];
        SECURE_RANDOM.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
}
