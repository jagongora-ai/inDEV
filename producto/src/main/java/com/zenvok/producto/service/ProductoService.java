package com.zenvok.producto.service;

import com.zenvok.producto.client.EstadoClient;
import com.zenvok.producto.dto.EstadoResponseDTO;
import com.zenvok.producto.dto.ProductoRequestDTO;
import com.zenvok.producto.dto.ProductoResponseDTO;
import com.zenvok.producto.exception.CategoriaNotFoundException;
import com.zenvok.producto.exception.ProductoNotFoundException;
import com.zenvok.producto.model.Categoria;
import com.zenvok.producto.model.Producto;
import com.zenvok.producto.repository.CategoriaRepository;
import com.zenvok.producto.repository.ProductoRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductoService {

    private static final Logger logger = LoggerFactory.getLogger(ProductoService.class);

    @Autowired
    private EstadoClient estadoClient;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Transactional(readOnly = true)
    public List<ProductoResponseDTO> listar() {
        logger.info("Listando productos");

        return productoRepository.findAll()
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProductoResponseDTO buscarPorId(Long id) {
        logger.info("Buscando producto con id: {}", id);

        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Producto no encontrado con id: {}", id);
                    return new ProductoNotFoundException(id);
                });

        logger.info("Producto encontrado con id: {}", id);

        return convertirAResponse(producto);
    }

    @Transactional(readOnly = true)
    public List<ProductoResponseDTO> listarPorCategoria(Long categoriaId) {
        logger.info("Listando productos por categoría con id: {}", categoriaId);

        return productoRepository.findByCategoriaId(categoriaId)
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    @Transactional
    public ProductoResponseDTO guardar(ProductoRequestDTO request) {
        logger.info("Iniciando creación de producto con nombre: {}", request.getNombre());

        validarEstadoExiste(request.getEstadoId());

        Categoria categoria = categoriaRepository.findById(request.getCategoriaId())
                .orElseThrow(() -> {
                    logger.warn("Categoría no encontrada con id: {}", request.getCategoriaId());
                    return new CategoriaNotFoundException(request.getCategoriaId());
                });

        Producto producto = new Producto();
        producto.setNombre(request.getNombre().trim());
        producto.setDescripcion(request.getDescripcion());
        producto.setPrecio(request.getPrecio());
        producto.setEstadoId(request.getEstadoId());
        producto.setCategoria(categoria);

        Producto guardado = productoRepository.save(producto);

        logger.info("Producto creado correctamente con id: {}", guardado.getId());

        return convertirAResponse(guardado);
    }

    @Transactional
    public ProductoResponseDTO actualizar(Long id, ProductoRequestDTO request) {
        logger.info("Iniciando actualización de producto con id: {}", id);

        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("No se puede actualizar. Producto no encontrado con id: {}", id);
                    return new ProductoNotFoundException(id);
                });

        validarEstadoExiste(request.getEstadoId());

        Categoria categoria = categoriaRepository.findById(request.getCategoriaId())
                .orElseThrow(() -> {
                    logger.warn("Categoría no encontrada con id: {}", request.getCategoriaId());
                    return new CategoriaNotFoundException(request.getCategoriaId());
                });

        producto.setNombre(request.getNombre().trim());
        producto.setDescripcion(request.getDescripcion());
        producto.setPrecio(request.getPrecio());
        producto.setEstadoId(request.getEstadoId());
        producto.setCategoria(categoria);

        Producto actualizado = productoRepository.save(producto);

        logger.info("Producto actualizado correctamente con id: {}", actualizado.getId());

        return convertirAResponse(actualizado);
    }

    @Transactional
    public void eliminar(Long id) {
        logger.warn("Solicitud para eliminar producto con id: {}", id);

        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("No se puede eliminar. Producto no encontrado con id: {}", id);
                    return new ProductoNotFoundException(id);
                });

        productoRepository.delete(producto);

        logger.info("Producto eliminado correctamente con id: {}", id);
    }

    @Transactional(readOnly = true)
    public List<ProductoResponseDTO> buscarPorNombre(String nombre) {
        logger.info("Buscando productos por nombre: {}", nombre);

        return productoRepository.findByNombreContainingIgnoreCase(nombre)
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ProductoResponseDTO> listarPorEstadoId(Long estadoId) {
        logger.info("Listando productos por estado con id: {}", estadoId);

        return productoRepository.findByEstadoId(estadoId)
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    private void validarEstadoExiste(Long estadoId) {
        try {
            logger.info("Validando existencia del estado remoto con id: {}", estadoId);

            EstadoResponseDTO estado = estadoClient.buscarPorId(estadoId);

            if (estado == null || estado.getId() == null) {
                logger.warn("Estado remoto no encontrado con id: {}", estadoId);
                throw new IllegalArgumentException("Estado no encontrado con id: " + estadoId);
            }

            logger.info("Estado remoto validado correctamente. Estado: {}", estado.getNombre());

        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Error al comunicarse con el microservicio Estado para estadoId: {}", estadoId, e);
            throw new RuntimeException("Error al comunicarse con el microservicio Estado");
        }
    }

    private ProductoResponseDTO convertirAResponse(Producto producto) {
        ProductoResponseDTO dto = new ProductoResponseDTO();

        dto.setId(producto.getId());
        dto.setNombre(producto.getNombre());
        dto.setDescripcion(producto.getDescripcion());
        dto.setPrecio(producto.getPrecio());
        dto.setEstadoId(producto.getEstadoId());
        dto.setCategoriaId(producto.getCategoria().getId());
        dto.setCategoriaNombre(producto.getCategoria().getNombre());

        return dto;
    }
}