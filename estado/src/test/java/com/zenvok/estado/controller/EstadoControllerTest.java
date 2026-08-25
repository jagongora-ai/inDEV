package com.zenvok.estado.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zenvok.estado.dto.EstadoRequestDTO;
import com.zenvok.estado.dto.EstadoResponseDTO;
import com.zenvok.estado.service.EstadoService;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;

import org.springframework.http.MediaType;

import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EstadoController.class)
class EstadoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EstadoService estadoService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void listar_DeberiaRetornarEstados() throws Exception {
        EstadoResponseDTO estado = new EstadoResponseDTO();
        estado.setId(1L);
        estado.setNombre("Activo");
        estado.setDescripcion("Estado activo");

        when(estadoService.listar()).thenReturn(List.of(estado));

        mockMvc.perform(get("/api/estados"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nombre").value("Activo"));

        verify(estadoService).listar();
    }

    @Test
    void buscarPorId_DeberiaRetornarEstado() throws Exception {
        EstadoResponseDTO estado = new EstadoResponseDTO();
        estado.setId(1L);
        estado.setNombre("Activo");
        estado.setDescripcion("Estado activo");

        when(estadoService.buscarPorId(1L)).thenReturn(estado);

        mockMvc.perform(get("/api/estados/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Activo"));

        verify(estadoService).buscarPorId(1L);
    }

    @Test
    void buscarPorNombre_DeberiaRetornarEstados() throws Exception {
        EstadoResponseDTO estado = new EstadoResponseDTO();
        estado.setId(1L);
        estado.setNombre("Activo");
        estado.setDescripcion("Estado activo");

        when(estadoService.buscarPorNombre("act")).thenReturn(List.of(estado));

        mockMvc.perform(get("/api/estados/buscar")
                        .param("nombre", "act"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Activo"));

        verify(estadoService).buscarPorNombre("act");
    }

    @Test
    void crearEstado_DeberiaRetornarCreated() throws Exception {
        EstadoRequestDTO request = new EstadoRequestDTO();
        request.setNombre("Activo");
        request.setDescripcion("Estado activo");

        EstadoResponseDTO response = new EstadoResponseDTO();
        response.setId(1L);
        response.setNombre("Activo");
        response.setDescripcion("Estado activo");

        when(estadoService.guardar(any(EstadoRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/api/estados")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Activo"));

        verify(estadoService).guardar(any(EstadoRequestDTO.class));
    }

    @Test
    void crearEstado_ConNombreVacio_DeberiaRetornarBadRequest() throws Exception {
        EstadoRequestDTO request = new EstadoRequestDTO();
        request.setNombre("");
        request.setDescripcion("Estado activo");

        mockMvc.perform(post("/api/estados")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void actualizarEstado_DeberiaRetornarOk() throws Exception {
        EstadoRequestDTO request = new EstadoRequestDTO();
        request.setNombre("Inactivo");
        request.setDescripcion("Estado inactivo");

        EstadoResponseDTO response = new EstadoResponseDTO();
        response.setId(1L);
        response.setNombre("Inactivo");
        response.setDescripcion("Estado inactivo");

        when(estadoService.actualizar(eq(1L), any(EstadoRequestDTO.class))).thenReturn(response);

        mockMvc.perform(put("/api/estados/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Inactivo"));

        verify(estadoService).actualizar(eq(1L), any(EstadoRequestDTO.class));
    }

    @Test
    void eliminarEstado_DeberiaRetornarNoContent() throws Exception {
        mockMvc.perform(delete("/api/estados/1"))
                .andExpect(status().isNoContent());

        verify(estadoService).eliminar(1L);
    }
}