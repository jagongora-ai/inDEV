package com.zenvok.Ventas.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Modelo de peticion para crear un detalle de venta")
public class DetalleRequestDTO {

    @Schema(description = "Cantidad de productos solicitados", example = "2", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad debe ser como mínimo 1")
    private Integer cantidad;

    @Schema(description = "ID del producto asociado", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "El ID del producto es obligatorio")
    private Long productoId;

    @Schema(description = "Monto subtotal del detalle de la venta", example = "49980.0", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "El subtotal no puede estar vacío")
    @DecimalMin(value = "0.01", message = "El subtotal debe ser mayor a 0")
    private BigDecimal subtotal;

    @Schema(description = "ID de la venta a la que pertenece el detalle", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "El ID de la venta es obligatorio")
    private Long ventaId; 
}
