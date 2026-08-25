package com.zenvok.producto.exception;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.zenvok.producto.dto.ErrorResponseDTO;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidateErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errores = new LinkedHashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                errores.put(error.getField(), error.getDefaultMessage())
        );

        logger.warn("Error de validación en la petición: {}", errores);

        return ResponseEntity.badRequest().body(errores);
    }

    @ExceptionHandler({
            ProductoNotFoundException.class,
            CategoriaNotFoundException.class
    })
    public ResponseEntity<ErrorResponseDTO> handleNotFound(RuntimeException ex) {
        logger.warn("Recurso no encontrado: {}", ex.getMessage());

        ErrorResponseDTO error = new ErrorResponseDTO(
                "RECURSO_NO_ENCONTRADO",
                ex.getMessage(),
                LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseDTO> handleIllegalArgument(IllegalArgumentException ex) {
        logger.warn("Error de regla de negocio: {}", ex.getMessage());

        ErrorResponseDTO error = new ErrorResponseDTO(
                "ERROR_NEGOCIO",
                ex.getMessage(),
                LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponseDTO> handleRuntimeException(RuntimeException ex) {
        logger.error("Error interno controlado: {}", ex.getMessage());

        ErrorResponseDTO error = new ErrorResponseDTO(
                "ERROR_INTERNO",
                ex.getMessage(),
                LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGeneralException(Exception ex) {
        logger.error("Error inesperado en el microservicio producto", ex);

        ErrorResponseDTO error = new ErrorResponseDTO(
                "ERROR_GENERAL",
                "Ocurrió un error inesperado en el sistema",
                LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}