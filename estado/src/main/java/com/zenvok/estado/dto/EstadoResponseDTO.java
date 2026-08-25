package com.zenvok.estado.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Modelo de respuesta con la informacion de un estado")
public class EstadoResponseDTO {

    @Schema(description = "Identificador unico del estado", example = "1")
    private Long id;

    @Schema(description = "Nombre identificador del estado", example = "Activo")
    private String nombre;

    @Schema(description = "Detalle o descripcion sobre la utilidad del estado", example = "Indica que el recurso esta disponible y operativo")
    private String descripcion;
}