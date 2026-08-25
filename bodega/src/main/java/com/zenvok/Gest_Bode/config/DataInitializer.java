package com.zenvok.Gest_Bode.config;

import com.zenvok.Gest_Bode.model.Estante;
import com.zenvok.Gest_Bode.model.Pas_Est;
import com.zenvok.Gest_Bode.model.Pasillo;
import com.zenvok.Gest_Bode.model.ProdPasEst;
import com.zenvok.Gest_Bode.repository.EstanteRepository;
import com.zenvok.Gest_Bode.repository.PasEstRepository;
import com.zenvok.Gest_Bode.repository.PasilloRepository;
import com.zenvok.Gest_Bode.repository.ProdPasEstRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final PasilloRepository pasilloRepository;
    private final EstanteRepository estanteRepository;
    private final PasEstRepository pasEstRepository;
    private final ProdPasEstRepository prodPasEstRepository;

    @Override
    public void run(String... args) {

        if (pasilloRepository.count() > 0 && prodPasEstRepository.count() > 0) {
            log.info("Datos de bodega ya cargados. Se omite inicialización.");
            return;
        }

        log.info("Cargando datos iniciales de Gestión Bodega...");

        Pasillo pasilloA = new Pasillo();
        pasilloA.setNombrePasillo("Pasillo A - Tecnología");
        Pasillo pasilloAGuardado = pasilloRepository.save(pasilloA);

        Pasillo pasilloB = new Pasillo();
        pasilloB.setNombrePasillo("Pasillo B - Hogar");
        Pasillo pasilloBGuardado = pasilloRepository.save(pasilloB);

        Estante estante1 = new Estante();
        estante1.setNombreEstante("Estante 1 - Superior");
        Estante estante1Guardado = estanteRepository.save(estante1);

        Estante estante2 = new Estante();
        estante2.setNombreEstante("Estante 2 - Inferior");
        Estante estante2Guardado = estanteRepository.save(estante2);

        Pas_Est ubicacion1 = new Pas_Est();
        ubicacion1.setPasillo(pasilloAGuardado);
        ubicacion1.setEstante(estante1Guardado);
        Pas_Est ubicacion1Guardada = pasEstRepository.save(ubicacion1);

        Pas_Est ubicacion2 = new Pas_Est();
        ubicacion2.setPasillo(pasilloBGuardado);
        ubicacion2.setEstante(estante2Guardado);
        Pas_Est ubicacion2Guardada = pasEstRepository.save(ubicacion2);

        ProdPasEst producto1 = new ProdPasEst();
        producto1.setProductoId(1L);
        producto1.setPasEst(ubicacion1Guardada);
        prodPasEstRepository.save(producto1);

        ProdPasEst producto2 = new ProdPasEst();
        producto2.setProductoId(2L);
        producto2.setPasEst(ubicacion2Guardada);
        prodPasEstRepository.save(producto2);

        log.info("Datos iniciales de Gestión Bodega cargados correctamente.");
    }
}