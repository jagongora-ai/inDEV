package com.zenvok.estado.service;

import com.zenvok.estado.dto.EstadoRequestDTO;
import com.zenvok.estado.dto.EstadoResponseDTO;
import com.zenvok.estado.exception.EstadoNotFoundException;
import com.zenvok.estado.model.Estado;
import com.zenvok.estado.repository.EstadoRepository;

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
class EstadoServiceTest {

    @Mock
    private EstadoRepository estadoRepository;

    @InjectMocks
    private EstadoService estadoService;

    @Test
    void listar_DeberiaRetornarListaDeEstados() {
        Estado estado = new Estado();
        estado.setId(1L);
        estado.setNombre("Activo");
        estado.setDescripcion("Estado activo");

        when(estadoRepository.findAll()).thenReturn(List.of(estado));

        List<EstadoResponseDTO> resultado = estadoService.listar();

        assertEquals(1, resultado.size());
        assertEquals(1L, resultado.get(0).getId());
        assertEquals("Activo", resultado.get(0).getNombre());
        assertEquals("Estado activo", resultado.get(0).getDescripcion());

        verify(estadoRepository).findAll();
    }

    @Test
    void buscarPorId_CuandoExiste_DeberiaRetornarEstado() {
        Estado estado = new Estado();
        estado.setId(1L);
        estado.setNombre("Activo");
        estado.setDescripcion("Estado activo");

        when(estadoRepository.findById(1L)).thenReturn(Optional.of(estado));

        EstadoResponseDTO resultado = estadoService.buscarPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Activo", resultado.getNombre());
        assertEquals("Estado activo", resultado.getDescripcion());

        verify(estadoRepository).findById(1L);
    }

    @Test
    void buscarPorId_CuandoNoExiste_DeberiaLanzarExcepcion() {
        when(estadoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EstadoNotFoundException.class, () -> estadoService.buscarPorId(99L));

        verify(estadoRepository).findById(99L);
    }

    @Test
    void buscarPorNombre_DeberiaRetornarEstadosCoincidentes() {
        Estado estado = new Estado();
        estado.setId(1L);
        estado.setNombre("Activo");
        estado.setDescripcion("Estado activo");

        when(estadoRepository.findByNombreContainingIgnoreCase("act"))
                .thenReturn(List.of(estado));

        List<EstadoResponseDTO> resultado = estadoService.buscarPorNombre("act");

        assertEquals(1, resultado.size());
        assertEquals("Activo", resultado.get(0).getNombre());

        verify(estadoRepository).findByNombreContainingIgnoreCase("act");
    }

    @Test
    void guardar_DeberiaCrearEstado() {
        EstadoRequestDTO request = new EstadoRequestDTO();
        request.setNombre("Activo");
        request.setDescripcion("Estado activo");

        Estado estadoGuardado = new Estado();
        estadoGuardado.setId(1L);
        estadoGuardado.setNombre("Activo");
        estadoGuardado.setDescripcion("Estado activo");

        when(estadoRepository.save(any(Estado.class))).thenReturn(estadoGuardado);

        EstadoResponseDTO resultado = estadoService.guardar(request);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Activo", resultado.getNombre());
        assertEquals("Estado activo", resultado.getDescripcion());

        verify(estadoRepository).save(any(Estado.class));
    }

    @Test
    void actualizar_CuandoExiste_DeberiaActualizarEstado() {
        Estado estadoExistente = new Estado();
        estadoExistente.setId(1L);
        estadoExistente.setNombre("Activo");
        estadoExistente.setDescripcion("Estado activo");

        EstadoRequestDTO request = new EstadoRequestDTO();
        request.setNombre("Inactivo");
        request.setDescripcion("Estado inactivo");

        Estado estadoActualizado = new Estado();
        estadoActualizado.setId(1L);
        estadoActualizado.setNombre("Inactivo");
        estadoActualizado.setDescripcion("Estado inactivo");

        when(estadoRepository.findById(1L)).thenReturn(Optional.of(estadoExistente));
        when(estadoRepository.save(any(Estado.class))).thenReturn(estadoActualizado);

        EstadoResponseDTO resultado = estadoService.actualizar(1L, request);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Inactivo", resultado.getNombre());
        assertEquals("Estado inactivo", resultado.getDescripcion());

        verify(estadoRepository).findById(1L);
        verify(estadoRepository).save(any(Estado.class));
    }

    @Test
    void actualizar_CuandoNoExiste_DeberiaLanzarExcepcion() {
        EstadoRequestDTO request = new EstadoRequestDTO();
        request.setNombre("Inactivo");
        request.setDescripcion("Estado inactivo");

        when(estadoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EstadoNotFoundException.class, () -> estadoService.actualizar(99L, request));

        verify(estadoRepository).findById(99L);
        verify(estadoRepository, never()).save(any(Estado.class));
    }

    @Test
    void eliminar_CuandoExiste_DeberiaEliminarEstado() {
        Estado estado = new Estado();
        estado.setId(1L);
        estado.setNombre("Activo");
        estado.setDescripcion("Estado activo");

        when(estadoRepository.findById(1L)).thenReturn(Optional.of(estado));

        estadoService.eliminar(1L);

        verify(estadoRepository).findById(1L);
        verify(estadoRepository).delete(estado);
    }

    @Test
    void eliminar_CuandoNoExiste_DeberiaLanzarExcepcion() {
        when(estadoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EstadoNotFoundException.class, () -> estadoService.eliminar(99L));

        verify(estadoRepository).findById(99L);
        verify(estadoRepository, never()).delete(any(Estado.class));
    }
}