package com.example.stylohub.adapter.out.persistence.postgres;

import com.example.stylohub.adapter.out.persistence.postgres.mapper.JpaProfileMapper;
import com.example.stylohub.application.port.out.ProfileRepositoryPort;
import com.example.stylohub.domain.model.Profile;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class PostgresProfileAdapter implements ProfileRepositoryPort {

    private final SpringDataJpaProfileRepository jpaRepo;
    private final JpaProfileMapper mapper;

    public PostgresProfileAdapter(SpringDataJpaProfileRepository jpaRepo, JpaProfileMapper mapper) {
        this.jpaRepo = jpaRepo;
        this.mapper = mapper;
    }

    @Override
    public Profile save(Profile profile) {
        var entity = mapper.toEntity(profile);
        var saved = jpaRepo.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Profile> findById(UUID id) {
        return jpaRepo.findByIdWithWidgets(id).map(mapper::toDomain);
    }

    @Override
    public Optional<Profile> findByUsername(String username) {
        return jpaRepo.findByUsername(username).map(mapper::toDomain);
    }

    @Override
    public Optional<Profile> findByUserId(UUID userId) {
        return jpaRepo.findByUserId(userId).map(mapper::toDomain);
    }

    @Override
    public boolean existsByUsername(String username) {
        return jpaRepo.existsByUsername(username);
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepo.deleteById(id);
    }
}
