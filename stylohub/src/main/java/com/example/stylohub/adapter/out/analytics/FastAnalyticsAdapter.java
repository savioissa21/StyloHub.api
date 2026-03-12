package com.example.stylohub.adapter.out.analytics;

import com.example.stylohub.adapter.out.persistence.postgres.entity.AnalyticsEventEntity;
import com.example.stylohub.application.dto.DashboardStatsDTO;
import com.example.stylohub.application.port.out.AnalyticsRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class FastAnalyticsAdapter implements AnalyticsRepositoryPort {

    private final SpringDataJpaAnalyticsRepository jpaRepo;

    public FastAnalyticsAdapter(SpringDataJpaAnalyticsRepository jpaRepo) {
        this.jpaRepo = jpaRepo;
    }

    @Override
    public void recordProfileView(UUID profileId) {
        jpaRepo.save(AnalyticsEventEntity.builder()
                .profileId(profileId)
                .eventType("PROFILE_VIEW")
                .build());
    }

    @Override
    public void recordWidgetClick(UUID profileId, UUID widgetId) {
        jpaRepo.save(AnalyticsEventEntity.builder()
                .profileId(profileId)
                .widgetId(widgetId)
                .eventType("WIDGET_CLICK")
                .build());
    }

    @Override
    public DashboardStatsDTO getStats(UUID profileId) {
        long totalViews = jpaRepo.countByProfileIdAndEventType(profileId, "PROFILE_VIEW");
        long totalClicks = jpaRepo.countByProfileIdAndEventType(profileId, "WIDGET_CLICK");
        double ctr = totalViews > 0 ? (double) totalClicks / totalViews * 100.0 : 0.0;

        Map<UUID, Long> clicksPerWidget = new HashMap<>();
        jpaRepo.countClicksGroupedByWidget(profileId).forEach(row -> {
            clicksPerWidget.put((UUID) row[0], (Long) row[1]);
        });

        return new DashboardStatsDTO(profileId, totalViews, totalClicks, ctr, clicksPerWidget);
    }
}
