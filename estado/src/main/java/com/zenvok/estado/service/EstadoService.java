package com.zenvok.estado.service;

import com.zenvok.estado.dto.EstadoRequestDTO;
import com.zenvok.estado.dto.EstadoResponseDTO;
import com.zenvok.estado.exception.EstadoNotFoundException;
import com.zenvok.estado.model.Estado;
import com.zenvok.estado.repository.EstadoRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EstadoService {

    private static final Logger logger = LoggerFactory.getLogger(EstadoService.class);

    @Autowired
    private EstadoRepository estadoRepository;

    @Transactional(readOnly = true)
    public List<EstadoResponseDTO> listar() {
        logger.info("Listando estados");

        return estadoRepository.findAll()
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public EstadoResponseDTO buscarPorId(Long id) {
        logger.info("Buscando estado con id: {}", id);

        Estado estado = estadoRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Estado no encontrado con id: {}", id);
                    return new EstadoNotFoundException(id);
                });

        logger.info("Estado encontrado con id: {}", id);

        return convertirAResponse(estado);
    }

    @Transactional(readOnly = true)
    public List<EstadoResponseDTO> buscarPorNombre(String nombre) {
        logger.info("Buscando estados por nombre: {}", nombre);

        return estadoRepository.findByNombreContainingIgnoreCase(nombre)
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    @Transactional
    public EstadoResponseDTO guardar(EstadoRequestDTO request) {
        logger.info("Iniciando creación de estado con nombre: {}", request.getNombre());

        Estado estado = new Estado();
        estado.setNombre(request.getNombre().trim());
        estado.setDescripcion(request.getDescripcion());

        Estado guardado = estadoRepository.save(estado);

        logger.info("Estado creado correctamente con id: {}", guardado.getId());

        return convertirAResponse(guardado);
    }

    @Transactional
    public EstadoResponseDTO actualizar(Long id, EstadoRequestDTO request) {
        logger.info("Iniciando actualización de estado con id: {}", id);

        Estado estado = estadoRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("No se puede actualizar. Estado no encontrado con id: {}", id);
                    return new EstadoNotFoundException(id);
                });

        estado.setNombre(request.getNombre().trim());
        estado.setDescripcion(request.getDescripcion());

        Estado actualizado = estadoRepository.save(estado);

        logger.info("Estado actualizado correctamente con id: {}", actualizado.getId());

        return convertirAResponse(actualizado);
    }

    @Transactional
    public void eliminar(Long id) {
        logger.warn("Solicitud para eliminar estado con id: {}", id);

        Estado estado = estadoRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("No se puede eliminar. Estado no encontrado con id: {}", id);
                    return new EstadoNotFoundException(id);
                });

        estadoRepository.delete(estado);

        logger.info("Estado eliminado correctamente con id: {}", id);
    }

    private EstadoResponseDTO convertirAResponse(Estado estado) {
        EstadoResponseDTO dto = new EstadoResponseDTO();

        dto.setId(estado.getId());
        dto.setNombre(estado.getNombre());
        dto.setDescripcion(estado.getDescripcion());

        return dto;
    }
}