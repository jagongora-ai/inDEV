package com.zenvok.producto.repository;

import com.zenvok.producto.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    List<Producto> findByNombreContainingIgnoreCase(String nombre);
    
    List<Producto> findByCategoriaId(Long categoriaId); 

    List<Producto> findByEstadoId(Long estadoId);
}