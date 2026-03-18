package com.example.stylohub.adapter.out.persistence.postgres;

import com.example.stylohub.adapter.out.persistence.postgres.entity.LeadEntity;
import com.example.stylohub.application.port.out.LeadRepositoryPort;
import com.example.stylohub.domain.exception.ResourceNotFoundException;
import com.example.stylohub.domain.model.Lead;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Component
public class JpaLeadRepositoryAdapter implements LeadRepositoryPort {

    private final SpringDataJpaLeadRepository jpaRepo;
    private final ObjectMapper objectMapper;

    public JpaLeadRepositoryAdapter(SpringDataJpaLeadRepository jpaRepo, ObjectMapper objectMapper) {
        this.jpaRepo = jpaRepo;
        this.objectMapper = objectMapper;
    }

    @Override
    public Lead save(Lead lead) {
        LeadEntity entity = LeadEntity.builder()
                .id(lead.getId())
                .profileId(lead.getProfileId())
                .widgetId(lead.getWidgetId())
                .widgetTitle(lead.getWidgetTitle())
                .fieldsJson(serializeFields(lead.getFields()))
                .capturedAt(lead.getCapturedAt())
                .build();
        jpaRepo.save(entity);
        return lead;
    }

    @Override
    public List<Lead> findByProfileId(UUID profileId) {
        return jpaRepo.findByProfileIdOrderByCapturedAtDesc(profileId)
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public Optional<Lead> findById(UUID leadId) {
        return jpaRepo.findById(leadId).map(this::toDomain);
    }

    @Override
    public void deleteById(UUID leadId) {
        if (!jpaRepo.existsById(leadId)) {
            throw new ResourceNotFoundException("Lead", leadId);
        }
        jpaRepo.deleteById(leadId);
    }

    // ── Helpers ──────────────────────────────────────────────────────────────

    private Lead toDomain(LeadEntity e) {
        return new Lead(e.getId(), e.getProfileId(), e.getWidgetId(),
                e.getWidgetTitle(), deserializeFields(e.getFieldsJson()), e.getCapturedAt());
    }

    private String serializeFields(Map<String, String> fields) {
        try {
            return objectMapper.writeValueAsString(fields);
        } catch (JsonProcessingException ex) {
            throw new IllegalStateException("Falha ao serializar campos do lead", ex);
        }
    }

    private Map<String, String> deserializeFields(String json) {
        try {
            return objectMapper.readValue(json, new TypeReference<>() {});
        } catch (JsonProcessingException ex) {
            return Map.of();
        }
    }
}
