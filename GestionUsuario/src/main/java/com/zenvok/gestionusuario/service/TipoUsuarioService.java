package com.zenvok.gestionusuario.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zenvok.gestionusuario.dto.TipoUsuarioDTO;
import com.zenvok.gestionusuario.model.TipoUsuario;
import com.zenvok.gestionusuario.repository.TipoUsuarioRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class TipoUsuarioService {

    private final TipoUsuarioRepository tipoUsuarioRepository;

    private TipoUsuarioDTO mapToDTO(TipoUsuario tipo) {
        TipoUsuarioDTO dto = new TipoUsuarioDTO();
        dto.setId(tipo.getId());
        dto.setNombreTipo(tipo.getNombreTipo());
        return dto;
    }

    @Transactional
    public TipoUsuarioDTO crearTipo(TipoUsuarioDTO dto) {
        log.info("Creando nuevo tipo de usuario: {}", dto.getNombreTipo());
        
        TipoUsuario tipo = new TipoUsuario();
        tipo.setNombreTipo(dto.getNombreTipo());
        
        TipoUsuario nuevoTipo = tipoUsuarioRepository.save(tipo);
        log.info("Tipo de usuario creado con éxito. ID asignado: {}", nuevoTipo.getId());
        return mapToDTO(nuevoTipo);
    }

    @Transactional(readOnly = true)
    public List<TipoUsuarioDTO> obtenerTodosLosTipos() {
        log.info("Obteniendo la lista de todos los tipos de usuario");
        return tipoUsuarioRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public java.util.Optional<TipoUsuarioDTO> obtenerTipoPorId(Long id) {
        log.info("Buscando tipo de usuario por ID: {}", id);
        
        java.util.Optional<TipoUsuarioDTO> resultado = tipoUsuarioRepository.findById(id).map(this::mapToDTO);
        
        if (resultado.isEmpty()) {
            log.warn("No se encontró el tipo de usuario con ID: {}", id);
        }
        
        return resultado;
    }
}

