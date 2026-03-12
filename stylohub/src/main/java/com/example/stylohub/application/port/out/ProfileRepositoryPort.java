package com.example.stylohub.application.port.out;

import com.example.stylohub.domain.model.Profile;

import java.util.Optional;
import java.util.UUID;

public interface ProfileRepositoryPort {
    Profile save(Profile profile);
    Optional<Profile> findById(UUID id);
    Optional<Profile> findByUsername(String username);
    Optional<Profile> findByUserId(UUID userId);
    boolean existsByUsername(String username);
    void deleteById(UUID id);
}
