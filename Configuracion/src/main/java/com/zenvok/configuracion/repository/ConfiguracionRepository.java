package com.zenvok.configuracion.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zenvok.configuracion.model.Configuracion;


@Repository
public interface ConfiguracionRepository extends JpaRepository<Configuracion, Long>{

    Optional<Configuracion> findByClave(String clave);

}
