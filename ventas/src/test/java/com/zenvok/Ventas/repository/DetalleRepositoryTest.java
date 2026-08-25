package com.zenvok.Ventas.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import com.zenvok.Ventas.model.Detalle;
import com.zenvok.Ventas.model.Ventas;

@DataJpaTest
@ActiveProfiles("test")
@DisplayName("Test de repositorio de Detalle en memoria")
public class DetalleRepositoryTest {
    
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private DetalleRepository detalleRepository;

    private Ventas venta;
    private Detalle detalle1;
    private Detalle detalle2;

    @BeforeEach
    void setUp() {
        venta = new Ventas();
        venta.setFechaVentas(LocalDate.of(2026, 6, 20));
        venta.setTotal(new BigDecimal("1500.50"));
        venta.setUsuarioId(1L);
        venta.setEstadoId(2L);
        entityManager.persist(venta);

        detalle1 = new Detalle();
        detalle1.setProductoId(100L);
        detalle1.setCantidad(2);
        detalle1.setSubtotal(new BigDecimal("500.25"));
        detalle1.setVenta(venta);
        entityManager.persist(detalle1);

        detalle2 = new Detalle();
        detalle2.setProductoId(101L);
        detalle2.setCantidad(1);
        detalle2.setSubtotal(new BigDecimal("500.25"));
        detalle2.setVenta(venta);
        entityManager.persist(detalle2);

        entityManager.flush();
    }

    @Test
    @DisplayName("findByVentaIdVenta() debe retornar la lista de detalles asociados a una venta")
    void findByVentaId_debeRetornarDetalles_cuandoExisten() {
        List<Detalle> detalles = detalleRepository.findByVentaIdVenta(venta.getIdVenta());
        assertNotNull(detalles);
        assertEquals(2, detalles.size(), "La venta debería tener exactamente 2 detalles");
        assertTrue(detalles.stream().anyMatch(d -> d.getProductoId().equals(100L)));
        assertTrue(detalles.stream().anyMatch(d -> d.getProductoId().equals(101L)));
    }

    @Test
    @DisplayName("findByVentaIdVenta() debe retornar lista vacía cuando el ID de la venta no existe")
    void findByVentaId_debeRetornarListaVacia_cuandoNoExisten() {
        List<Detalle> detalles = detalleRepository.findByVentaIdVenta(-1L);
        assertNotNull(detalles);
        assertTrue(detalles.isEmpty(), "La lista debería estar vacía para un ID inválido");
    }
}