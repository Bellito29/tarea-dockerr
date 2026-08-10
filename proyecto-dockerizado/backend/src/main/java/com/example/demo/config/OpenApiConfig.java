package com.example.demo.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "Fitness App API",
        version = "1.0.0",
        description = "API REST para la gestión de usuarios, entrenadores, rutinas, ejercicios, sesiones y más.",
        contact = @Contact(name = "Equipo de Desarrollo")
    ),
    servers = {
        @Server(url = "http://localhost:8080", description = "Servidor local de desarrollo")
    }
)
@SecurityScheme(
    name = "bearerAuth",
    type = SecuritySchemeType.HTTP,
    scheme = "bearer",
    bearerFormat = "JWT",
    in = SecuritySchemeIn.HEADER,
    description = "Token JWT obtenido desde el endpoint POST /rest/public/auth/login"
)
public class OpenApiConfig {
}
