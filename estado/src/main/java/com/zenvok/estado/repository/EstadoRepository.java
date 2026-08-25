package com.zenvok.estado.repository;

import com.zenvok.estado.model.Estado;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstadoRepository extends JpaRepository<Estado, Long> {

    List<Estado> findByNombreContainingIgnoreCase(String nombre);
}