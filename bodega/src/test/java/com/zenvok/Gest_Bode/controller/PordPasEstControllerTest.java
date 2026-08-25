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
import com.zenvok.Gest_Bode.dto.ProdPasEstRequestDTO;
import com.zenvok.Gest_Bode.dto.ProdPasEstResponseDTO;
import com.zenvok.Gest_Bode.service.ProdPasEstService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ProdPasEstController.class)
class ProdPasEstControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProdPasEstService prodPasEstService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void ubicarProducto_DeberiaRetornarCreated() throws Exception {
        ProdPasEstRequestDTO request = new ProdPasEstRequestDTO(1L, 1L);

        ProdPasEstResponseDTO response = new ProdPasEstResponseDTO();
        response.setIdProdEst(1L);
        response.setProductoId(1L);
        response.setPasEstId(1L);
        response.setNombrePasillo("Pasillo A");
        response.setNombreEstante("Estante 1");

        when(prodPasEstService.guardar(any(ProdPasEstRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/api/productos_ubicaciones")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idProdEst").value(1))
                .andExpect(jsonPath("$.productoId").value(1))
                .andExpect(jsonPath("$.nombrePasillo").value("Pasillo A"));
    }

    @Test
    void listarTodas_DeberiaRetornarLista() throws Exception {
        ProdPasEstResponseDTO response = new ProdPasEstResponseDTO();
        response.setIdProdEst(1L);
        response.setProductoId(1L);
        response.setPasEstId(1L);
        response.setNombrePasillo("Pasillo A");
        response.setNombreEstante("Estante 1");

        when(prodPasEstService.obtenerTodos()).thenReturn(List.of(response));

        mockMvc.perform(get("/api/productos_ubicaciones"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idProdEst").value(1))
                .andExpect(jsonPath("$[0].productoId").value(1));
    }

    @Test
    void buscarPorProducto_DeberiaRetornarOk() throws Exception {
        ProdPasEstResponseDTO response = new ProdPasEstResponseDTO();
        response.setIdProdEst(1L);
        response.setProductoId(1L);
        response.setPasEstId(1L);
        response.setNombrePasillo("Pasillo A");
        response.setNombreEstante("Estante 1");

        when(prodPasEstService.buscarPorProductoId(1L)).thenReturn(Optional.of(response));

        mockMvc.perform(get("/api/productos_ubicaciones/producto/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productoId").value(1))
                .andExpect(jsonPath("$.nombreEstante").value("Estante 1"));
    }

    @Test
    void buscarPorProducto_DeberiaRetornarNotFound() throws Exception {
        when(prodPasEstService.buscarPorProductoId(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/productos_ubicaciones/producto/99"))
                .andExpect(status().isNotFound());
    }
}