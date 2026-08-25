package com.zenvok.producto.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Modelo de respuesta para la gestion de errores en el sistema")
public class ErrorResponseDTO {

    @Schema(description = "Tipo o codigo del error", example = "Bad Request")
    private String error;

    @Schema(description = "Detalle o mensaje explicativo del error", example = "Los datos enviados son invalidos")
    private String message;

    @Schema(description = "Fecha y hora exacta en la que ocurrio el error", example = "05-07-2026 16:10:00")
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime fecha;

}