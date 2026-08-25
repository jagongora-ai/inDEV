package com.zenvok.configuracion.service;

import com.zenvok.configuracion.dto.ConfiguracionDTO;
import com.zenvok.configuracion.model.Configuracion;
import com.zenvok.configuracion.repository.ConfiguracionRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConfiguracionService {

    private final ConfiguracionRepository repository;

    @Transactional
    public ConfiguracionDTO guardarConfiguracion(ConfiguracionDTO dto) {
        log.info("Guardando configuración con clave: {}", dto.getClave());

        if (repository.findByClave(dto.getClave()).isPresent()) {
            log.warn("Intento de crear configuración duplicada con clave: {}", dto.getClave());
            throw new RuntimeException("La clave ya existe: " + dto.getClave());
        }

        Configuracion conf = new Configuracion(null, dto.getClave(), dto.getValor());
        Configuracion saved = repository.save(conf);

        log.info("Configuración creada correctamente con id: {}", saved.getId());

        return mapearADTO(saved);
    }

    @Transactional(readOnly = true)
    public List<ConfiguracionDTO> obtenerTodas() {
        log.info("Listando todas las configuraciones");

        List<ConfiguracionDTO> configuraciones = repository.findAll()
                .stream()
                .map(this::mapearADTO)
                .collect(Collectors.toList());

        log.info("Total de configuraciones encontradas: {}", configuraciones.size());

        return configuraciones;
    }

    @Transactional(readOnly = true)
    public Optional<ConfiguracionDTO> obtenerPorClave(String clave) {
        log.info("Buscando configuración por clave: {}", clave);

        Optional<ConfiguracionDTO> configuracion = repository.findByClave(clave)
                .map(this::mapearADTO);

        if (configuracion.isEmpty()) {
            log.warn("Configuración no encontrada con clave: {}", clave);
        }

        return configuracion;
    }

    @Transactional
    public ConfiguracionDTO modificarConfiguracion(String clave, ConfiguracionDTO dto) {
        log.info("Actualizando configuración con clave: {}", clave);

        Configuracion conf = repository.findByClave(clave)
                .orElseThrow(() -> {
                    log.warn("No se puede actualizar. Configuración no encontrada con clave: {}", clave);
                    return new RuntimeException("Clave no encontrada: " + clave);
                });

        conf.setValor(dto.getValor());

        Configuracion updated = repository.save(conf);

        log.info("Configuración actualizada correctamente con id: {}", updated.getId());

        return mapearADTO(updated);
    }

    private ConfiguracionDTO mapearADTO(Configuracion configuracion) {
        return new ConfiguracionDTO(
                configuracion.getId(),
                configuracion.getClave(),
                configuracion.getValor()
        );
    }
}
