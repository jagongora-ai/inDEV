package com.zenvok.Ventas.config;

import com.zenvok.Ventas.model.Detalle;
import com.zenvok.Ventas.model.Ventas;
import com.zenvok.Ventas.repository.DetalleRepository;
import com.zenvok.Ventas.repository.VentasRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final VentasRepository ventasRepository;
    private final DetalleRepository detalleRepository;

    @Override
    public void run(String... args) {

        if (ventasRepository.count() > 0) {
            log.info("Ventas ya cargadas en la base de datos, se omite la inicialización.");
            return;
        }

        log.info("Cargando datos iniciales de ventas y detalles...");

        Ventas v1 = new Ventas();
        v1.setFechaVentas(LocalDate.now());
        v1.setTotal(new BigDecimal("15500.00"));
        v1.setUsuarioId(1L);
        v1.setEstadoId(1L);
        Ventas v1Guardada = ventasRepository.save(v1);

        Detalle d1 = new Detalle();
        d1.setCantidad(2);
        d1.setSubtotal(new BigDecimal("7750.00"));
        d1.setProductoId(1L);
        d1.setVenta(v1Guardada);
        detalleRepository.save(d1);

        Ventas v2 = new Ventas();
        v2.setFechaVentas(LocalDate.now().minusDays(1));
        v2.setTotal(new BigDecimal("4500.50"));
        v2.setUsuarioId(1L);
        v2.setEstadoId(1L);
        Ventas v2Guardada = ventasRepository.save(v2);

        Detalle d2 = new Detalle();
        d2.setCantidad(1);
        d2.setSubtotal(new BigDecimal("4500.50"));
        d2.setProductoId(2L);
        d2.setVenta(v2Guardada);
        detalleRepository.save(d2);

        log.info("Ventas y detalles iniciales cargados con éxito");
    }
}