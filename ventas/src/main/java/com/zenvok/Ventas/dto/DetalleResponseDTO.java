package com.zenvok.Ventas.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Respuesta detallada de un producto vendido dentro de una transacción")
public class DetalleResponseDTO {

    @Schema(description = "ID único del registro de este detalle", 
    example = "102")
    private Long id;

    @Schema(description = "ID del producto comprado", 
    example = "45")
    private Long productoId;

    @Schema(description = "Nombre comercial del producto obtenido del microservicio de Inventario", 
    example = "Audífonos Bluetooth")
    private String productoNombre;

    @Schema(description = "Cantidad de unidades compradas", 
    example = "2")
    private Integer cantidad;

    @Schema(description = "Subtotal acumulado para este ítem", 
    example = "39.98")
    private BigDecimal subtotal;

    @Schema(description = "ID de la venta principal a la que está amarrado este detalle", 
    example = "15")
    private Long ventaId;
}
