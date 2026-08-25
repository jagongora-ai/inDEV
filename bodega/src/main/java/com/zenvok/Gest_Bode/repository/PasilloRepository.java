package com.zenvok.Gest_Bode.repository;

import com.zenvok.Gest_Bode.model.Pasillo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PasilloRepository extends JpaRepository<Pasillo, Long> {

    @Query("SELECT p FROM Pasillo p WHERE LOWER(p.nombrePasillo) LIKE LOWER(CONCAT('%', :nombre, '%'))")
    List<Pasillo> buscarPorNombreParecido(@Param("nombre") String nombre);
}