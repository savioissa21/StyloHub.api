package com.example.stylohub.adapter.in.web.dto;

import java.util.List;

public record UpdateWidgetRequest(
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
