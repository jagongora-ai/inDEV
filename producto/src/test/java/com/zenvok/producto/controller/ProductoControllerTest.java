package com.zenvok.producto.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zenvok.producto.dto.ProductoRequestDTO;
import com.zenvok.producto.dto.ProductoResponseDTO;
import com.zenvok.producto.service.ProductoService;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
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

@WebMvcTest(ProductoController.class)
class ProductoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductoService productoService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void listarProductos_DeberiaRetornarProductos() throws Exception {
        ProductoResponseDTO producto = crearResponse();

        when(productoService.listar()).thenReturn(List.of(producto));

        mockMvc.perform(get("/api/productos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nombre").value("Notebook Lenovo"))
                .andExpect(jsonPath("$[0].precio").value(450000))
                .andExpect(jsonPath("$[0].estadoId").value(1))
                .andExpect(jsonPath("$[0].categoriaId").value(1))
                .andExpect(jsonPath("$[0].categoriaNombre").value("Tecnología"));

        verify(productoService).listar();
    }

    @Test
    void buscarProductoPorId_DeberiaRetornarProducto() throws Exception {
        ProductoResponseDTO producto = crearResponse();

        when(productoService.buscarPorId(1L)).thenReturn(producto);

        mockMvc.perform(get("/api/productos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Notebook Lenovo"))
                .andExpect(jsonPath("$.categoriaNombre").value("Tecnología"));

        verify(productoService).buscarPorId(1L);
    }

    @Test
    void crearProducto_DeberiaRetornarCreated() throws Exception {
        ProductoRequestDTO request = crearRequest();
        ProductoResponseDTO response = crearResponse();

        when(productoService.guardar(any(ProductoRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/api/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Notebook Lenovo"))
                .andExpect(jsonPath("$.precio").value(450000));

        verify(productoService).guardar(any(ProductoRequestDTO.class));
    }

    @Test
    void crearProducto_ConPrecioMenorA10000_DeberiaRetornarBadRequest() throws Exception {
        ProductoRequestDTO request = crearRequest();
        request.setPrecio(new BigDecimal("5000"));

        mockMvc.perform(post("/api/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void crearProducto_ConNombreVacio_DeberiaRetornarBadRequest() throws Exception {
        ProductoRequestDTO request = crearRequest();
        request.setNombre("");

        mockMvc.perform(post("/api/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void actualizarProducto_DeberiaRetornarOk() throws Exception {
        ProductoRequestDTO request = crearRequest();
        ProductoResponseDTO response = crearResponse();

        when(productoService.actualizar(eq(1L), any(ProductoRequestDTO.class))).thenReturn(response);

        mockMvc.perform(put("/api/productos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Notebook Lenovo"));

        verify(productoService).actualizar(eq(1L), any(ProductoRequestDTO.class));
    }

    @Test
    void eliminarProducto_DeberiaRetornarNoContent() throws Exception {
        mockMvc.perform(delete("/api/productos/1"))
                .andExpect(status().isNoContent());

        verify(productoService).eliminar(1L);
    }

    @Test
    void buscarPorNombre_DeberiaRetornarProductos() throws Exception {
        ProductoResponseDTO producto = crearResponse();

        when(productoService.buscarPorNombre("note")).thenReturn(List.of(producto));

        mockMvc.perform(get("/api/productos/buscar")
                        .param("nombre", "note"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Notebook Lenovo"));

        verify(productoService).buscarPorNombre("note");
    }

    @Test
    void listarPorCategoria_DeberiaRetornarProductos() throws Exception {
        ProductoResponseDTO producto = crearResponse();

        when(productoService.listarPorCategoria(1L)).thenReturn(List.of(producto));

        mockMvc.perform(get("/api/productos/categoria/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].categoriaId").value(1));

        verify(productoService).listarPorCategoria(1L);
    }

    @Test
    void listarPorEstado_DeberiaRetornarProductos() throws Exception {
        ProductoResponseDTO producto = crearResponse();

        when(productoService.listarPorEstadoId(1L)).thenReturn(List.of(producto));

        mockMvc.perform(get("/api/productos/estado/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].estadoId").value(1));

        verify(productoService).listarPorEstadoId(1L);
    }

    private ProductoRequestDTO crearRequest() {
        ProductoRequestDTO request = new ProductoRequestDTO();
        request.setNombre("Notebook Lenovo");
        request.setDescripcion("Notebook para oficina");
        request.setPrecio(new BigDecimal("450000"));
        request.setEstadoId(1L);
        request.setCategoriaId(1L);
        return request;
    }

    private ProductoResponseDTO crearResponse() {
        ProductoResponseDTO response = new ProductoResponseDTO();
        response.setId(1L);
        response.setNombre("Notebook Lenovo");
        response.setDescripcion("Notebook para oficina");
        response.setPrecio(new BigDecimal("450000"));
        response.setEstadoId(1L);
        response.setCategoriaId(1L);
        response.setCategoriaNombre("Tecnología");
        return response;
    }
}
