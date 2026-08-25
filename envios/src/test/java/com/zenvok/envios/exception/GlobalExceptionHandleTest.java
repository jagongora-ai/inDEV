package com.zenvok.envios.exception;

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
        request.setRequestURI("/api/envios");

        RuntimeException exception = new RuntimeException("Error controlado");

        ResponseEntity<ErrorResponseDTO> response = handler.manejarRuntimeException(exception, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Error controlado", response.getBody().getMensaje());
        assertEquals("/api/envios", response.getBody().getRuta());
    }

    @Test
    void manejarRuntimeException_DeberiaRetornarNotFound() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/envios");

        RuntimeException exception = new RuntimeException("Envio no existe");

        ResponseEntity<ErrorResponseDTO> response = handler.manejarRuntimeException(exception, request);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Envio no existe", response.getBody().getMensaje());
    }

    @Test
    void manejarRuntimeException_DeberiaRetornarServiceUnavailable() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/envios");

        RuntimeException exception = new RuntimeException("No se pudo conectar con venta-service");

        ResponseEntity<ErrorResponseDTO> response = handler.manejarRuntimeException(exception, request);

        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("No se pudo conectar con venta-service", response.getBody().getMensaje());
    }

    @Test
    void manejarException_DeberiaRetornarInternalServerError() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/envios");

        Exception exception = new Exception("Error inesperado");

        ResponseEntity<ErrorResponseDTO> response = handler.manejarException(exception, request);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Error interno del servidor", response.getBody().getMensaje());
        assertEquals("/api/envios", response.getBody().getRuta());
    }
}