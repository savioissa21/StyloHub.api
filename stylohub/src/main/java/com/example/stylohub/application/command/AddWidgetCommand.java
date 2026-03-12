package com.example.stylohub.application.command;

import com.example.stylohub.domain.model.WidgetType;

import java.util.List;
import java.util.Map;

public record AddWidgetCommand(
        WidgetType type,
        int order,
        // Campos genéricos — o ProfileService mapeia para o WidgetConfig correto
        String title,
        String url,
        String videoId,
        Boolean autoPlay,
        Boolean showControls,
        String spotifyUri,
        Boolean compact,
        String imageUrl,
        String altText,
        String linkUrl,
        String content,
        String buttonLabel,
        String successMessage,
        List<String> formFields
) {}
