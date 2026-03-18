package com.example.stylohub.adapter.in.web.mapper;

import com.example.stylohub.adapter.in.web.dto.*;
import com.example.stylohub.application.command.*;
import com.example.stylohub.domain.model.Profile;
import com.example.stylohub.domain.model.Widget;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WebProfileMapper {

    private final ObjectMapper objectMapper;

    public WebProfileMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    // Para a visão pública — só mostra widgets ativos
    public ProfileResponse toPublicResponse(Profile profile) {
        List<WidgetResponse> widgets = profile.getWidgets().stream()
                .filter(Widget::isActive)
                .map(this::toWidgetResponse)
                .toList();
        return buildResponse(profile, widgets);
    }

    // Para o painel do criador — mostra todos os widgets (inclusive inativos)
    public ProfileResponse toCreatorResponse(Profile profile) {
        List<WidgetResponse> widgets = profile.getWidgets().stream()
                .map(this::toWidgetResponse)
                .toList();
        return buildResponse(profile, widgets);
    }

    public AddWidgetCommand toAddCommand(AddWidgetRequest req) {
        return new AddWidgetCommand(req.type(), req.order(), req.title(), req.url(),
                req.videoId(), req.autoPlay(), req.showControls(), req.spotifyUri(),
                req.compact(), req.imageUrl(), req.altText(), req.linkUrl(),
                req.content(), req.buttonLabel(), req.successMessage(), req.formFields());
    }

    public UpdateWidgetCommand toUpdateCommand(UpdateWidgetRequest req) {
        return new UpdateWidgetCommand(req.title(), req.url(), req.videoId(),
                req.autoPlay(), req.showControls(), req.spotifyUri(), req.compact(),
                req.imageUrl(), req.altText(), req.linkUrl(), req.content(),
                req.buttonLabel(), req.successMessage(), req.formFields());
    }

    public UpdateThemeCommand toThemeCommand(UpdateThemeRequest req) {
        return new UpdateThemeCommand(req.bgType(), req.bgValue(), req.primaryColor(),
                req.textColor(), req.buttonStyle(), req.isCustom(),
                req.borderColor(), req.shadowStyle());
    }

    private ProfileResponse buildResponse(Profile profile, List<WidgetResponse> widgets) {
        ThemeResponse theme = new ThemeResponse(
                profile.getTheme().getBgType().name(),
                profile.getTheme().getBgValue(),
                profile.getTheme().getPrimaryColor(),
                profile.getTheme().getTextColor(),
                profile.getTheme().getButtonStyle().name(),
                profile.getTheme().isCustom(),
                profile.getTheme().getBorderColor(),
                profile.getTheme().getShadowStyle().name()
        );
        return new ProfileResponse(profile.getId(), profile.getUsername(), theme,
                profile.getSubscription().getPlan().name(), widgets);
    }

    private WidgetResponse toWidgetResponse(Widget widget) {
        String configJson;
        try {
            configJson = objectMapper.writeValueAsString(widget.getConfig());
        } catch (JsonProcessingException e) {
            configJson = "{}";
        }
        return new WidgetResponse(widget.getId(), widget.getConfig().getType().name(),
                widget.getOrderIndex(), widget.isActive(), configJson);
    }
}
