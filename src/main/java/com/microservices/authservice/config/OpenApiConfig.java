package com.microservices.authservice.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "Auth Service API",
        version = "1.0.0",
        description = "Authentication microservice for JWT-based authentication\n\n" +
                     "🌐 All endpoints are PUBLIC (no JWT required)\n" +
                     "User management operations are handled by the User Service",
        contact = @Contact(
            name = "Development Team",
            email = "dev@microservices.com"
        ),
        license = @License(
            name = "MIT License",
            url = "https://opensource.org/licenses/MIT"
        )
    ),
    servers = {
        @Server(url = "/api/auth", description = "Auth Service"),
        @Server(url = "http://localhost:8080/api/auth", description = "Local Development Server")
    }
)
@SecurityScheme(
    name = "bearerAuth",
    type = SecuritySchemeType.HTTP,
    bearerFormat = "JWT",
    scheme = "bearer"
)
public class OpenApiConfig {
}