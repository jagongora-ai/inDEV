package com.zenvok.envios.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.zenvok.envios.model.Envio;
import com.zenvok.envios.repository.EnvioRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final EnvioRepository envioRepository;

    @Override
    public void run(String... args) {

        if (envioRepository.count() > 0) {
            log.info("Envios ya cargados. Se omite inicialización.");
            return;
        }

        log.info("Cargando envíos iniciales...");

        Envio envio1 = new Envio();
        envio1.setFechaEnvio("2026-05-24");
        envio1.setFechaEmbargue("2026-05-25");
        envio1.setComentarios("Envío de notebook Lenovo");
        envio1.setDireccionId(1L);
        envio1.setVentaId(1L);
        envio1.setEstadoId(1L);
        envioRepository.save(envio1);

        Envio envio2 = new Envio();
        envio2.setFechaEnvio("2026-05-24");
        envio2.setFechaEmbargue("2026-05-26");
        envio2.setComentarios("Envío de aspiradora");
        envio2.setDireccionId(2L);
        envio2.setVentaId(2L);
        envio2.setEstadoId(3L);
        envioRepository.save(envio2);

        log.info("Envíos iniciales cargados correctamente.");
    }
}