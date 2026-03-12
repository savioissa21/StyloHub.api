package com.example.stylohub.application.port.in;

import com.example.stylohub.application.command.RegisterUserCommand;
import com.example.stylohub.application.dto.AuthTokenDTO;
import com.example.stylohub.domain.model.OAuthProvider;

public interface AuthUseCase {
    AuthTokenDTO register(RegisterUserCommand command);
    AuthTokenDTO login(String email, String password);
    AuthTokenDTO loginWithOAuth(String email, OAuthProvider provider);
}
