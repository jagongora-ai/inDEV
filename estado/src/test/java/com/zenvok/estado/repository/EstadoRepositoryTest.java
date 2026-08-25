package com.zenvok.estado.repository;

import com.zenvok.estado.model.Estado;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class EstadoRepositoryTest {

    @Autowired
    private EstadoRepository estadoRepository;

    @Test
    void save_DeberiaGuardarEstado() {
    
        Estado estado = new Estado();
        estado.setNombre("Pendiente");
        estado.setDescripcion("Estado pendiente");

    
        Estado guardado = estadoRepository.save(estado);

        
        assertNotNull(guardado.getId());
        assertEquals("Pendiente", guardado.getNombre());
        assertEquals("Estado pendiente", guardado.getDescripcion());
    }

    @Test
    void findById_CuandoExiste_DeberiaRetornarEstado() {

        Estado estado = new Estado();
        estado.setNombre("Activo");
        estado.setDescripcion("Estado activo");

        Estado guardado = estadoRepository.save(estado);


        Optional<Estado> resultado = estadoRepository.findById(guardado.getId());


        assertTrue(resultado.isPresent());
        assertEquals("Activo", resultado.get().getNombre());
        assertEquals("Estado activo", resultado.get().getDescripcion());
    }

    @Test
    void findByNombreContainingIgnoreCase_DeberiaRetornarCoincidencias() {
        
        Estado estado = new Estado();
        estado.setNombre("Activo");
        estado.setDescripcion("Estado activo");

        estadoRepository.save(estado);


        List<Estado> resultado = estadoRepository.findByNombreContainingIgnoreCase("act");


        assertFalse(resultado.isEmpty());
        assertEquals("Activo", resultado.get(0).getNombre());
    }

    @Test
    void findByNombreContainingIgnoreCase_CuandoNoExiste_DeberiaRetornarListaVacia() {

        List<Estado> resultado = estadoRepository.findByNombreContainingIgnoreCase("noexiste");

        assertTrue(resultado.isEmpty());
    }

    @Test
    void delete_DeberiaEliminarEstado() {
        Estado estado = new Estado();
        estado.setNombre("Temporal");
        estado.setDescripcion("Estado temporal");

        Estado guardado = estadoRepository.save(estado);

        estadoRepository.delete(guardado);

        Optional<Estado> resultado = estadoRepository.findById(guardado.getId());
        assertTrue(resultado.isEmpty());
    }
}