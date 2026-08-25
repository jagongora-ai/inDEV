package com.zenvok.Gest_Bode.service;

import com.zenvok.Gest_Bode.dto.EstanteRequestDTO;
import com.zenvok.Gest_Bode.dto.EstanteResponseDTO;
import com.zenvok.Gest_Bode.model.Estante;
import com.zenvok.Gest_Bode.repository.EstanteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EstanteService {

    private final EstanteRepository estanteRepository;

    public EstanteResponseDTO guardar(EstanteRequestDTO dto) {
        log.info("Creando estante con nombre: {}", dto.getNombreEstante());

        Estante estante = new Estante();
        estante.setNombreEstante(dto.getNombreEstante());

        Estante guardado = estanteRepository.save(estante);

        log.info("Estante creado correctamente con id: {}", guardado.getIdEstante());

        return mapearAResponse(guardado);
    }

    public List<EstanteResponseDTO> obtenerTodos() {
        log.info("Listando todos los estantes");

        List<EstanteResponseDTO> estantes = estanteRepository.findAll()
                .stream()
                .map(this::mapearAResponse)
                .collect(Collectors.toList());

        log.info("Total de estantes encontrados: {}", estantes.size());

        return estantes;
    }

    public Optional<EstanteResponseDTO> obtenerPorId(Long id) {
        log.info("Buscando estante con id: {}", id);

        Optional<EstanteResponseDTO> estante = estanteRepository.findById(id)
                .map(this::mapearAResponse);

        if (estante.isEmpty()) {
            log.warn("Estante no encontrado con id: {}", id);
        }

        return estante;
    }

    public List<EstanteResponseDTO> buscarPorNombre(String nombre) {
        log.info("Buscando estantes por nombre: {}", nombre);

        List<EstanteResponseDTO> estantes = estanteRepository.buscarPorNombreParecido(nombre)
                .stream()
                .map(this::mapearAResponse)
                .collect(Collectors.toList());

        log.info("Total de estantes encontrados por nombre: {}", estantes.size());

        return estantes;
    }

    private EstanteResponseDTO mapearAResponse(Estante estante) {
        return new EstanteResponseDTO(estante.getIdEstante(), estante.getNombreEstante());
    }
}