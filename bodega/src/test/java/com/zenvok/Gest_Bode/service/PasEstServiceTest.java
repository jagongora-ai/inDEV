package com.zenvok.Gest_Bode.service;

import com.zenvok.Gest_Bode.dto.PasEstRequestDTO;
import com.zenvok.Gest_Bode.dto.PasEstResponseDTO;
import com.zenvok.Gest_Bode.model.Estante;
import com.zenvok.Gest_Bode.model.Pasillo;
import com.zenvok.Gest_Bode.model.Pas_Est;
import com.zenvok.Gest_Bode.repository.EstanteRepository;
import com.zenvok.Gest_Bode.repository.PasEstRepository;
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
class PasEstServiceTest {

    @Mock
    private PasEstRepository pasEstRepository;

    @Mock
    private PasilloRepository pasilloRepository;

    @Mock
    private EstanteRepository estanteRepository;

    @InjectMocks
    private PasEstService pasEstService;

    @Test
    void guardar_DeberiaCrearRelacion() {
        Pasillo pasillo = new Pasillo(1L, "Pasillo A");
        Estante estante = new Estante(1L, "Estante 1");
        Pas_Est relacion = new Pas_Est(1L, pasillo, estante);
        PasEstRequestDTO request = new PasEstRequestDTO(1L, 1L);

        when(pasilloRepository.findById(1L)).thenReturn(Optional.of(pasillo));
        when(estanteRepository.findById(1L)).thenReturn(Optional.of(estante));
        when(pasEstRepository.save(any(Pas_Est.class))).thenReturn(relacion);

        PasEstResponseDTO response = pasEstService.guardar(request);

        assertNotNull(response);
        assertEquals(1L, response.getIdPasEst());
        assertEquals("Pasillo A", response.getNombrePasillo());
        assertEquals("Estante 1", response.getNombreEstante());
    }

    @Test
    void guardar_DeberiaLanzarError_CuandoPasilloNoExiste() {
        PasEstRequestDTO request = new PasEstRequestDTO(99L, 1L);

        when(pasilloRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> pasEstService.guardar(request));

        assertTrue(exception.getMessage().contains("Pasillo"));
    }

    @Test
    void guardar_DeberiaLanzarError_CuandoEstanteNoExiste() {
        Pasillo pasillo = new Pasillo(1L, "Pasillo A");
        PasEstRequestDTO request = new PasEstRequestDTO(1L, 99L);

        when(pasilloRepository.findById(1L)).thenReturn(Optional.of(pasillo));
        when(estanteRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> pasEstService.guardar(request));

        assertTrue(exception.getMessage().contains("Estante"));
    }

    @Test
    void obtenerTodos_DeberiaRetornarLista() {
        Pasillo pasillo = new Pasillo(1L, "Pasillo A");
        Estante estante = new Estante(1L, "Estante 1");
        Pas_Est relacion = new Pas_Est(1L, pasillo, estante);

        when(pasEstRepository.findAll()).thenReturn(List.of(relacion));

        List<PasEstResponseDTO> response = pasEstService.obtenerTodos();

        assertEquals(1, response.size());
        assertEquals("Pasillo A", response.get(0).getNombrePasillo());
    }

    @Test
    void obtenerPorId_DeberiaRetornarRelacion() {
        Pasillo pasillo = new Pasillo(1L, "Pasillo A");
        Estante estante = new Estante(1L, "Estante 1");
        Pas_Est relacion = new Pas_Est(1L, pasillo, estante);

        when(pasEstRepository.findById(1L)).thenReturn(Optional.of(relacion));

        Optional<PasEstResponseDTO> response = pasEstService.obtenerPorId(1L);

        assertTrue(response.isPresent());
        assertEquals(1L, response.get().getIdPasEst());
    }

    @Test
    void listarEstantesPorPasillo_DeberiaRetornarLista() {
        Pasillo pasillo = new Pasillo(1L, "Pasillo A");
        Estante estante = new Estante(1L, "Estante 1");
        Pas_Est relacion = new Pas_Est(1L, pasillo, estante);

        when(pasEstRepository.findByPasilloIdPasillo(1L)).thenReturn(List.of(relacion));

        List<PasEstResponseDTO> response = pasEstService.listarEstantesPorPasillo(1L);

        assertEquals(1, response.size());
        assertEquals(1L, response.get(0).getPasilloId());
    }
}