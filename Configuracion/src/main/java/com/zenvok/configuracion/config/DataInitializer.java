package com.zenvok.configuracion.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.zenvok.configuracion.model.Configuracion;
import com.zenvok.configuracion.repository.ConfiguracionRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final ConfiguracionRepository configuracionRepository;

    @Override
    public void run(String... args) throws Exception {
        if (configuracionRepository.count() > 0) {
            log.info("Las configuraciones del sistema ya existen en la base de datos. Se omite este paso.");
            return;
        }

        log.info("Cargando parametros iniciales de Configuracion...");

        Configuracion iva = new Configuracion();
        iva.setClave("iva");
        iva.setValor("19"); 
        configuracionRepository.save(iva);

        Configuracion costoEnvio = new Configuracion();
        costoEnvio.setClave("costo_envio_base");
        costoEnvio.setValor("3500"); 
        configuracionRepository.save(costoEnvio);

        Configuracion comision = new Configuracion();
        comision.setClave("comision_servicio");
        comision.setValor("1500"); 
        configuracionRepository.save(comision);

        log.info("¡Parametros del sistema logistico cargados exitosamente en XAMPP!");
    }
}