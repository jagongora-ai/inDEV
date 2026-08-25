package com.zenvok.envios.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.zenvok.envios.model.Envio;

public interface EnvioRepository extends JpaRepository<Envio, Long> {

    @Query("SELECT e FROM Envio e WHERE e.ventaId = :ventaId")
    List<Envio> findByVentaId(@Param("ventaId") Long ventaId);

    @Query("SELECT e FROM Envio e WHERE e.direccionId = :direccionId")
    List<Envio> findByDireccionId(@Param("direccionId") Long direccionId);

    @Query("SELECT e FROM Envio e WHERE e.estadoId = :estadoId")
    List<Envio> findByEstadoId(@Param("estadoId") Long estadoId);
}
