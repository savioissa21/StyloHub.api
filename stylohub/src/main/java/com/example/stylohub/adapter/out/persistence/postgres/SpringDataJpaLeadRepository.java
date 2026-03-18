package com.example.stylohub.adapter.out.persistence.postgres;

import com.example.stylohub.adapter.out.persistence.postgres.entity.LeadEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SpringDataJpaLeadRepository extends JpaRepository<LeadEntity, UUID> {
    List<LeadEntity> findByProfileIdOrderByCapturedAtDesc(UUID profileId);
}
