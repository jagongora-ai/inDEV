package com.zenvok.Gest_Bode.service;

import com.zenvok.Gest_Bode.dto.PasEstRequestDTO;
import com.zenvok.Gest_Bode.dto.PasEstResponseDTO;
import com.zenvok.Gest_Bode.model.Estante;
import com.zenvok.Gest_Bode.model.Pasillo;
import com.zenvok.Gest_Bode.model.Pas_Est;
import com.zenvok.Gest_Bode.repository.EstanteRepository;
import com.zenvok.Gest_Bode.repository.PasEstRepository;
import com.zenvok.Gest_Bode.repository.PasilloRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PasEstService {

    private final PasEstRepository pasEstRepository;
    private final PasilloRepository pasilloRepository;
    private final EstanteRepository estanteRepository;

    public PasEstResponseDTO guardar(PasEstRequestDTO dto) {
        log.info("Creando relación pasillo-estante. Pasillo id: {}, Estante id: {}", dto.getPasilloId(), dto.getEstanteId());

        Pasillo pasillo = pasilloRepository.findById(dto.getPasilloId())
                .orElseThrow(() -> {
                    log.warn("Pasillo no encontrado con id: {}", dto.getPasilloId());
                    return new RuntimeException("No se encontró el Pasillo con ID: " + dto.getPasilloId());
                });

        Estante estante = estanteRepository.findById(dto.getEstanteId())
                .orElseThrow(() -> {
                    log.warn("Estante no encontrado con id: {}", dto.getEstanteId());
                    return new RuntimeException("No se encontró el Estante con ID: " + dto.getEstanteId());
                });

        Pas_Est pasEst = new Pas_Est();
        pasEst.setPasillo(pasillo);
        pasEst.setEstante(estante);

        Pas_Est guardado = pasEstRepository.save(pasEst);

        log.info("Relación pasillo-estante creada correctamente con id: {}", guardado.getIdPasEst());

        return mapearAResponse(guardado);
    }

    public List<PasEstResponseDTO> obtenerTodos() {
        log.info("Listando todas las relaciones pasillo-estante");

        List<PasEstResponseDTO> relaciones = pasEstRepository.findAll()
                .stream()
                .map(this::mapearAResponse)
                .collect(Collectors.toList());

        log.info("Total de relaciones encontradas: {}", relaciones.size());

        return relaciones;
    }

    public Optional<PasEstResponseDTO> obtenerPorId(Long id) {
        log.info("Buscando relación pasillo-estante con id: {}", id);

        Optional<PasEstResponseDTO> relacion = pasEstRepository.findById(id)
                .map(this::mapearAResponse);

        if (relacion.isEmpty()) {
            log.warn("Relación pasillo-estante no encontrada con id: {}", id);
        }

        return relacion;
    }

    public List<PasEstResponseDTO> listarEstantesPorPasillo(Long idPasillo) {
        log.info("Buscando estantes del pasillo con id: {}", idPasillo);

        List<PasEstResponseDTO> relaciones = pasEstRepository.findByPasilloIdPasillo(idPasillo)
                .stream()
                .map(this::mapearAResponse)
                .collect(Collectors.toList());

        log.info("Total de estantes encontrados para el pasillo {}: {}", idPasillo, relaciones.size());

        return relaciones;
    }

    private PasEstResponseDTO mapearAResponse(Pas_Est entity) {
        PasEstResponseDTO res = new PasEstResponseDTO();
        res.setIdPasEst(entity.getIdPasEst());
        res.setPasilloId(entity.getPasillo().getIdPasillo());
        res.setNombrePasillo(entity.getPasillo().getNombrePasillo());
        res.setEstanteId(entity.getEstante().getIdEstante());
        res.setNombreEstante(entity.getEstante().getNombreEstante());
        return res;
    }
}