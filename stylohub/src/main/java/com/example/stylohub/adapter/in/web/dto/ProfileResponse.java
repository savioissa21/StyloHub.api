package com.example.stylohub.adapter.in.web.dto;

import java.util.List;
import java.util.UUID;

public record ProfileResponse(
        UUID id,
        String username,
        ThemeResponse theme,
        String plan,
        List<WidgetResponse> widgets
) {}
