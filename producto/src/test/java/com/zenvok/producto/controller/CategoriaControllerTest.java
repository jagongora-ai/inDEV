package com.zenvok.producto.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zenvok.producto.dto.CategoriaRequestDTO;
import com.zenvok.producto.dto.CategoriaResponseDTO;
import com.zenvok.producto.service.CategoriaService;

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

@WebMvcTest(CategoriaController.class)
class CategoriaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CategoriaService categoriaService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void listarCategorias_DeberiaRetornarCategorias() throws Exception {
        CategoriaResponseDTO categoria = crearResponse();

        when(categoriaService.listar()).thenReturn(List.of(categoria));

        mockMvc.perform(get("/api/categorias"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nombre").value("Tecnología"))
                .andExpect(jsonPath("$[0].descripcion").value("Productos tecnológicos"));

        verify(categoriaService).listar();
    }

    @Test
    void buscarCategoriaPorId_DeberiaRetornarCategoria() throws Exception {
        CategoriaResponseDTO categoria = crearResponse();

        when(categoriaService.buscarPorId(1L)).thenReturn(categoria);

        mockMvc.perform(get("/api/categorias/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Tecnología"));

        verify(categoriaService).buscarPorId(1L);
    }

    @Test
    void crearCategoria_DeberiaRetornarCreated() throws Exception {
        CategoriaRequestDTO request = crearRequest();
        CategoriaResponseDTO response = crearResponse();

        when(categoriaService.guardar(any(CategoriaRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/api/categorias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Tecnología"));

        verify(categoriaService).guardar(any(CategoriaRequestDTO.class));
    }

    @Test
    void crearCategoria_ConNombreVacio_DeberiaRetornarBadRequest() throws Exception {
        CategoriaRequestDTO request = crearRequest();
        request.setNombre("");

        mockMvc.perform(post("/api/categorias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void actualizarCategoria_DeberiaRetornarOk() throws Exception {
        CategoriaRequestDTO request = crearRequest();
        CategoriaResponseDTO response = crearResponse();

        when(categoriaService.actualizar(eq(1L), any(CategoriaRequestDTO.class))).thenReturn(response);

        mockMvc.perform(put("/api/categorias/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Tecnología"));

        verify(categoriaService).actualizar(eq(1L), any(CategoriaRequestDTO.class));
    }

    @Test
    void eliminarCategoria_DeberiaRetornarNoContent() throws Exception {
        mockMvc.perform(delete("/api/categorias/1"))
                .andExpect(status().isNoContent());

        verify(categoriaService).eliminar(1L);
    }

    private CategoriaRequestDTO crearRequest() {
        CategoriaRequestDTO request = new CategoriaRequestDTO();
        request.setNombre("Tecnología");
        request.setDescripcion("Productos tecnológicos");
        return request;
    }

    private CategoriaResponseDTO crearResponse() {
        CategoriaResponseDTO response = new CategoriaResponseDTO();
        response.setId(1L);
        response.setNombre("Tecnología");
        response.setDescripcion("Productos tecnológicos");
        return response;
    }
}