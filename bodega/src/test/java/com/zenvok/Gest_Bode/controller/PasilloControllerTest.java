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
import com.zenvok.Gest_Bode.dto.PasilloRequestDTO;
import com.zenvok.Gest_Bode.dto.PasilloResponseDTO;
import com.zenvok.Gest_Bode.service.PasilloService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(PasilloController.class)
class PasilloControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PasilloService pasilloService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void crear_DeberiaRetornarCreated() throws Exception {
        PasilloRequestDTO request = new PasilloRequestDTO("Pasillo A");
        PasilloResponseDTO response = new PasilloResponseDTO(1L, "Pasillo A");

        when(pasilloService.guardar(any(PasilloRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/api/pasillos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idPasillo").value(1))
                .andExpect(jsonPath("$.nombrePasillo").value("Pasillo A"));
    }

    @Test
    void listarTodos_DeberiaRetornarLista() throws Exception {
        PasilloResponseDTO response = new PasilloResponseDTO(1L, "Pasillo A");

        when(pasilloService.obtenerTodos()).thenReturn(List.of(response));

        mockMvc.perform(get("/api/pasillos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idPasillo").value(1))
                .andExpect(jsonPath("$[0].nombrePasillo").value("Pasillo A"));
    }

    @Test
    void buscarPorId_DeberiaRetornarOk() throws Exception {
        PasilloResponseDTO response = new PasilloResponseDTO(1L, "Pasillo A");

        when(pasilloService.obtenerPorId(1L)).thenReturn(Optional.of(response));

        mockMvc.perform(get("/api/pasillos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idPasillo").value(1))
                .andExpect(jsonPath("$.nombrePasillo").value("Pasillo A"));
    }

    @Test
    void buscarPorId_DeberiaRetornarNotFound() throws Exception {
        when(pasilloService.obtenerPorId(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/pasillos/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void buscarPorNombre_DeberiaRetornarLista() throws Exception {
        PasilloResponseDTO response = new PasilloResponseDTO(1L, "Pasillo A");

        when(pasilloService.buscarPorNombre("A")).thenReturn(List.of(response));

        mockMvc.perform(get("/api/pasillos/buscar")
                .param("nombre", "A"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombrePasillo").value("Pasillo A"));
    }
}