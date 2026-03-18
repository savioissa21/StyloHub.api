package com.example.stylohub.adapter.in.web.dto;

public record ThemeResponse(
        String bgType,
        String bgValue,
        String primaryColor,
        String textColor,
        String buttonStyle,
        boolean isCustom,
        String borderColor,
        String shadowStyle
) {}
