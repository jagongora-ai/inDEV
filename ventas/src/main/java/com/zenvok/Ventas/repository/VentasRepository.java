package com.zenvok.Ventas.repository;
import com.zenvok.Ventas.model.Ventas;

import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface VentasRepository extends JpaRepository<Ventas, Long> {    
    List<Ventas> findByFechaVentas(LocalDate fechaVentas);

    @Query("SELECT v FROM Ventas v WHERE v.total >= :totalMinimo")
    List<Ventas> buscarVentasMayoresA(@Param("totalMinimo") BigDecimal totalMinimo);

    List<Ventas> findByUsuarioId(long l);
}
