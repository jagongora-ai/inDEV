package com.zenvok.Gest_Bode.service;

import com.zenvok.Gest_Bode.dto.PasilloRequestDTO;
import com.zenvok.Gest_Bode.dto.PasilloResponseDTO;
import com.zenvok.Gest_Bode.model.Pasillo;
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
public class PasilloService {

    private final PasilloRepository pasilloRepository;

    public PasilloResponseDTO guardar(PasilloRequestDTO dto) {
        log.info("Creando pasillo con nombre: {}", dto.getNombrePasillo());

        Pasillo pasillo = new Pasillo();
        pasillo.setNombrePasillo(dto.getNombrePasillo());

        Pasillo guardado = pasilloRepository.save(pasillo);

        log.info("Pasillo creado correctamente con id: {}", guardado.getIdPasillo());

        return mapearAResponse(guardado);
    }

    public List<PasilloResponseDTO> obtenerTodos() {
        log.info("Listando todos los pasillos");

        List<PasilloResponseDTO> pasillos = pasilloRepository.findAll()
                .stream()
                .map(this::mapearAResponse)
                .collect(Collectors.toList());

        log.info("Total de pasillos encontrados: {}", pasillos.size());

        return pasillos;
    }

    public Optional<PasilloResponseDTO> obtenerPorId(Long id) {
        log.info("Buscando pasillo con id: {}", id);

        Optional<PasilloResponseDTO> pasillo = pasilloRepository.findById(id)
                .map(this::mapearAResponse);

        if (pasillo.isEmpty()) {
            log.warn("Pasillo no encontrado con id: {}", id);
        }

        return pasillo;
    }

    public List<PasilloResponseDTO> buscarPorNombre(String nombre) {
        log.info("Buscando pasillos por nombre: {}", nombre);

        List<PasilloResponseDTO> pasillos = pasilloRepository.buscarPorNombreParecido(nombre)
                .stream()
                .map(this::mapearAResponse)
                .collect(Collectors.toList());

        log.info("Total de pasillos encontrados por nombre: {}", pasillos.size());

        return pasillos;
    }

    private PasilloResponseDTO mapearAResponse(Pasillo pasillo) {
        return new PasilloResponseDTO(pasillo.getIdPasillo(), pasillo.getNombrePasillo());
    }
}