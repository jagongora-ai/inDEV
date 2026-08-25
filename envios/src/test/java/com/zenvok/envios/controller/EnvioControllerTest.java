package com.zenvok.envios.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zenvok.envios.dto.EnvioRequest;
import com.zenvok.envios.dto.EnvioResponse;
import com.zenvok.envios.service.EnvioService;

@WebMvcTest(EnvioController.class)
class EnvioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EnvioService envioService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void listarEnvios_DeberiaRetornarLista() throws Exception {
        EnvioResponse response = new EnvioResponse(1L, "2026-06-20", "2026-06-21", "Entrega", 1L, 1L, 1L);

        when(envioService.listarEnvios()).thenReturn(List.of(response));

        mockMvc.perform(get("/api/envios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].fechaEnvio").value("2026-06-20"))
                .andExpect(jsonPath("$[0].direccionId").value(1));
    }

    @Test
    void obtenerPorId_DeberiaRetornarEnvio() throws Exception {
        EnvioResponse response = new EnvioResponse(1L, "2026-06-20", "2026-06-21", "Entrega", 1L, 1L, 1L);

        when(envioService.obtenerPorId(1L)).thenReturn(response);

        mockMvc.perform(get("/api/envios/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.comentarios").value("Entrega"));
    }

    @Test
    void buscarPorVenta_DeberiaRetornarLista() throws Exception {
        EnvioResponse response = new EnvioResponse(1L, "2026-06-20", "2026-06-21", "Entrega", 1L, 1L, 1L);

        when(envioService.buscarPorVenta(1L)).thenReturn(List.of(response));

        mockMvc.perform(get("/api/envios/venta/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].ventaId").value(1));
    }

    @Test
    void buscarPorDireccion_DeberiaRetornarLista() throws Exception {
        EnvioResponse response = new EnvioResponse(1L, "2026-06-20", "2026-06-21", "Entrega", 1L, 1L, 1L);

        when(envioService.buscarPorDireccion(1L)).thenReturn(List.of(response));

        mockMvc.perform(get("/api/envios/direccion/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].direccionId").value(1));
    }

    @Test
    void buscarPorEstado_DeberiaRetornarLista() throws Exception {
        EnvioResponse response = new EnvioResponse(1L, "2026-06-20", "2026-06-21", "Entrega", 1L, 1L, 1L);

        when(envioService.buscarPorEstado(1L)).thenReturn(List.of(response));

        mockMvc.perform(get("/api/envios/estado/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].estadoId").value(1));
    }

    @Test
    void crearEnvio_DeberiaRetornarCreated() throws Exception {
        EnvioRequest request = new EnvioRequest();
        request.setFechaEnvio("2026-06-20");
        request.setFechaEmbargue("2026-06-21");
        request.setComentarios("Entrega");
        request.setDireccionId(1L);
        request.setVentaId(1L);
        request.setEstadoId(1L);

        EnvioResponse response = new EnvioResponse(1L, "2026-06-20", "2026-06-21", "Entrega", 1L, 1L, 1L);

        when(envioService.crearEnvio(any(EnvioRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/envios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.fechaEnvio").value("2026-06-20"))
                .andExpect(jsonPath("$.ventaId").value(1));
    }

    @Test
    void crearEnvio_DeberiaRetornarBadRequest_CuandoDatosSonInvalidos() throws Exception {
        EnvioRequest request = new EnvioRequest();
        request.setFechaEnvio("");
        request.setFechaEmbargue("");
        request.setDireccionId(null);
        request.setVentaId(null);
        request.setEstadoId(null);

        mockMvc.perform(post("/api/envios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void actualizarEnvio_DeberiaRetornarOk() throws Exception {
        EnvioRequest request = new EnvioRequest();
        request.setFechaEnvio("2026-07-01");
        request.setFechaEmbargue("2026-07-02");
        request.setComentarios("Actualizado");
        request.setDireccionId(1L);
        request.setVentaId(1L);
        request.setEstadoId(1L);

        EnvioResponse response = new EnvioResponse(1L, "2026-07-01", "2026-07-02", "Actualizado", 1L, 1L, 1L);

        when(envioService.actualizarEnvio(any(Long.class), any(EnvioRequest.class))).thenReturn(response);

        mockMvc.perform(put("/api/envios/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fechaEnvio").value("2026-07-01"))
                .andExpect(jsonPath("$.comentarios").value("Actualizado"));
    }

    @Test
    void eliminarEnvio_DeberiaRetornarNoContent() throws Exception {
        doNothing().when(envioService).eliminarEnvio(1L);

        mockMvc.perform(delete("/api/envios/1"))
                .andExpect(status().isNoContent());
    }
}