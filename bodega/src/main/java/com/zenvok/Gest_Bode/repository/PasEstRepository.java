package com.zenvok.Gest_Bode.repository;

import com.zenvok.Gest_Bode.model.Pas_Est;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PasEstRepository extends JpaRepository<Pas_Est, Long> {
    
    List<Pas_Est> findByPasilloIdPasillo(Long idPasillo);
}
