package com.zenvok.estado.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Modelo de respuesta estandarizado para la notificacion de errores en el sistema")
public class ErrorResponseDTO {

    @Schema(description = "Detalle técnico o mensaje del error ocurrido", example = "El recurso solicitado no fue encontrado")
    private String error;
    
    @Schema(description = "Nombre o tipo de excepcion/error clasificado", example = "ResourceNotFoundException")
    private String nombre;
    
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    @Schema(description = "Fecha y hora exacta en la que se produjo el inconveniente", example = "05-07-2026 17:27:35")
    private LocalDateTime fecha;
}