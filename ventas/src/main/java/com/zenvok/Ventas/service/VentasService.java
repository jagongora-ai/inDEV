package com.zenvok.Ventas.service;

import com.zenvok.Ventas.client.EstadoClient;
import com.zenvok.Ventas.client.UsuarioClient;
import com.zenvok.Ventas.dto.EstadoResponseDTO;
import com.zenvok.Ventas.dto.UsuarioResponseDTO;
import com.zenvok.Ventas.dto.VentaRequestDTO;
import com.zenvok.Ventas.dto.VentaResponseDTO;
import com.zenvok.Ventas.model.Ventas;
import com.zenvok.Ventas.repository.VentasRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VentasService {

    private final VentasRepository ventasRepository;
    private final UsuarioClient usuarioClient;
    private final EstadoClient estadoClient;

    @Transactional
    public VentaResponseDTO guardar(VentaRequestDTO dto) {

        UsuarioResponseDTO usuario = validarUsuarioExiste(dto.getUsuarioId());
        EstadoResponseDTO estado = validarEstadoExiste(dto.getEstadoId());

        Ventas venta = new Ventas();
        venta.setFechaVentas(dto.getFechaVentas());
        venta.setTotal(dto.getTotal());
        venta.setUsuarioId(dto.getUsuarioId());
        venta.setEstadoId(dto.getEstadoId());

        Ventas ventaGuardada = ventasRepository.save(venta);

        return mapearAResponse(ventaGuardada, usuario, estado);
    }

    @Transactional(readOnly = true)
    public List<VentaResponseDTO> obtenerTodas() {
        return ventasRepository.findAll()
                .stream()
                .map(this::mapearAResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<VentaResponseDTO> obtenerPorId(Long id) {
        return ventasRepository.findById(id)
                .map(this::mapearAResponse);
    }

    private UsuarioResponseDTO validarUsuarioExiste(Long usuarioId) {
        try {
            UsuarioResponseDTO usuario = usuarioClient.buscarPorId(usuarioId);

            if (usuario == null || usuario.getId() == null) {
                throw new RuntimeException("Usuario no encontrado con ID: " + usuarioId);
            }

            return usuario;

        } catch (Exception e) {
            throw new RuntimeException("Usuario no encontrado con ID: " + usuarioId);
        }
    }

    private EstadoResponseDTO validarEstadoExiste(Long estadoId) {
        try {
            EstadoResponseDTO estado = estadoClient.buscarPorId(estadoId);

            if (estado == null || estado.getId() == null) {
                throw new RuntimeException("Estado no encontrado con ID: " + estadoId);
            }

            return estado;

        } catch (Exception e) {
            throw new RuntimeException("Estado no encontrado con ID: " + estadoId);
        }
    }

    private VentaResponseDTO mapearAResponse(Ventas venta) {

        VentaResponseDTO response = new VentaResponseDTO();

        response.setId(venta.getIdVenta());
        response.setFechaVentas(venta.getFechaVentas());
        response.setTotal(venta.getTotal());
        response.setUsuarioId(venta.getUsuarioId());
        response.setEstadoId(venta.getEstadoId());

        try {
            UsuarioResponseDTO usuario = usuarioClient.buscarPorId(venta.getUsuarioId());
            response.setUsuarioNombre(usuario.getNombre() + " " + usuario.getApellido());
        } catch (Exception e) {
            response.setUsuarioNombre("Usuario no disponible");
        }

        try {
            EstadoResponseDTO estado = estadoClient.buscarPorId(venta.getEstadoId());
            response.setEstadoNombre(estado.getNombre());
        } catch (Exception e) {
            response.setEstadoNombre("Estado no disponible");
        }
        return response;
    }

    private VentaResponseDTO mapearAResponse(
            Ventas venta,
            UsuarioResponseDTO usuario,
            EstadoResponseDTO estado
    ) {
        VentaResponseDTO response = new VentaResponseDTO();

        response.setId(venta.getIdVenta());
        response.setFechaVentas(venta.getFechaVentas());
        response.setTotal(venta.getTotal());

        response.setUsuarioId(venta.getUsuarioId());
        response.setUsuarioNombre(usuario.getNombre() + " " + usuario.getApellido());

        response.setEstadoId(venta.getEstadoId());
        response.setEstadoNombre(estado.getNombre());

        return response;
    }
}