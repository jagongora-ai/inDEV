package com.zenvok.estado.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "API Estado",
                version = "1.0",
                description = "Microservicio encargado de la gestión de estados"
        )
)
public class OpenApiConfig {
}