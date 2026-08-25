package com.zenvok.producto.config;

import com.zenvok.producto.model.Categoria;
import com.zenvok.producto.model.Producto;
import com.zenvok.producto.repository.CategoriaRepository;
import com.zenvok.producto.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Override
    public void run(String... args) {

        
        if (categoriaRepository.count() > 0) {
            System.out.println("La BD ya tiene datos, no se insertan datos iniciales");
            return;
        }

        System.out.println("Insertando datos iniciales...");

        
        Categoria cat1 = new Categoria();
        cat1.setNombre("Tecnología");
        cat1.setDescripcion("Productos electrónicos");
        categoriaRepository.save(cat1);

        Categoria cat2 = new Categoria();
        cat2.setNombre("Hogar");
        cat2.setDescripcion("Productos para el hogar");
        categoriaRepository.save(cat2);


        Producto p1 = new Producto();
        p1.setNombre("Notebook Lenovo");
        p1.setDescripcion("Notebook oficina");
        p1.setPrecio(new BigDecimal("450000"));
        p1.setEstadoId(1L);
        p1.setCategoria(cat1);
        productoRepository.save(p1);

        Producto p2 = new Producto();
        p2.setNombre("Aspiradora");
        p2.setDescripcion("Aspiradora potente");
        p2.setPrecio(new BigDecimal("120000"));
        p2.setEstadoId(1L);
        p2.setCategoria(cat2);
        productoRepository.save(p2);

        System.out.println("Datos insertados correctamente");
    }
}