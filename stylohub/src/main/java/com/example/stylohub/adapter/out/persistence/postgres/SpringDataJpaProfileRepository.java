package com.example.stylohub.adapter.out.persistence.postgres;

import com.example.stylohub.adapter.out.persistence.postgres.entity.ProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface SpringDataJpaProfileRepository extends JpaRepository<ProfileEntity, UUID> {

    Optional<ProfileEntity> findByUsername(String username);

    Optional<ProfileEntity> findByUserId(UUID userId);

    boolean existsByUsername(String username);

    @Query("SELECT p FROM ProfileEntity p LEFT JOIN FETCH p.widgets WHERE p.id = :id")
    Optional<ProfileEntity> findByIdWithWidgets(UUID id);
}
