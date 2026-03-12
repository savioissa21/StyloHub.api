package com.example.stylohub.adapter.out.persistence.postgres;

import com.example.stylohub.adapter.out.persistence.postgres.entity.UserEntity;
import com.example.stylohub.application.port.out.UserRepositoryPort;
import com.example.stylohub.domain.model.OAuthProvider;
import com.example.stylohub.domain.model.User;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class PostgresUserAdapter implements UserRepositoryPort {

    private final SpringDataJpaUserRepository jpaRepo;

    public PostgresUserAdapter(SpringDataJpaUserRepository jpaRepo) {
        this.jpaRepo = jpaRepo;
    }

    @Override
    public User save(User user) {
        UserEntity entity = toEntity(user);
        UserEntity saved = jpaRepo.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<User> findById(UUID id) {
        return jpaRepo.findById(id).map(this::toDomain);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return jpaRepo.findByEmail(email).map(this::toDomain);
    }

    @Override
    public boolean existsByEmail(String email) {
        return jpaRepo.existsByEmail(email);
    }

    private UserEntity toEntity(User user) {
        return UserEntity.builder()
                .id(user.getId())
                .email(user.getEmail())
                .passwordHash(user.getPasswordHash())
                .provider(user.getProvider().name())
                .isActive(user.isActive())
                .build();
    }

    private User toDomain(UserEntity entity) {
        OAuthProvider provider = OAuthProvider.valueOf(entity.getProvider());
        if (provider == OAuthProvider.LOCAL) {
            return new User(entity.getId(), entity.getEmail(), entity.getPasswordHash());
        }
        User user = new User(entity.getId(), entity.getEmail(), provider);
        if (!entity.isActive()) user.deactivate();
        user.clearEvents(); // Rehydration não dispara eventos
        return user;
    }
}
