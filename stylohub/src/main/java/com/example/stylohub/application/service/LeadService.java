package com.example.stylohub.application.service;

import com.example.stylohub.application.port.in.ManageLeadsUseCase;
import com.example.stylohub.application.port.out.LeadRepositoryPort;
import com.example.stylohub.application.port.out.ProfileRepositoryPort;
import com.example.stylohub.domain.exception.BusinessRuleViolationException;
import com.example.stylohub.domain.exception.DomainValidationException;
import com.example.stylohub.domain.exception.ResourceNotFoundException;
import com.example.stylohub.domain.model.Lead;
import com.example.stylohub.domain.model.Profile;
import com.example.stylohub.domain.model.Widget;
import com.example.stylohub.domain.model.config.LeadFormConfig;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Transactional
public class LeadService implements ManageLeadsUseCase {

    private final ProfileRepositoryPort profileRepo;
    private final LeadRepositoryPort leadRepo;

    public LeadService(ProfileRepositoryPort profileRepo, LeadRepositoryPort leadRepo) {
        this.profileRepo = profileRepo;
        this.leadRepo = leadRepo;
    }

    @Override
    public Lead captureLead(String username, UUID widgetId, Map<String, String> fields) {
        Profile profile = profileRepo.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Perfil", username));

        Widget widget = profile.getWidgets().stream()
                .filter(w -> w.getId().equals(widgetId) && w.isActive())
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Widget", widgetId));

        if (!(widget.getConfig() instanceof LeadFormConfig config)) {
            throw new BusinessRuleViolationException("Este widget não é um formulário de lead.");
        }

        // Valida que o campo de e-mail foi preenchido
        boolean hasEmail = fields.entrySet().stream()
                .anyMatch(e -> (e.getKey().toLowerCase().contains("email")
                             || e.getKey().toLowerCase().contains("e-mail"))
                           && e.getValue() != null && !e.getValue().isBlank());

        if (!hasEmail) {
            throw new DomainValidationException("O campo de e-mail é obrigatório.");
        }

        Lead lead = new Lead(
                UUID.randomUUID(),
                profile.getId(),
                widgetId,
                config.getTitle(),
                fields,
                LocalDateTime.now()
        );

        return leadRepo.save(lead);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Lead> getLeadsByProfileId(UUID profileId) {
        return leadRepo.findByProfileId(profileId);
    }

    @Override
    public void deleteLead(UUID profileId, UUID leadId) {
        Lead lead = leadRepo.findById(leadId)
                .orElseThrow(() -> new ResourceNotFoundException("Lead", leadId));

        if (!lead.getProfileId().equals(profileId)) {
            throw new BusinessRuleViolationException("Você não tem permissão para remover este lead.");
        }

        leadRepo.deleteById(leadId);
    }
}
