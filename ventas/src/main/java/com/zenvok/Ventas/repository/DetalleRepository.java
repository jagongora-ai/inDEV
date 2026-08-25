package com.zenvok.Ventas.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.zenvok.Ventas.model.Detalle;

import org.springframework.data.repository.query.Param;

public interface DetalleRepository extends JpaRepository<Detalle, Long>{
    List<Detalle> findByVentaIdVenta(Long idVenta);

    @Query("SELECT d FROM Detalle d WHERE d.venta.idVenta = :idVenta")
    List<Detalle> buscarDetallesPorVentaId(@Param("idVenta") Long idVenta);

}
