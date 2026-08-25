package com.zenvok.Gest_Bode.service;

import com.zenvok.Gest_Bode.client.ProductoClient;
import com.zenvok.Gest_Bode.dto.ProdPasEstRequestDTO;
import com.zenvok.Gest_Bode.dto.ProdPasEstResponseDTO;
import com.zenvok.Gest_Bode.dto.ProductoResponseDTO;
import com.zenvok.Gest_Bode.model.Estante;
import com.zenvok.Gest_Bode.model.Pasillo;
import com.zenvok.Gest_Bode.model.Pas_Est;
import com.zenvok.Gest_Bode.model.ProdPasEst;
import com.zenvok.Gest_Bode.repository.PasEstRepository;
import com.zenvok.Gest_Bode.repository.ProdPasEstRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProdPasEstServiceTest {

    @Mock
    private ProductoClient productoClient;

    @Mock
    private ProdPasEstRepository prodPasEstRepository;

    @Mock
    private PasEstRepository pasEstRepository;

    @InjectMocks
    private ProdPasEstService prodPasEstService;

    @Test
    void guardar_DeberiaCrearUbicacion() {
        Pasillo pasillo = new Pasillo(1L, "Pasillo A");
        Estante estante = new Estante(1L, "Estante 1");
        Pas_Est pasEst = new Pas_Est(1L, pasillo, estante);
        ProdPasEstRequestDTO request = new ProdPasEstRequestDTO(1L, 1L);
        ProductoResponseDTO producto = new ProductoResponseDTO(1L, "Mouse", "Mouse gamer", BigDecimal.valueOf(25000), 1L, 1L, "Tecnología");
        ProdPasEst ubicacion = new ProdPasEst(1L, pasEst, 1L);

        when(pasEstRepository.findById(1L)).thenReturn(Optional.of(pasEst));
        when(productoClient.buscarPorId(1L)).thenReturn(producto);
        when(prodPasEstRepository.save(any(ProdPasEst.class))).thenReturn(ubicacion);

        ProdPasEstResponseDTO response = prodPasEstService.guardar(request);

        assertNotNull(response);
        assertEquals(1L, response.getIdProdEst());
        assertEquals(1L, response.getProductoId());
        assertEquals("Pasillo A", response.getNombrePasillo());
    }

    @Test
    void guardar_DeberiaLanzarError_CuandoPasEstNoExiste() {
        ProdPasEstRequestDTO request = new ProdPasEstRequestDTO(99L, 1L);

        when(pasEstRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> prodPasEstService.guardar(request));

        assertTrue(exception.getMessage().contains("Pasillo-Estante"));
    }

    @Test
    void guardar_DeberiaLanzarError_CuandoProductoNoExiste() {
        Pasillo pasillo = new Pasillo(1L, "Pasillo A");
        Estante estante = new Estante(1L, "Estante 1");
        Pas_Est pasEst = new Pas_Est(1L, pasillo, estante);
        ProdPasEstRequestDTO request = new ProdPasEstRequestDTO(1L, 99L);

        when(pasEstRepository.findById(1L)).thenReturn(Optional.of(pasEst));
        when(productoClient.buscarPorId(99L)).thenThrow(new RuntimeException("No encontrado"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> prodPasEstService.guardar(request));

        assertTrue(exception.getMessage().contains("Producto"));
    }

    @Test
    void obtenerTodos_DeberiaRetornarLista() {
        Pasillo pasillo = new Pasillo(1L, "Pasillo A");
        Estante estante = new Estante(1L, "Estante 1");
        Pas_Est pasEst = new Pas_Est(1L, pasillo, estante);
        ProdPasEst ubicacion = new ProdPasEst(1L, pasEst, 1L);

        when(prodPasEstRepository.findAll()).thenReturn(List.of(ubicacion));

        List<ProdPasEstResponseDTO> response = prodPasEstService.obtenerTodos();

        assertEquals(1, response.size());
        assertEquals("Estante 1", response.get(0).getNombreEstante());
    }

    @Test
    void buscarPorProductoId_DeberiaRetornarUbicacion() {
        Pasillo pasillo = new Pasillo(1L, "Pasillo A");
        Estante estante = new Estante(1L, "Estante 1");
        Pas_Est pasEst = new Pas_Est(1L, pasillo, estante);
        ProdPasEst ubicacion = new ProdPasEst(1L, pasEst, 1L);

        when(prodPasEstRepository.buscarUbicacionDeProducto(1L)).thenReturn(Optional.of(ubicacion));

        Optional<ProdPasEstResponseDTO> response = prodPasEstService.buscarPorProductoId(1L);

        assertTrue(response.isPresent());
        assertEquals(1L, response.get().getProductoId());
    }

    @Test
    void buscarPorProductoId_DeberiaRetornarVacio() {
        when(prodPasEstRepository.buscarUbicacionDeProducto(99L)).thenReturn(Optional.empty());

        Optional<ProdPasEstResponseDTO> response = prodPasEstService.buscarPorProductoId(99L);

        assertTrue(response.isEmpty());
    }
}