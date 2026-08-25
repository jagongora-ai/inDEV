package com.zenvok.Ventas.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import com.zenvok.Ventas.model.Ventas;

@DataJpaTest
@ActiveProfiles("test")
@DisplayName("Test de repositorio de Ventas en memoria")
public class VentasRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private VentasRepository ventasRepository;

    private Ventas venta1;
    private Ventas venta2;

    @BeforeEach
    void setUp() {
        venta1 = new Ventas();
        venta1.setFechaVentas(LocalDate.of(2026, 6, 20));
        venta1.setTotal(new BigDecimal("1500.50"));
        venta1.setUsuarioId(1L);
        venta1.setEstadoId(2L);
        entityManager.persist(venta1);

        venta2 = new Ventas();
        venta2.setFechaVentas(LocalDate.of(2026, 6, 21));
        venta2.setTotal(new BigDecimal("450.00"));
        venta2.setUsuarioId(2L);
        venta2.setEstadoId(2L);
        entityManager.persist(venta2);
        entityManager.flush();
    }

    @Test
    @DisplayName("findAll() debe retornar la lista con todas las ventas insertadas")
    void findAll_debeRetornarTodasLasVentas() {
        List<Ventas> ventas = ventasRepository.findAll();
        assertNotNull(ventas);
        assertEquals(2, ventas.size(), "Deberían existir exactamente 2 ventas en la base de datos");
    }

    @Test
    @DisplayName("findById() debe retornar la venta correcta cuando el ID existe")
    void findById_debeRetornarVenta_cuandoExiste() {
        Optional<Ventas> resultado = ventasRepository.findById(venta1.getIdVenta());
        assertTrue(resultado.isPresent(), "La venta debería ser encontrada");
        assertEquals(new BigDecimal("1500.50"), resultado.get().getTotal());
        assertEquals(1L, resultado.get().getUsuarioId());
    }

    @Test
    @DisplayName("findById() debe retornar un Optional vacío si el ID buscado no existe")
    void findById_debeRetornarVacio_cuandoNoExiste() {
        Optional<Ventas> resultado = ventasRepository.findById(999L);

        assertFalse(resultado.isPresent(), "El resultado debería estar vacío para un ID falso");
    }
}