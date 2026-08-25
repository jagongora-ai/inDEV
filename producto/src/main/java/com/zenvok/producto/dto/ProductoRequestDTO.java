package com.zenvok.producto.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Modelo de peticion para crear o actualizar un producto")
public class ProductoRequestDTO {

    @Schema(description = "Nombre del producto", example = "Teclado Mecanico RGB", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
    private String nombre;

    @Schema(description = "Descripcion detallada del producto", example = "Teclado con switches red y distribucion en español", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @Size(max = 255, message = "La descripción no puede superar los 255 caracteres")
    private String descripcion;

    @Schema(description = "Precio del producto (Valor minimo de 10000)", example = "24990.0", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "10000.0", inclusive = true, message = "El precio no puede ser menor a 10000")
    private BigDecimal precio;

    @Schema(description = "ID del estado del producto", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "El estado es obligatorio")
    @Positive(message = "El estadoId debe ser mayor a 0")
    private Long estadoId;

    @Schema(description = "ID de la categoria del producto", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "La categoría es obligatoria")
    @Positive(message = "La categoriaId debe ser mayor a 0")
    private Long categoriaId;
}