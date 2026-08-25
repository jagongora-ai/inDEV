package com.zenvok.configuracion.exception;

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
        request.setRequestURI("/api/configuraciones");

        RuntimeException exception = new RuntimeException("La clave ya existe: iva");

        ResponseEntity<ErrorResponseDTO> response = handler.manejarRuntimeException(exception, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("La clave ya existe: iva", response.getBody().getMensaje());
        assertEquals("/api/configuraciones", response.getBody().getRuta());
    }

    @Test
    void manejarRuntimeException_DeberiaRetornarNotFoundPorNoEncontrada() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/configuraciones/clave/iva");

        RuntimeException exception = new RuntimeException("Clave no encontrada: iva");

        ResponseEntity<ErrorResponseDTO> response = handler.manejarRuntimeException(exception, request);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Clave no encontrada: iva", response.getBody().getMensaje());
    }

    @Test
    void manejarRuntimeException_DeberiaRetornarNotFoundPorNoExiste() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/configuraciones/clave/descuento");

        RuntimeException exception = new RuntimeException("La clave no existe");

        ResponseEntity<ErrorResponseDTO> response = handler.manejarRuntimeException(exception, request);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("La clave no existe", response.getBody().getMensaje());
    }

    @Test
    void manejarRuntimeException_DeberiaRetornarMensajeDefaultCuandoEsNull() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/configuraciones");

        RuntimeException exception = new RuntimeException();

        ResponseEntity<ErrorResponseDTO> response = handler.manejarRuntimeException(exception, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Error controlado", response.getBody().getMensaje());
    }

    @Test
    void manejarException_DeberiaRetornarInternalServerError() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/configuraciones");

        Exception exception = new Exception("Error inesperado");

        ResponseEntity<ErrorResponseDTO> response = handler.manejarException(exception, request);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Error interno del servidor", response.getBody().getMensaje());
        assertEquals("/api/configuraciones", response.getBody().getRuta());
    }
}