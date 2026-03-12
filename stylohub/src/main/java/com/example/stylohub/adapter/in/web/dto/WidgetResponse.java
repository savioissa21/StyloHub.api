package com.example.stylohub.adapter.in.web.dto;

import com.fasterxml.jackson.annotation.JsonRawValue;

import java.util.UUID;

public record WidgetResponse(
        UUID id,
        String type,
        int orderIndex,
        boolean isActive,
        @JsonRawValue String config  // JSON raw do WidgetConfig
) {}
