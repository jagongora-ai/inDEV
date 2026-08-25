package com.zenvok.Gest_Bode.repository;

import com.zenvok.Gest_Bode.model.ProdPasEst;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ProdPasEstRepository extends JpaRepository<ProdPasEst, Long> {

    @Query("SELECT p FROM ProdPasEst p WHERE p.productoId = :idProducto")
    Optional<ProdPasEst> buscarUbicacionDeProducto(@Param("idProducto") Long idProducto);
}