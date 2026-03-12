package com.example.stylohub.application.command;

public record RegisterUserCommand(
        String email,
        String password,
        String username
) {}
