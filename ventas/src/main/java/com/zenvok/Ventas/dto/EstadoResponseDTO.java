package com.zenvok.Ventas.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Información sobre el estado actual del flujo de la venta")
public class EstadoResponseDTO {

    @Schema(description = "ID único del estado", 
    example = "2")
    private Long id;

    @Schema(description = "Nombre corto del estado", 
    example = "APROBADO")
    private String nombre;

    @Schema(description = "Descripción detallada de lo que significa este estado", 
    example = "La venta ha sido pagada y confirmada con éxito")
    private String descripcion;
}