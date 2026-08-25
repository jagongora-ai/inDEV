package com.zenvok.Gest_Bode.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zenvok.Gest_Bode.dto.PasEstRequestDTO;
import com.zenvok.Gest_Bode.dto.PasEstResponseDTO;
import com.zenvok.Gest_Bode.service.PasEstService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(PasEstController.class)
class PasEstControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PasEstService pasEstService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void asignarEstanteAPasillo_DeberiaRetornarCreated() throws Exception {
        PasEstRequestDTO request = new PasEstRequestDTO(1L, 1L);

        PasEstResponseDTO response = new PasEstResponseDTO();
        response.setIdPasEst(1L);
        response.setPasilloId(1L);
        response.setNombrePasillo("Pasillo A");
        response.setEstanteId(1L);
        response.setNombreEstante("Estante 1");

        when(pasEstService.guardar(any(PasEstRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/api/bodega_estructuras")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idPasEst").value(1))
                .andExpect(jsonPath("$.nombrePasillo").value("Pasillo A"))
                .andExpect(jsonPath("$.nombreEstante").value("Estante 1"));
    }

    @Test
    void listarTodas_DeberiaRetornarLista() throws Exception {
        PasEstResponseDTO response = new PasEstResponseDTO();
        response.setIdPasEst(1L);
        response.setPasilloId(1L);
        response.setNombrePasillo("Pasillo A");
        response.setEstanteId(1L);
        response.setNombreEstante("Estante 1");

        when(pasEstService.obtenerTodos()).thenReturn(List.of(response));

        mockMvc.perform(get("/api/bodega_estructuras"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idPasEst").value(1))
                .andExpect(jsonPath("$[0].nombrePasillo").value("Pasillo A"));
    }

    @Test
    void listarPorPasillo_DeberiaRetornarLista() throws Exception {
        PasEstResponseDTO response = new PasEstResponseDTO();
        response.setIdPasEst(1L);
        response.setPasilloId(1L);
        response.setNombrePasillo("Pasillo A");
        response.setEstanteId(1L);
        response.setNombreEstante("Estante 1");

        when(pasEstService.listarEstantesPorPasillo(1L)).thenReturn(List.of(response));

        mockMvc.perform(get("/api/bodega_estructuras/pasillo/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].pasilloId").value(1))
                .andExpect(jsonPath("$[0].nombreEstante").value("Estante 1"));
    }
}