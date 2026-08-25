package com.zenvok.envios.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
@Schema(description = "Modelo de petición para registrar o actualizar un envío en el sistema")
public class EnvioRequest {

    @Schema(description = "Fecha planificada o realizada del envío", example = "2026-07-05", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "La fecha de envio es obligatoria")
    private String fechaEnvio;

    @Schema(description = "Fecha de embarque o salida de la carga", example = "2026-07-06", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "La fecha de embargue es obligatoria")
    private String fechaEmbargue;

    @Schema(description = "Observaciones o anotaciones adicionales sobre la entrega", example = "Dejar en conserjería si no responde")
    private String comentarios;

    @Schema(description = "Identificador de la dirección de destino", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "El id de la direccion es obligatorio")
    private Long direccionId;

    @Schema(description = "Identificador de la venta asociada al envío", example = "10", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "El id de la venta es obligatorio")
    @Positive(message = "El id de la venta debe ser mayor a 0")
    private Long ventaId;

    @Schema(description = "Identificador del estado actual del envío", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "El id del estado es obligatorio")
    @Positive(message = "El id del estado debe ser mayor a 0")
    private Long estadoId;
}
