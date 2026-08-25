package com.zenvok.producto.service;

import com.zenvok.producto.dto.CategoriaRequestDTO;
import com.zenvok.producto.dto.CategoriaResponseDTO;
import com.zenvok.producto.exception.CategoriaNotFoundException;
import com.zenvok.producto.model.Categoria;
import com.zenvok.producto.repository.CategoriaRepository;

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
class CategoriaServiceTest {

    @Mock
    private CategoriaRepository categoriaRepository;

    @InjectMocks
    private CategoriaService categoriaService;

    @Test
    void listar_DeberiaRetornarListaDeCategorias() {
        Categoria categoria = crearCategoria();

        when(categoriaRepository.findAll()).thenReturn(List.of(categoria));

        List<CategoriaResponseDTO> resultado = categoriaService.listar();

        assertEquals(1, resultado.size());
        assertEquals(1L, resultado.get(0).getId());
        assertEquals("Tecnología", resultado.get(0).getNombre());
        assertEquals("Productos tecnológicos", resultado.get(0).getDescripcion());

        verify(categoriaRepository).findAll();
    }

    @Test
    void buscarPorId_CuandoExiste_DeberiaRetornarCategoria() {
        Categoria categoria = crearCategoria();

        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));

        CategoriaResponseDTO resultado = categoriaService.buscarPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Tecnología", resultado.getNombre());
        assertEquals("Productos tecnológicos", resultado.getDescripcion());

        verify(categoriaRepository).findById(1L);
    }

    @Test
    void buscarPorId_CuandoNoExiste_DeberiaLanzarExcepcion() {
        when(categoriaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(CategoriaNotFoundException.class, () -> categoriaService.buscarPorId(99L));

        verify(categoriaRepository).findById(99L);
    }

    @Test
    void guardar_DeberiaCrearCategoria() {
        CategoriaRequestDTO request = crearRequest();
        Categoria categoriaGuardada = crearCategoria();

        when(categoriaRepository.save(any(Categoria.class))).thenReturn(categoriaGuardada);

        CategoriaResponseDTO resultado = categoriaService.guardar(request);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Tecnología", resultado.getNombre());
        assertEquals("Productos tecnológicos", resultado.getDescripcion());

        verify(categoriaRepository).save(any(Categoria.class));
    }

    @Test
    void actualizar_CuandoExiste_DeberiaActualizarCategoria() {
        Categoria categoriaExistente = crearCategoria();

        CategoriaRequestDTO request = new CategoriaRequestDTO();
        request.setNombre("Hogar");
        request.setDescripcion("Productos para el hogar");

        Categoria categoriaActualizada = new Categoria();
        categoriaActualizada.setId(1L);
        categoriaActualizada.setNombre("Hogar");
        categoriaActualizada.setDescripcion("Productos para el hogar");

        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoriaExistente));
        when(categoriaRepository.save(any(Categoria.class))).thenReturn(categoriaActualizada);

        CategoriaResponseDTO resultado = categoriaService.actualizar(1L, request);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Hogar", resultado.getNombre());
        assertEquals("Productos para el hogar", resultado.getDescripcion());

        verify(categoriaRepository).findById(1L);
        verify(categoriaRepository).save(any(Categoria.class));
    }

    @Test
    void actualizar_CuandoNoExiste_DeberiaLanzarExcepcion() {
        CategoriaRequestDTO request = crearRequest();

        when(categoriaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(CategoriaNotFoundException.class, () -> categoriaService.actualizar(99L, request));

        verify(categoriaRepository).findById(99L);
        verify(categoriaRepository, never()).save(any(Categoria.class));
    }

    @Test
    void eliminar_CuandoExiste_DeberiaEliminarCategoria() {
        Categoria categoria = crearCategoria();

        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));

        categoriaService.eliminar(1L);

        verify(categoriaRepository).findById(1L);
        verify(categoriaRepository).delete(categoria);
    }

    @Test
    void eliminar_CuandoNoExiste_DeberiaLanzarExcepcion() {
        when(categoriaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(CategoriaNotFoundException.class, () -> categoriaService.eliminar(99L));

        verify(categoriaRepository).findById(99L);
        verify(categoriaRepository, never()).delete(any(Categoria.class));
    }

    private CategoriaRequestDTO crearRequest() {
        CategoriaRequestDTO request = new CategoriaRequestDTO();
        request.setNombre("Tecnología");
        request.setDescripcion("Productos tecnológicos");
        return request;
    }

    private Categoria crearCategoria() {
        Categoria categoria = new Categoria();
        categoria.setId(1L);
        categoria.setNombre("Tecnología");
        categoria.setDescripcion("Productos tecnológicos");
        return categoria;
    }
}