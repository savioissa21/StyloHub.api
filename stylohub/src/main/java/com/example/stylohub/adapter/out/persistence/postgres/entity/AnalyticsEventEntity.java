package com.example.stylohub.adapter.out.persistence.postgres.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "analytics_events",
       indexes = {
           @Index(name = "idx_analytics_profile_id", columnList = "profile_id"),
           @Index(name = "idx_analytics_created_at", columnList = "created_at")
       })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnalyticsEventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "profile_id", nullable = false)
    private UUID profileId;

    @Column(name = "widget_id")
    private UUID widgetId; // null = visualização do perfil, preenchido = clique em widget

    @Column(name = "event_type", nullable = false, length = 20)
    private String eventType; // "PROFILE_VIEW" ou "WIDGET_CLICK"

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
