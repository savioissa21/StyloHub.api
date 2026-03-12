package com.example.stylohub.application.command;

import com.example.stylohub.domain.model.PlanType;

import java.util.UUID;

public record CreateProfileCommand(
        UUID userId,
        String username,
        PlanType plan
) {}
