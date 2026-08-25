package com.zenvok.producto.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Modelo de respuesta con la informacion del estado de un producto")
public class EstadoResponseDTO {

    @Schema(description = "Identificador unico del estado", example = "1")
    private Long id;

    @Schema(description = "Nombre del estado", example = "Activo")
    private String nombre;

    @Schema(description = "Descripcion de lo que representa el estado", example = "El producto se encuentra disponible para la venta")
    private String descripcion;
}