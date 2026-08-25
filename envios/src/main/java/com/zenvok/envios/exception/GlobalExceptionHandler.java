package com.zenvok.envios.exception;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> manejarValidaciones(MethodArgumentNotValidException ex, HttpServletRequest request) {
        List<String> errores = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.toList());

        log.warn("Error de validación en ruta {}: {}", request.getRequestURI(), errores);

        ErrorResponseDTO response = new ErrorResponseDTO(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Error de validación",
                request.getRequestURI(),
                errores
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<ErrorResponseDTO> manejarValidacionesParametros(HandlerMethodValidationException ex, HttpServletRequest request) {
        List<String> errores = ex.getAllErrors()
                .stream()
                .map(error -> error.getDefaultMessage())
                .collect(Collectors.toList());

        log.warn("Error de validación de parámetros en ruta {}: {}", request.getRequestURI(), errores);

        ErrorResponseDTO response = new ErrorResponseDTO(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Error de validación",
                request.getRequestURI(),
                errores
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponseDTO> manejarRuntimeException(RuntimeException ex, HttpServletRequest request) {
        log.warn("Error controlado en ruta {}: {}", request.getRequestURI(), ex.getMessage());

        String mensaje = ex.getMessage() == null ? "Error controlado" : ex.getMessage();
        String mensajeNormalizado = mensaje.toLowerCase();

        HttpStatus status = HttpStatus.BAD_REQUEST;

        if (mensajeNormalizado.contains("no existe") || mensajeNormalizado.contains("no encontrado")) {
            status = HttpStatus.NOT_FOUND;
        }

        if (mensajeNormalizado.contains("no se pudo conectar")) {
            status = HttpStatus.SERVICE_UNAVAILABLE;
        }

        ErrorResponseDTO response = new ErrorResponseDTO(
                LocalDateTime.now(),
                status.value(),
                mensaje,
                request.getRequestURI(),
                List.of(mensaje)
        );

        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> manejarException(Exception ex, HttpServletRequest request) {
        log.error("Error interno en ruta {}: {}", request.getRequestURI(), ex.getMessage());

        ErrorResponseDTO response = new ErrorResponseDTO(
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Error interno del servidor",
                request.getRequestURI(),
                List.of("Ocurrió un error inesperado")
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}