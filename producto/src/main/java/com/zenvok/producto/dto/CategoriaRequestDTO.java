package com.zenvok.producto.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Modelo de peticion para crear o actualizar una categoria")
public class CategoriaRequestDTO {

    @NotBlank(message = "El nombre de la categoría es obligatorio")
    @Size(min = 3, max = 100, message = "El nombre de la categoría debe tener entre 3 y 100 caracteres")
    @Schema(description = "Nombre de la categoria", example = "Tecnologia", requiredMode = Schema.RequiredMode.REQUIRED)
    private String nombre;

    @Schema(description = "Descripcion detallada de la categoria", example = "Dispositivos electronicos y accesorios tecnologicos", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @Size(max = 255, message = "La descripción no puede superar los 255 caracteres")
    private String descripcion;
}