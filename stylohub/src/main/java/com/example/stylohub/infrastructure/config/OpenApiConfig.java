package com.example.stylohub.infrastructure.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Value("${app.frontend.url:http://localhost:3000}")
    private String frontendUrl;

    @Bean
    OpenAPI styloHubOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("StyloHub API")
                        .description("API da plataforma StyloHub — crie o seu perfil de links estilizado")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("StyloHub Team")
                                .url(frontendUrl))
                        .license(new License()
                                .name("Proprietary")))
                .servers(List.of(
                        new Server().url("/").description("Current server")
                ))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("JWT token obtido em /api/auth/login")
                        )
                );
    }
}
