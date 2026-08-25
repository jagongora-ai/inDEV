package com.zenvok.Ventas.exception;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorResponseDTO {

    private LocalDateTime fecha;
    private int estado;
    private String mensaje;
    private String ruta;
    private List<String> errores;
}