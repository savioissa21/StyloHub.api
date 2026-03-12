package com.example.stylohub.application.service;

import com.example.stylohub.application.command.CreateProfileCommand;
import com.example.stylohub.application.command.RegisterUserCommand;
import com.example.stylohub.application.dto.AuthTokenDTO;
import com.example.stylohub.application.port.in.AuthUseCase;
import com.example.stylohub.application.port.out.EventPublisherPort;
import com.example.stylohub.application.port.out.UserRepositoryPort;
import com.example.stylohub.domain.exception.BusinessRuleViolationException;
import com.example.stylohub.domain.exception.DomainValidationException;
import com.example.stylohub.domain.model.OAuthProvider;
import com.example.stylohub.domain.model.PlanType;
import com.example.stylohub.domain.model.User;
import com.example.stylohub.infrastructure.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class AuthService implements AuthUseCase {

    private final UserRepositoryPort userRepo;
    private final ProfileService profileService;
    private final EventPublisherPort eventPublisher;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepositoryPort userRepo,
                       ProfileService profileService,
                       EventPublisherPort eventPublisher,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService) {
        this.userRepo = userRepo;
        this.profileService = profileService;
        this.eventPublisher = eventPublisher;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Override
    public AuthTokenDTO register(RegisterUserCommand command) {
        if (userRepo.existsByEmail(command.email())) {
            throw new BusinessRuleViolationException(
                "Já existe uma conta com o e-mail: " + command.email()
            );
        }

        String passwordHash = passwordEncoder.encode(command.password());
        User user = new User(UUID.randomUUID(), command.email(), passwordHash);
        User saved = userRepo.save(user);

        // Cria o perfil associado automaticamente
        profileService.createProfile(new CreateProfileCommand(
                saved.getId(),
                command.username(),
                PlanType.FREE
        ));

        eventPublisher.publishAll(saved.getDomainEvents());
        saved.clearEvents();

        String token = jwtService.generateToken(saved.getId().toString(), saved.getEmail());
        return new AuthTokenDTO(token, jwtService.getExpirationMs());
    }

    @Override
    public AuthTokenDTO login(String email, String password) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new DomainValidationException("Credenciais inválidas."));

        if (!user.isActive()) {
            throw new BusinessRuleViolationException("Esta conta foi desativada.");
        }
        if (user.getProvider() != OAuthProvider.LOCAL) {
            throw new BusinessRuleViolationException(
                "Esta conta usa login via " + user.getProvider() + ". Use o provedor correto."
            );
        }
        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new DomainValidationException("Credenciais inválidas.");
        }

        String token = jwtService.generateToken(user.getId().toString(), user.getEmail());
        return new AuthTokenDTO(token, jwtService.getExpirationMs());
    }

    @Override
    public AuthTokenDTO loginWithOAuth(String email, OAuthProvider provider) {
        User user = userRepo.findByEmail(email).orElseGet(() -> {
            User newUser = new User(UUID.randomUUID(), email, provider);
            User saved = userRepo.save(newUser);
            profileService.createProfile(new CreateProfileCommand(
                    saved.getId(),
                    generateUsernameFromEmail(email),
                    PlanType.FREE
            ));
            eventPublisher.publishAll(saved.getDomainEvents());
            saved.clearEvents();
            return saved;
        });

        if (!user.isActive()) {
            throw new BusinessRuleViolationException("Esta conta foi desativada.");
        }

        String token = jwtService.generateToken(user.getId().toString(), user.getEmail());
        return new AuthTokenDTO(token, jwtService.getExpirationMs());
    }

    private String generateUsernameFromEmail(String email) {
        return email.split("@")[0].replaceAll("[^a-zA-Z0-9_]", "_") + "_" +
               UUID.randomUUID().toString().substring(0, 6);
    }
}
