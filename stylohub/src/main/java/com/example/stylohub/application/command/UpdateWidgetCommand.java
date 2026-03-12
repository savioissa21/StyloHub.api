package com.example.stylohub.application.command;

import java.util.List;

public record UpdateWidgetCommand(
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
