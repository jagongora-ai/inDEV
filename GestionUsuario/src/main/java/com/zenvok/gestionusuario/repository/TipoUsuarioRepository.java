package com.zenvok.gestionusuario.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.zenvok.gestionusuario.model.TipoUsuario;

@Repository
public interface TipoUsuarioRepository extends JpaRepository<TipoUsuario, Long>{

    @Query("SELECT t FROM TipoUsuario t WHERE t.nombreTipo = :nombre")
    Optional<TipoUsuario> buscarPorNombre(@Param("nombre") String nombre);

}
