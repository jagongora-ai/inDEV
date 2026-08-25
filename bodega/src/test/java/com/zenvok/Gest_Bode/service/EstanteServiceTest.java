package com.zenvok.Gest_Bode.service;

import com.zenvok.Gest_Bode.dto.EstanteRequestDTO;
import com.zenvok.Gest_Bode.dto.EstanteResponseDTO;
import com.zenvok.Gest_Bode.model.Estante;
import com.zenvok.Gest_Bode.repository.EstanteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EstanteServiceTest {

    @Mock
    private EstanteRepository estanteRepository;

    @InjectMocks
    private EstanteService estanteService;

    @Test
    void guardar_DeberiaCrearEstante() {
        EstanteRequestDTO request = new EstanteRequestDTO("Estante 1");
        Estante estante = new Estante(1L, "Estante 1");

        when(estanteRepository.save(any(Estante.class))).thenReturn(estante);

        EstanteResponseDTO response = estanteService.guardar(request);

        assertNotNull(response);
        assertEquals(1L, response.getIdEstante());
        assertEquals("Estante 1", response.getNombreEstante());
        verify(estanteRepository, times(1)).save(any(Estante.class));
    }

    @Test
    void obtenerTodos_DeberiaRetornarLista() {
        when(estanteRepository.findAll()).thenReturn(List.of(new Estante(1L, "Estante 1")));

        List<EstanteResponseDTO> response = estanteService.obtenerTodos();

        assertEquals(1, response.size());
        assertEquals("Estante 1", response.get(0).getNombreEstante());
    }

    @Test
    void obtenerPorId_DeberiaRetornarEstante() {
        when(estanteRepository.findById(1L)).thenReturn(Optional.of(new Estante(1L, "Estante 1")));

        Optional<EstanteResponseDTO> response = estanteService.obtenerPorId(1L);

        assertTrue(response.isPresent());
        assertEquals(1L, response.get().getIdEstante());
    }

    @Test
    void obtenerPorId_DeberiaRetornarVacio() {
        when(estanteRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<EstanteResponseDTO> response = estanteService.obtenerPorId(99L);

        assertTrue(response.isEmpty());
    }

    @Test
    void buscarPorNombre_DeberiaRetornarLista() {
        when(estanteRepository.buscarPorNombreParecido("1")).thenReturn(List.of(new Estante(1L, "Estante 1")));

        List<EstanteResponseDTO> response = estanteService.buscarPorNombre("1");

        assertEquals(1, response.size());
        assertEquals("Estante 1", response.get(0).getNombreEstante());
    }
}