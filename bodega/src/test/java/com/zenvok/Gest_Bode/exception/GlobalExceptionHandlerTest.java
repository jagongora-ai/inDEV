package com.zenvok.Gest_Bode.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void manejarRuntimeException_DeberiaRetornarBadRequest() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/pasillos");

        RuntimeException exception = new RuntimeException("Error controlado");

        ResponseEntity<ErrorResponseDTO> response = handler.manejarRuntimeException(exception, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Error controlado", response.getBody().getMensaje());
        assertEquals("/api/pasillos", response.getBody().getRuta());
    }

    @Test
    void manejarRuntimeException_DeberiaRetornarNotFound() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/pasillos");

        RuntimeException exception = new RuntimeException("No encontrado");

        ResponseEntity<ErrorResponseDTO> response = handler.manejarRuntimeException(exception, request);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("No encontrado", response.getBody().getMensaje());
    }

    @Test
    void manejarRuntimeException_DeberiaRetornarServiceUnavailable() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/productos_ubicaciones");

        RuntimeException exception = new RuntimeException("No se pudo conectar con producto-service");

        ResponseEntity<ErrorResponseDTO> response = handler.manejarRuntimeException(exception, request);

        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("No se pudo conectar con producto-service", response.getBody().getMensaje());
    }

    @Test
    void manejarException_DeberiaRetornarInternalServerError() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/pasillos");

        Exception exception = new Exception("Error inesperado");

        ResponseEntity<ErrorResponseDTO> response = handler.manejarException(exception, request);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Error interno del servidor", response.getBody().getMensaje());
        assertEquals("/api/pasillos", response.getBody().getRuta());
    }
}