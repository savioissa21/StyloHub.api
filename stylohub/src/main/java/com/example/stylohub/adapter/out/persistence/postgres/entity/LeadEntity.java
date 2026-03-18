package com.example.stylohub.adapter.out.persistence.postgres.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "leads")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LeadEntity {

    @Id
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(name = "profile_id", nullable = false, updatable = false)
    private UUID profileId;

    @Column(name = "widget_id", nullable = false, updatable = false)
    private UUID widgetId;

    @Column(name = "widget_title")
    private String widgetTitle;

    @Column(name = "fields_json", columnDefinition = "jsonb", nullable = false)
    private String fieldsJson;

    @Column(name = "captured_at", nullable = false, updatable = false)
    private LocalDateTime capturedAt;
}
