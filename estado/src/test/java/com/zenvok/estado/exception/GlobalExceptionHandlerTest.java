package com.zenvok.estado.exception;

import com.zenvok.estado.dto.ErrorResponseDTO;

import org.junit.jupiter.api.Test;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handleEstadoNotFound_DeberiaRetornarNotFound() {
        EstadoNotFoundException exception = new EstadoNotFoundException(99L);

        ResponseEntity<ErrorResponseDTO> response = handler.handleEstadoNotFound(exception);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("RECURSO_NO_ENCONTRADO", response.getBody().getError());
        assertTrue(response.getBody().getNombre().contains("99"));
        assertNotNull(response.getBody().getFecha());
    }

    @Test
    void handleIllegalArgument_DeberiaRetornarBadRequest() {
        IllegalArgumentException exception = new IllegalArgumentException("Dato inválido");

        ResponseEntity<ErrorResponseDTO> response = handler.handleIllegalArgument(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("ERROR_NEGOCIO", response.getBody().getError());
        assertEquals("Dato inválido", response.getBody().getNombre());
        assertNotNull(response.getBody().getFecha());
    }

    @Test
    void handleRuntimeException_DeberiaRetornarInternalServerError() {
        RuntimeException exception = new RuntimeException("Error controlado");

        ResponseEntity<ErrorResponseDTO> response = handler.handleRuntimeException(exception);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("ERROR_INTERNO", response.getBody().getError());
        assertEquals("Error controlado", response.getBody().getNombre());
        assertNotNull(response.getBody().getFecha());
    }

    @Test
    void handleGeneralException_DeberiaRetornarInternalServerError() {
        Exception exception = new Exception("Error inesperado");

        ResponseEntity<ErrorResponseDTO> response = handler.handleGeneralException(exception);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("ERROR_GENERAL", response.getBody().getError());
        assertEquals("Ocurrió un error inesperado en el sistema", response.getBody().getNombre());
        assertNotNull(response.getBody().getFecha());
    }
}