package com.example.stylohub.adapter.in.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank @Email
        String email,

        @NotBlank @Size(min = 8, message = "A senha deve ter no mínimo 8 caracteres")
        String password,

        @NotBlank
        @Size(min = 3, max = 50, message = "O username deve ter entre 3 e 50 caracteres")
        @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "Username só pode conter letras, números e _")
        String username
) {}
