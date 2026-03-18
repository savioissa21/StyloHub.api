package com.example.stylohub.adapter.in.web.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Map;
import java.util.UUID;

public record SubmitLeadRequest(
        @NotNull UUID widgetId,
        @NotEmpty Map<String, String> fields
) {}
