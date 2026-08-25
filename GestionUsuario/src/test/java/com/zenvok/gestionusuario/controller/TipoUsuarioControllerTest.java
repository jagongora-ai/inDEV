package com.zenvok.gestionusuario.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zenvok.gestionusuario.dto.TipoUsuarioDTO;
import com.zenvok.gestionusuario.service.TipoUsuarioService;

@WebMvcTest(TipoUsuarioController.class)
class TipoUsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TipoUsuarioService tipoUsuarioService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void crearTipo_DeberiaRetornarTipoCreado() throws Exception {
        TipoUsuarioDTO request = new TipoUsuarioDTO(null, "Administrador");
        TipoUsuarioDTO response = new TipoUsuarioDTO(1L, "Administrador");

        when(tipoUsuarioService.crearTipo(any(TipoUsuarioDTO.class))).thenReturn(response);

        mockMvc.perform(post("/api/usuarios/tipos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombreTipo").value("Administrador"));
    }

    @Test
    void obtenerTodosLosTipos_DeberiaRetornarLista() throws Exception {
        when(tipoUsuarioService.obtenerTodosLosTipos()).thenReturn(Arrays.asList(new TipoUsuarioDTO(1L, "Administrador")));

        mockMvc.perform(get("/api/usuarios/tipos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombreTipo").value("Administrador"));
    }

    @Test
    void obtenerTipoPorId_DeberiaRetornarTipo_CuandoExiste() throws Exception {
        when(tipoUsuarioService.obtenerTipoPorId(1L)).thenReturn(Optional.of(new TipoUsuarioDTO(1L, "Administrador")));

        mockMvc.perform(get("/api/usuarios/tipos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombreTipo").value("Administrador"));
    }

    @Test
    void obtenerTipoPorId_DeberiaRetornarNotFound_CuandoNoExiste() throws Exception {
        when(tipoUsuarioService.obtenerTipoPorId(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/usuarios/tipos/99"))
                .andExpect(status().isNotFound());
    }
}