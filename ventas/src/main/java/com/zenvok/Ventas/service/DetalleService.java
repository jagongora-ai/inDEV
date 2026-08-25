package com.zenvok.Ventas.service;

import com.zenvok.Ventas.client.BodegaClient;
import com.zenvok.Ventas.client.ProductoClient;
import com.zenvok.Ventas.dto.BodegaProductoResponseDTO;
import com.zenvok.Ventas.dto.DetalleRequestDTO;
import com.zenvok.Ventas.dto.DetalleResponseDTO;
import com.zenvok.Ventas.dto.ProductoResponseDTO;
import com.zenvok.Ventas.model.Detalle;
import com.zenvok.Ventas.model.Ventas;
import com.zenvok.Ventas.repository.DetalleRepository;
import com.zenvok.Ventas.repository.VentasRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DetalleService {

    private final ProductoClient productoClient;
    private final BodegaClient bodegaClient;
    private final DetalleRepository detalleRepository;
    private final VentasRepository ventasRepository;

    @Transactional
    public DetalleResponseDTO guardar(DetalleRequestDTO dto) {

        Ventas ventaAsociada = ventasRepository.findById(dto.getVentaId())
            .orElseThrow(() -> new RuntimeException("No se encontró la venta con ID: " + dto.getVentaId()));
        ProductoResponseDTO producto = validarProductoExiste(dto.getProductoId());
        validarProductoEnBodega(dto.getProductoId());
        Detalle detalle = new Detalle();
        detalle.setCantidad(dto.getCantidad());
        detalle.setSubtotal(dto.getSubtotal());
        detalle.setVenta(ventaAsociada);
        detalle.setProductoId(dto.getProductoId());
        Detalle detalleGuardado = detalleRepository.save(detalle);
        DetalleResponseDTO response = mapearAResponse(detalleGuardado);
        response.setProductoNombre(producto.getNombre());
        return response;
    }

    @Transactional(readOnly = true)
    public List<DetalleResponseDTO> listar() {
        return detalleRepository.findAll()
                .stream()
                .map(this::mapearAResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<DetalleResponseDTO> findById(Long id) {
        return detalleRepository.findById(id)
                .map(this::mapearAResponse);
    }

    @Transactional(readOnly = true)
    public List<DetalleResponseDTO> obtenerDetallesPorVenta(Long idVenta) {
        return detalleRepository.findByVentaIdVenta(idVenta)
                .stream()
                .map(this::mapearAResponse)
                .collect(Collectors.toList());
    }

    private ProductoResponseDTO validarProductoExiste(Long productoId) {
        try {
            ProductoResponseDTO producto = productoClient.buscarPorId(productoId);
            if (producto == null || producto.getId() == null) {
                throw new RuntimeException("Producto no encontrado con ID: " + productoId);
            }
            return producto;
        } catch (Exception e) {
            throw new RuntimeException("Producto no encontrado con ID: " + productoId);
        }
    }

    private void validarProductoEnBodega(Long productoId) {
        try {
            BodegaProductoResponseDTO ubicacion = bodegaClient.buscarPorProducto(productoId);

            if (ubicacion == null || ubicacion.getProductoId() == null) {
                throw new RuntimeException("Producto no ubicado en bodega con ID: " + productoId);
            }
        } catch (Exception e) {
            throw new RuntimeException("Producto no ubicado en bodega con ID: " + productoId);
        }
    }

    private DetalleResponseDTO mapearAResponse(Detalle detalle) {
        DetalleResponseDTO response = new DetalleResponseDTO();

        response.setId(detalle.getId());
        response.setCantidad(detalle.getCantidad());
        response.setSubtotal(detalle.getSubtotal());
        response.setVentaId(detalle.getVenta().getIdVenta());
        response.setProductoId(detalle.getProductoId());

        try {
            ProductoResponseDTO producto = productoClient.buscarPorId(detalle.getProductoId());
            response.setProductoNombre(producto.getNombre());
        } catch (Exception e) {
            response.setProductoNombre("Producto no disponible");
        }

        return response;
    }
}