package com.example.stylohub.adapter.out.persistence.postgres.mapper;

import com.example.stylohub.adapter.out.persistence.postgres.entity.ProfileEntity;
import com.example.stylohub.adapter.out.persistence.postgres.entity.WidgetEntity;
import com.example.stylohub.domain.model.*;
import com.example.stylohub.domain.model.config.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JpaProfileMapper {

    private final ObjectMapper objectMapper;

    public JpaProfileMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public ProfileEntity toEntity(Profile profile) {
        ProfileEntity entity = ProfileEntity.builder()
                .id(profile.getId())
                .userId(profile.getUserId())
                .username(profile.getUsername())
                .avatarUrl(profile.getAvatarUrl())
                .bgType(profile.getTheme().getBgType().name())
                .bgValue(profile.getTheme().getBgValue())
                .primaryColor(profile.getTheme().getPrimaryColor())
                .textColor(profile.getTheme().getTextColor())
                .buttonStyle(profile.getTheme().getButtonStyle().name())
                .isCustomTheme(profile.getTheme().isCustom())
                .borderColor(profile.getTheme().getBorderColor())
                .shadowStyle(profile.getTheme().getShadowStyle().name())
                .planType(profile.getSubscription().getPlan().name())
                .build();

        List<WidgetEntity> widgetEntities = profile.getWidgets().stream()
                .map(w -> toWidgetEntity(w, entity))
                .toList();
        entity.setWidgets(new ArrayList<>(widgetEntities));

        return entity;
    }

    public Profile toDomain(ProfileEntity entity) {
        Theme theme = new Theme(
                BackgroundType.valueOf(entity.getBgType()),
                entity.getBgValue(),
                entity.getPrimaryColor(),
                entity.getTextColor(),
                ButtonStyle.valueOf(entity.getButtonStyle()),
                entity.isCustomTheme(),
                entity.getBorderColor() != null ? entity.getBorderColor() : "#D4AF37",
                entity.getShadowStyle() != null ? ShadowStyle.valueOf(entity.getShadowStyle()) : ShadowStyle.NONE
        );

        Subscription subscription = new Subscription(PlanType.valueOf(entity.getPlanType()));

        List<Widget> widgets = entity.getWidgets().stream()
                .map(this::toWidgetDomain)
                .toList();

        return Profile.reconstitute(
                entity.getId(), entity.getUserId(), entity.getUsername(),
                entity.getAvatarUrl(), theme, subscription, widgets
        );
    }

    private WidgetEntity toWidgetEntity(Widget widget, ProfileEntity profileEntity) {
        return WidgetEntity.builder()
                .id(widget.getId())
                .profile(profileEntity)
                .widgetType(widget.getConfig().getType().name())
                .configJson(serializeConfig(widget.getConfig()))
                .orderIndex(widget.getOrderIndex())
                .isActive(widget.isActive())
                .build();
    }

    private Widget toWidgetDomain(WidgetEntity entity) {
        WidgetConfig config = deserializeConfig(entity.getWidgetType(), entity.getConfigJson());
        return Widget.reconstitute(entity.getId(), config, entity.getOrderIndex(), entity.isActive());
    }

    private String serializeConfig(WidgetConfig config) {
        try {
            return objectMapper.writeValueAsString(config);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Falha ao serializar WidgetConfig: " + e.getMessage(), e);
        }
    }

    private WidgetConfig deserializeConfig(String widgetType, String json) {
        try {
            Class<? extends WidgetConfig> configClass = switch (WidgetType.valueOf(widgetType)) {
                case LINK -> LinkConfig.class;
                case YOUTUBE -> YoutubeConfig.class;
                case SPOTIFY -> SpotifyConfig.class;
                case IMAGE -> ImageConfig.class;
                case TEXT -> TextConfig.class;
                case LEAD_FORM -> LeadFormConfig.class;
            };
            return objectMapper.readValue(json, configClass);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Falha ao deserializar WidgetConfig tipo " + widgetType, e);
        }
    }
}
