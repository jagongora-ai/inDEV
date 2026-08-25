package com.zenvok.envios.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "Modelo de respuesta con la información detallada de un envío")
public class EnvioResponse {
    @Schema(description = "Identificador único del envío", example = "1")
    private Long id;
    @Schema(description = "Fecha en la que se realiza o planifica el envío", example = "2026-07-05")
    private String fechaEnvio;
    @Schema(description = "Fecha en la que el envío fue embarcado", example = "2026-07-06")
    private String fechaEmbargue;
    @Schema(description = "Observaciones o comentarios adicionales de la entrega", example = "Dejar en conserjería si no responde")
    private String comentarios;
    @Schema(description = "Identificador de la dirección de destino", example = "1")
    private Long direccionId;
    @Schema(description = "Identificador de la venta asociada", example = "10")
    private Long ventaId;
    @Schema(description = "Identificador del estado actual del envío", example = "1")
    private Long estadoId;
}