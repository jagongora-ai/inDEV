package com.zenvok.Ventas.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zenvok.Ventas.dto.DetalleRequestDTO;
import com.zenvok.Ventas.dto.DetalleResponseDTO;
import com.zenvok.Ventas.service.DetalleService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DetalleController.class)
@DisplayName("Test de integración web para DetalleController")
class DetalleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DetalleService detalleService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("GET /api/detalles debe retornar todos los detalles")
    void listarDetalles_DeberiaRetornarDetalles() throws Exception {
        DetalleResponseDTO detalle = crearResponse();
        when(detalleService.listar()).thenReturn(List.of(detalle));
        mockMvc.perform(get("/api/detalles"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(1))
            .andExpect(jsonPath("$[0].productoId").value(100))
            .andExpect(jsonPath("$[0].productoNombre").value("Audífonos Bluetooth"))
            .andExpect(jsonPath("$[0].cantidad").value(2))
            .andExpect(jsonPath("$[0].subtotal").value(500.25))
            .andExpect(jsonPath("$[0].ventaId").value(15));
        verify(detalleService).listar();
    }

    @Test
    @DisplayName("GET /api/detalles/{id} debe retornar un detalle cuando el ID existe")
    void buscarDetallePorId_DeberiaRetornarDetalle() throws Exception {
        DetalleResponseDTO detalle = crearResponse();

        when(detalleService.findById(1L)).thenReturn(Optional.of(detalle));

        mockMvc.perform(get("/api/detalles/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.productoId").value(100))
            .andExpect(jsonPath("$.productoNombre").value("Audífonos Bluetooth"))
            .andExpect(jsonPath("$.subtotal").value(500.25));
        verify(detalleService).findById(1L);
    }

    @Test
    @DisplayName("POST /api/detalles debe crear un detalle y retornar 201 Created")
    void crearDetalle_DeberiaRetornarCreated() throws Exception {
        DetalleRequestDTO request = crearRequest();
        DetalleResponseDTO response = crearResponse();

        when(detalleService.guardar(any(DetalleRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/api/detalles")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.cantidad").value(2))
            .andExpect(jsonPath("$.ventaId").value(15));
        verify(detalleService).guardar(any(DetalleRequestDTO.class));
    }

    @Test
    @DisplayName("POST /api/detalles con cantidad inválida (0) debe retornar 400 Bad Request")
    void crearDetalle_ConCantidadInvalida_DeberiaRetornarBadRequest() throws Exception {
        DetalleRequestDTO request = crearRequest();
        request.setCantidad(0);

        mockMvc.perform(post("/api/detalles")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest());
    }

    private DetalleRequestDTO crearRequest() {
        DetalleRequestDTO request = new DetalleRequestDTO();
        request.setCantidad(2);
        request.setProductoId(100L);
        request.setSubtotal(new BigDecimal("500.25"));
        request.setVentaId(15L);
        return request;
    }

    private DetalleResponseDTO crearResponse() {
        DetalleResponseDTO response = new DetalleResponseDTO();
        response.setId(1L);
        response.setProductoId(100L);
        response.setProductoNombre("Audífonos Bluetooth");
        response.setCantidad(2);
        response.setSubtotal(new BigDecimal("500.25"));
        response.setVentaId(15L);
        return response;
    }
}