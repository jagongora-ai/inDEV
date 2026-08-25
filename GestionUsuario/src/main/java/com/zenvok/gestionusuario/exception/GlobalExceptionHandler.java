package com.zenvok.gestionusuario.exception;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j; 

@Slf4j 
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(MethodArgumentNotValidException ex) {
        log.warn("Excepción: Falló la validación de los campos en la petición.");
        Map<String, String> errores = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> 
            errores.put(error.getField(), error.getDefaultMessage())
        );
        return ResponseEntity.badRequest().body(errores);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntimeErrors(RuntimeException ex) {
        log.error("Excepción controlada en el servicio: {}", ex.getMessage()); 
        
        Map<String, Object> respuestaError = new LinkedHashMap<>();
        respuestaError.put("status", HttpStatus.BAD_REQUEST.value());
        respuestaError.put("error", "Error de Operación");
        respuestaError.put("message", ex.getMessage()); 
        return new ResponseEntity<>(respuestaError, HttpStatus.BAD_REQUEST);
    }
}