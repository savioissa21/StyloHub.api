package com.example.stylohub.application.port.out;

import com.example.stylohub.domain.model.Lead;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LeadRepositoryPort {
    Lead save(Lead lead);
    List<Lead> findByProfileId(UUID profileId);
    Optional<Lead> findById(UUID leadId);
    void deleteById(UUID leadId);
}
