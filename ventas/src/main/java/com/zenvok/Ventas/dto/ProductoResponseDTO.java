package com.zenvok.Ventas.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Información detallada del producto obtenido del microservicio de Inventario")
public class ProductoResponseDTO {

    @Schema(description = "ID único del producto", 
    example = "45")
    private Long id;

    @Schema(description = "Nombre comercial del producto", 
    example = "Audífonos Bluetooth")
    private String nombre;

    @Schema(description = "Descripción del producto", 
    example = "Audífonos inalámbricos con cancelación de ruido")
    private String descripcion;

    @Schema(description = "Precio unitario actual", 
    example = "19.99")
    private BigDecimal precio;

    @Schema(description = "ID del estado del producto", 
    example = "1")
    private Long estadoId;

    @Schema(description = "ID de la categoría asignada", 
    example = "3")
    private Long categoriaId;

    @Schema(description = "Nombre de la categoría del producto", 
    example = "Electrónica")
    private String categoriaNombre;
}
