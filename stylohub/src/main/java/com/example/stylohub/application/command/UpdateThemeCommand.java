package com.example.stylohub.application.command;

import com.example.stylohub.domain.model.BackgroundType;
import com.example.stylohub.domain.model.ButtonStyle;
import com.example.stylohub.domain.model.ShadowStyle;

public record UpdateThemeCommand(
        BackgroundType bgType,
        String bgValue,
        String primaryColor,
        String textColor,
        ButtonStyle buttonStyle,
        boolean isCustom,
        String borderColor,
        ShadowStyle shadowStyle
) {}
