package com.zenvok.producto.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "API Producto",
                version = "1.0",
                description = "Microservicio encargado de la gestión de productos y categorías"
        )
)
public class OpenApiConfig {
}