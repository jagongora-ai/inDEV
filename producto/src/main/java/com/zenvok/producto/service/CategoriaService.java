package com.zenvok.producto.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zenvok.producto.dto.CategoriaRequestDTO;
import com.zenvok.producto.dto.CategoriaResponseDTO;
import com.zenvok.producto.exception.CategoriaNotFoundException;
import com.zenvok.producto.model.Categoria;
import com.zenvok.producto.repository.CategoriaRepository;

@Service
public class CategoriaService {

    private static final Logger logger = LoggerFactory.getLogger(CategoriaService.class);

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Transactional(readOnly = true)
    public List<CategoriaResponseDTO> listar() {
        logger.info("Listando categorías");

        return categoriaRepository.findAll()
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public CategoriaResponseDTO buscarPorId(Long id) {
        logger.info("Buscando categoría con id: {}", id);

        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Categoría no encontrada con id: {}", id);
                    return new CategoriaNotFoundException(id);
                });

        return convertirAResponse(categoria);
    }

    @Transactional
    public CategoriaResponseDTO guardar(CategoriaRequestDTO request) {
        logger.info("Iniciando creación de categoría con nombre: {}", request.getNombre());

        Categoria categoria = new Categoria();
        categoria.setNombre(request.getNombre().trim());
        categoria.setDescripcion(request.getDescripcion());

        Categoria guardada = categoriaRepository.save(categoria);

        logger.info("Categoría creada correctamente con id: {}", guardada.getId());

        return convertirAResponse(guardada);
    }

    @Transactional
    public CategoriaResponseDTO actualizar(Long id, CategoriaRequestDTO request) {
        logger.info("Iniciando actualización de categoría con id: {}", id);

        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("No se puede actualizar. Categoría no encontrada con id: {}", id);
                    return new CategoriaNotFoundException(id);
                });

        categoria.setNombre(request.getNombre().trim());
        categoria.setDescripcion(request.getDescripcion());

        Categoria actualizada = categoriaRepository.save(categoria);

        logger.info("Categoría actualizada correctamente con id: {}", actualizada.getId());

        return convertirAResponse(actualizada);
    }

    @Transactional
    public void eliminar(Long id) {
        logger.warn("Solicitud para eliminar categoría con id: {}", id);

        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("No se puede eliminar. Categoría no encontrada con id: {}", id);
                    return new CategoriaNotFoundException(id);
                });

        categoriaRepository.delete(categoria);

        logger.info("Categoría eliminada correctamente con id: {}", id);
    }

    private CategoriaResponseDTO convertirAResponse(Categoria categoria) {
        CategoriaResponseDTO dto = new CategoriaResponseDTO();

        dto.setId(categoria.getId());
        dto.setNombre(categoria.getNombre());
        dto.setDescripcion(categoria.getDescripcion());

        return dto;
    }
}
