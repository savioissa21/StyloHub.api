package com.example.stylohub.application.command;

import com.example.stylohub.domain.model.BackgroundType;
import com.example.stylohub.domain.model.ButtonStyle;

public record UpdateThemeCommand(
        BackgroundType bgType,
        String bgValue,
        String primaryColor,
        String textColor,
        ButtonStyle buttonStyle,
        boolean isCustom
) {}
