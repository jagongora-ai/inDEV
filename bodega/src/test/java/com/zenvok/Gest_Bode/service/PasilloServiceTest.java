package com.zenvok.Gest_Bode.service;

import com.zenvok.Gest_Bode.dto.PasilloRequestDTO;
import com.zenvok.Gest_Bode.dto.PasilloResponseDTO;
import com.zenvok.Gest_Bode.model.Pasillo;
import com.zenvok.Gest_Bode.repository.PasilloRepository;
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
class PasilloServiceTest {

    @Mock
    private PasilloRepository pasilloRepository;

    @InjectMocks
    private PasilloService pasilloService;

    @Test
    void guardar_DeberiaCrearPasillo() {
        PasilloRequestDTO request = new PasilloRequestDTO("Pasillo A");
        Pasillo pasillo = new Pasillo(1L, "Pasillo A");

        when(pasilloRepository.save(any(Pasillo.class))).thenReturn(pasillo);

        PasilloResponseDTO response = pasilloService.guardar(request);

        assertNotNull(response);
        assertEquals(1L, response.getIdPasillo());
        assertEquals("Pasillo A", response.getNombrePasillo());
        verify(pasilloRepository, times(1)).save(any(Pasillo.class));
    }

    @Test
    void obtenerTodos_DeberiaRetornarLista() {
        when(pasilloRepository.findAll()).thenReturn(List.of(new Pasillo(1L, "Pasillo A")));

        List<PasilloResponseDTO> response = pasilloService.obtenerTodos();

        assertEquals(1, response.size());
        assertEquals("Pasillo A", response.get(0).getNombrePasillo());
    }

    @Test
    void obtenerPorId_DeberiaRetornarPasillo() {
        when(pasilloRepository.findById(1L)).thenReturn(Optional.of(new Pasillo(1L, "Pasillo A")));

        Optional<PasilloResponseDTO> response = pasilloService.obtenerPorId(1L);

        assertTrue(response.isPresent());
        assertEquals(1L, response.get().getIdPasillo());
    }

    @Test
    void obtenerPorId_DeberiaRetornarVacio() {
        when(pasilloRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<PasilloResponseDTO> response = pasilloService.obtenerPorId(99L);

        assertTrue(response.isEmpty());
    }

    @Test
    void buscarPorNombre_DeberiaRetornarLista() {
        when(pasilloRepository.buscarPorNombreParecido("A")).thenReturn(List.of(new Pasillo(1L, "Pasillo A")));

        List<PasilloResponseDTO> response = pasilloService.buscarPorNombre("A");

        assertEquals(1, response.size());
        assertEquals("Pasillo A", response.get(0).getNombrePasillo());
    }
}