package com.example.stylohub.adapter.in.web.controller;

import com.example.stylohub.adapter.in.web.dto.LoginRequest;
import com.example.stylohub.adapter.in.web.dto.RegisterRequest;
import com.example.stylohub.application.command.RegisterUserCommand;
import com.example.stylohub.application.dto.AuthTokenDTO;
import com.example.stylohub.application.port.in.AuthUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthUseCase authUseCase;

    public AuthController(AuthUseCase authUseCase) {
        this.authUseCase = authUseCase;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    AuthTokenDTO register(@Valid @RequestBody RegisterRequest request) {
        return authUseCase.register(new RegisterUserCommand(
                request.email(),
                request.password(),
                request.username()
        ));
    }

    @PostMapping("/login")
    AuthTokenDTO login(@Valid @RequestBody LoginRequest request) {
        return authUseCase.login(request.email(), request.password());
    }
}
