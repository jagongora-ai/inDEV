package com.zenvok.Gest_Bode.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zenvok.Gest_Bode.dto.EstanteRequestDTO;
import com.zenvok.Gest_Bode.dto.EstanteResponseDTO;
import com.zenvok.Gest_Bode.service.EstanteService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(EstanteController.class)
class EstanteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EstanteService estanteService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void crear_DeberiaRetornarCreated() throws Exception {
        EstanteRequestDTO request = new EstanteRequestDTO("Estante 1");
        EstanteResponseDTO response = new EstanteResponseDTO(1L, "Estante 1");

        when(estanteService.guardar(any(EstanteRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/api/estantes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idEstante").value(1))
                .andExpect(jsonPath("$.nombreEstante").value("Estante 1"));
    }

    @Test
    void listarTodos_DeberiaRetornarLista() throws Exception {
        EstanteResponseDTO response = new EstanteResponseDTO(1L, "Estante 1");

        when(estanteService.obtenerTodos()).thenReturn(List.of(response));

        mockMvc.perform(get("/api/estantes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idEstante").value(1))
                .andExpect(jsonPath("$[0].nombreEstante").value("Estante 1"));
    }

    @Test
    void buscarPorId_DeberiaRetornarOk() throws Exception {
        EstanteResponseDTO response = new EstanteResponseDTO(1L, "Estante 1");

        when(estanteService.obtenerPorId(1L)).thenReturn(Optional.of(response));

        mockMvc.perform(get("/api/estantes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idEstante").value(1))
                .andExpect(jsonPath("$.nombreEstante").value("Estante 1"));
    }

    @Test
    void buscarPorId_DeberiaRetornarNotFound() throws Exception {
        when(estanteService.obtenerPorId(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/estantes/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void buscarPorNombre_DeberiaRetornarLista() throws Exception {
        EstanteResponseDTO response = new EstanteResponseDTO(1L, "Estante 1");

        when(estanteService.buscarPorNombre("1")).thenReturn(List.of(response));

        mockMvc.perform(get("/api/estantes/buscar")
                .param("nombre", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombreEstante").value("Estante 1"));
    }
}