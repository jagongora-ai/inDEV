package com.zenvok.estado.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Modelo de peticion para crear o actualizar un estado en el sistema")
public class EstadoRequestDTO {

    @Schema(description = "Nombre identificador del estado", example = "Activo", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "El nombre del estado es obligatorio")
    @Size(min = 3, max = 50, message = "El nombre del estado debe tener entre 3 y 50 caracteres")
    private String nombre;

    @Schema(description = "Detalle o descripcion sobre la utilidad del estado", example = "Indica que el recurso esta disponible y operativo")
    @Size(max = 255, message = "La descripción no puede superar los 255 caracteres")
    private String descripcion;
}