package com.example.stylohub.adapter.out.persistence.postgres.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileEntity {

    @Id
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(name = "user_id", nullable = false, updatable = false)
    private UUID userId;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    // Theme fields (Value Object achatado em colunas)
    @Column(name = "bg_type", nullable = false, length = 20)
    private String bgType;

    @Column(name = "bg_value", nullable = false)
    private String bgValue;

    @Column(name = "primary_color", nullable = false, length = 7)
    private String primaryColor;

    @Column(name = "text_color", nullable = false, length = 7)
    private String textColor;

    @Column(name = "button_style", nullable = false, length = 20)
    private String buttonStyle;

    @Column(name = "is_custom_theme", nullable = false)
    private boolean isCustomTheme;

    // Subscription
    @Column(name = "plan_type", nullable = false, length = 20)
    private String planType;

    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy("order_index ASC")
    @Builder.Default
    private List<WidgetEntity> widgets = new ArrayList<>();
}
