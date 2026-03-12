package com.example.stylohub.adapter.in.web.dto;

import com.example.stylohub.domain.model.WidgetType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record AddWidgetRequest(
        @NotNull WidgetType type,
        @Min(0) int order,
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
