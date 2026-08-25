package com.zenvok.configuracion.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zenvok.configuracion.dto.ConfiguracionDTO;
import com.zenvok.configuracion.service.ConfiguracionService;

@WebMvcTest(ConfiguracionController.class)
public class ConfiguracionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ConfiguracionService configuracionService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void crearConfiguracion_Exitoso() throws Exception {
        ConfiguracionDTO dto = new ConfiguracionDTO();
        dto.setClave("iva");
        dto.setValor("19");

        when(configuracionService.guardarConfiguracion(any(ConfiguracionDTO.class))).thenReturn(dto);

        mockMvc.perform(post("/api/configuraciones")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.clave").value("iva"));
    }

    @Test
    void listarTodas_Exitoso() throws Exception {
        ConfiguracionDTO dto = new ConfiguracionDTO();
        dto.setClave("iva");
        dto.setValor("19");

        when(configuracionService.obtenerTodas()).thenReturn(Arrays.asList(dto));

        mockMvc.perform(get("/api/configuraciones"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].clave").value("iva"));
    }

    @Test
    void buscarPorClave_Encontrado() throws Exception {
        ConfiguracionDTO dto = new ConfiguracionDTO();
        dto.setClave("iva");
        dto.setValor("19");

        when(configuracionService.obtenerPorClave("iva")).thenReturn(Optional.of(dto));

        mockMvc.perform(get("/api/configuraciones/clave/iva"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valor").value("19"));
    }
}