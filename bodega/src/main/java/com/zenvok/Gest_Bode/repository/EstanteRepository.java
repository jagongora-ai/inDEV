package com.zenvok.Gest_Bode.repository;

import com.zenvok.Gest_Bode.model.Estante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EstanteRepository extends JpaRepository<Estante, Long> {

    @Query("SELECT e FROM Estante e WHERE LOWER(e.nombreEstante) LIKE LOWER(CONCAT('%', :nombre, '%'))")
    List<Estante> buscarPorNombreParecido(@Param("nombre") String nombre);
}
