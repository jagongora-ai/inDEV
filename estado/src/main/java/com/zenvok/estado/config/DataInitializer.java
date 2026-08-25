package com.zenvok.estado.config;

import com.zenvok.estado.model.Estado;
import com.zenvok.estado.repository.EstadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

// Carga datos iniciales al iniciar la aplicación
@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private EstadoRepository estadoRepository;

    @Override
    public void run(String... args) {

        if (estadoRepository.count() > 0) {
            System.out.println("La BD ya tiene estados, no se insertan datos iniciales");
            return;
        }

        System.out.println("Insertando estados iniciales...");

        Estado activo = new Estado();
        activo.setNombre("Activo");
        activo.setDescripcion("Registro activo en el sistema");
        estadoRepository.save(activo);

        Estado inactivo = new Estado();
        inactivo.setNombre("Inactivo");
        inactivo.setDescripcion("Registro inactivo en el sistema");
        estadoRepository.save(inactivo);

        Estado pendiente = new Estado();
        pendiente.setNombre("Pendiente");
        pendiente.setDescripcion("Estado pendiente de gestión");
        estadoRepository.save(pendiente);

        Estado entregado = new Estado();
        entregado.setNombre("Entregado");
        entregado.setDescripcion("Estado finalizado o entregado");
        estadoRepository.save(entregado);

        System.out.println("Estados iniciales insertados correctamente");
    }
}