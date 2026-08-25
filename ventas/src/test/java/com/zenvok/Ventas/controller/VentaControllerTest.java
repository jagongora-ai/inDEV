package com.zenvok.Ventas.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zenvok.Ventas.dto.VentaRequestDTO;
import com.zenvok.Ventas.dto.VentaResponseDTO;
import com.zenvok.Ventas.service.VentasService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(VentasController.class)
@DisplayName("Test de integración web para VentasController")
class VentasControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private VentasService ventasService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("GET /api/ventas debe retornar la lista de todas las ventas")
    void listarVentas_DeberiaRetornarTodasLasVentas() throws Exception {
        VentaResponseDTO venta = crearResponse();
        
        when(ventasService.obtenerTodas()).thenReturn(List.of(venta));

        mockMvc.perform(get("/api/ventas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(10))
                .andExpect(jsonPath("$[0].total").value(1500.50))
                .andExpect(jsonPath("$[0].usuarioNombre").value("Juan Pérez"))
                .andExpect(jsonPath("$[0].estadoNombre").value("APROBADO"));

        verify(ventasService).obtenerTodas();
    }

    @Test
    @DisplayName("GET /api/ventas/{id} debe retornar una venta cuando el ID existe")
    void buscarVentaPorId_DeberiaRetornarVenta() throws Exception {
        VentaResponseDTO venta = crearResponse();

        when(ventasService.obtenerPorId(10L)).thenReturn(Optional.of(venta));

        mockMvc.perform(get("/api/ventas/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.total").value(1500.50));

        verify(ventasService).obtenerPorId(10L);
    }

    @Test
    @DisplayName("POST /api/ventas debe registrar una venta y retornar 201 Created")
    void crearVenta_DeberiaRetornarCreated() throws Exception {
        VentaRequestDTO request = crearRequest();
        VentaResponseDTO response = crearResponse();

        when(ventasService.guardar(any(VentaRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/api/ventas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.total").value(1500.50));

        verify(ventasService).guardar(any(VentaRequestDTO.class));
    }

    @Test
    @DisplayName("POST /api/ventas con datos inválidos debe retornar 400 Bad Request")
    void crearVenta_ConFechaInvalida_DeberiaRetornarBadRequest() throws Exception {
        VentaRequestDTO request = crearRequest();
        request.setFechaVentas(null);

        mockMvc.perform(post("/api/ventas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    private VentaRequestDTO crearRequest() {
        VentaRequestDTO request = new VentaRequestDTO();
        request.setFechaVentas(LocalDate.of(2026, 6, 20));
        request.setTotal(new BigDecimal("1500.50"));
        request.setUsuarioId(1L);
        request.setEstadoId(2L);
        return request;
    }

    private VentaResponseDTO crearResponse() {
        VentaResponseDTO response = new VentaResponseDTO();
        response.setId(10L);
        response.setFechaVentas(LocalDate.of(2026, 6, 20));
        response.setTotal(new BigDecimal("1500.50"));
        response.setUsuarioId(1L);
        response.setUsuarioNombre("Juan Pérez");
        response.setEstadoId(2L);
        response.setEstadoNombre("APROBADO");
        return response;
    }
}
