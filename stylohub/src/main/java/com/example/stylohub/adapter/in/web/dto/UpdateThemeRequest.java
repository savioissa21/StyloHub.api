package com.example.stylohub.adapter.in.web.dto;

import com.example.stylohub.domain.model.BackgroundType;
import com.example.stylohub.domain.model.ButtonStyle;
import com.example.stylohub.domain.model.ShadowStyle;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record UpdateThemeRequest(
        @NotNull BackgroundType bgType,
        @NotBlank String bgValue,

        @NotBlank
        @Pattern(regexp = "^#([A-Fa-f0-9]{6})$", message = "Cor deve estar no formato hexadecimal #RRGGBB")
        String primaryColor,

        @NotBlank
        @Pattern(regexp = "^#([A-Fa-f0-9]{6})$", message = "Cor deve estar no formato hexadecimal #RRGGBB")
        String textColor,

        @NotNull ButtonStyle buttonStyle,
        boolean isCustom,

        @NotBlank
        @Pattern(regexp = "^#([A-Fa-f0-9]{6})$", message = "Cor deve estar no formato hexadecimal #RRGGBB")
        String borderColor,

        @NotNull ShadowStyle shadowStyle
) {}
