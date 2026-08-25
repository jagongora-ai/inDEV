package com.zenvok.Ventas.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Respuesta detallada con los datos de la venta procesada")
public class VentaResponseDTO {

    @Schema(description = "ID único de la venta registrado en la BD", example = "15")
    private Long id;

    @Schema(description = "Fecha de registro de la venta", example = "2026-06-20")
    private LocalDate fechaVentas;

    @Schema(description = "Total final de la transacción", example = "1500.50")
    private BigDecimal total;

    @Schema(description = "ID del usuario comprador", example = "1")
    private Long usuarioId;

    @Schema(description = "Nombre completo del usuario comprador", example = "Juan Pérez")
    private String usuarioNombre;

    @Schema(description = "ID del estado actual de la venta", example = "2")
    private Long estadoId;

    @Schema(description = "Nombre descriptivo del estado", example = "COMPLETO")
    private String estadoNombre;
}