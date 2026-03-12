package com.example.stylohub.adapter.out.persistence.postgres;

import com.example.stylohub.adapter.out.persistence.postgres.entity.PasswordResetTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface SpringDataJpaPasswordResetRepository extends JpaRepository<PasswordResetTokenEntity, UUID> {

    Optional<PasswordResetTokenEntity> findByToken(String token);

    @Modifying
    @Query("DELETE FROM PasswordResetTokenEntity t WHERE t.userId = :userId")
    void deleteAllByUserId(UUID userId);
}
