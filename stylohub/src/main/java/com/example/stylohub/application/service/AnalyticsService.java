package com.example.stylohub.application.service;

import com.example.stylohub.application.dto.DashboardStatsDTO;
import com.example.stylohub.application.port.in.TrackAnalyticsUseCase;
import com.example.stylohub.application.port.out.AnalyticsRepositoryPort;
import com.example.stylohub.application.port.out.ProfileRepositoryPort;
import com.example.stylohub.domain.exception.BusinessRuleViolationException;
import com.example.stylohub.domain.exception.ResourceNotFoundException;
import com.example.stylohub.domain.model.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class AnalyticsService implements TrackAnalyticsUseCase {

    private final AnalyticsRepositoryPort analyticsRepo;
    private final ProfileRepositoryPort profileRepo;

    public AnalyticsService(AnalyticsRepositoryPort analyticsRepo, ProfileRepositoryPort profileRepo) {
        this.analyticsRepo = analyticsRepo;
        this.profileRepo = profileRepo;
    }

    @Override
    public void recordProfileView(UUID profileId) {
        analyticsRepo.recordProfileView(profileId);
    }

    @Override
    public void recordWidgetClick(UUID profileId, UUID widgetId) {
        analyticsRepo.recordWidgetClick(profileId, widgetId);
    }

    @Override
    @Transactional(readOnly = true)
    public DashboardStatsDTO getDashboardStats(UUID profileId) {
        Profile profile = profileRepo.findById(profileId)
                .orElseThrow(() -> new ResourceNotFoundException("Profile", profileId));

        if (!profile.getSubscription().canAccessAnalytics()) {
            throw new BusinessRuleViolationException(
                "Analytics avançado está disponível apenas no plano PRO."
            );
        }

        return analyticsRepo.getStats(profileId);
    }
}
