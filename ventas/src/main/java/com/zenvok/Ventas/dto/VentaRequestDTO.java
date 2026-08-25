package com.zenvok.Ventas.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Modelo de peticion para registrar una nueva venta")
public class VentaRequestDTO {

    @Schema(description = "Fecha en la que se efectua la venta", example = "2026-07-05", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "La fecha de venta es obligatoria")
    private LocalDate fechaVentas;

    @Schema(description = "Monto total acumulado de la venta (Valor minimo de 1)", example = "49980.0", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "El total no puede estar vacío")
    @DecimalMin(value = "1", message = "El total debe ser mayor a 0")
    private BigDecimal total;

    @Schema(description = "ID del usuario que realiza o registra la venta", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "El ID del usuario es obligatorio")
    private Long usuarioId;

    @Schema(description = "ID del estado inicial de la venta", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "El ID del estado es obligatorio")
    private Long estadoId;
}