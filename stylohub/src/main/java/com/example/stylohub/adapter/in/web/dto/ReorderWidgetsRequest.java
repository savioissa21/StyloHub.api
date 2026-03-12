package com.example.stylohub.adapter.in.web.dto;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;
import java.util.UUID;

public record ReorderWidgetsRequest(
        @NotEmpty List<UUID> orderedWidgetIds
) {}
