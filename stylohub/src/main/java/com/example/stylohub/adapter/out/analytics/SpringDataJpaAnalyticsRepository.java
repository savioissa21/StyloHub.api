package com.example.stylohub.adapter.out.analytics;

import com.example.stylohub.adapter.out.persistence.postgres.entity.AnalyticsEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface SpringDataJpaAnalyticsRepository extends JpaRepository<AnalyticsEventEntity, Long> {

    long countByProfileIdAndEventType(UUID profileId, String eventType);

    long countByProfileIdAndWidgetIdAndEventType(UUID profileId, UUID widgetId, String eventType);

    @Query("SELECT a.widgetId, COUNT(a) FROM AnalyticsEventEntity a " +
           "WHERE a.profileId = :profileId AND a.eventType = 'WIDGET_CLICK' AND a.widgetId IS NOT NULL " +
           "GROUP BY a.widgetId")
    List<Object[]> countClicksGroupedByWidget(UUID profileId);
}
