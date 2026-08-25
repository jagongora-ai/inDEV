package com.zenvok.envios.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zenvok.envios.client.DireccionClient;
import com.zenvok.envios.client.EstadoClient;
import com.zenvok.envios.client.VentaClient;
import com.zenvok.envios.dto.EnvioRequest;
import com.zenvok.envios.dto.EnvioResponse;
import com.zenvok.envios.model.Envio;
import com.zenvok.envios.repository.EnvioRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class EnvioService {

    private final EnvioRepository envioRepo;
    private final DireccionClient direccionClient;
    private final VentaClient ventaClient;
    private final EstadoClient estadoClient;

    private EnvioResponse mapToDTO(Envio envio) {
        return new EnvioResponse(
                envio.getId(),
                envio.getFechaEnvio(),
                envio.getFechaEmbargue(),
                envio.getComentarios(),
                envio.getDireccionId(),
                envio.getVentaId(),
                envio.getEstadoId()
        );
    }

    @Transactional(readOnly = true)
    public List<EnvioResponse> listarEnvios() {
        log.info("Listando todos los envíos");

        List<EnvioResponse> envios = envioRepo.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());

        log.info("Total de envíos encontrados: {}", envios.size());
        return envios;
    }

    @Transactional(readOnly = true)
    public EnvioResponse obtenerPorId(Long id) {
        log.info("Buscando envío con id: {}", id);

        return envioRepo.findById(id)
                .map(this::mapToDTO)
                .orElseThrow(() -> {
                    log.warn("Envío no encontrado con id: {}", id);
                    return new RuntimeException("Envío no encontrado con id: " + id);
                });
    }

    @Transactional(readOnly = true)
    public List<EnvioResponse> buscarPorVenta(Long ventaId) {
        log.info("Buscando envíos por venta id: {}", ventaId);

        return envioRepo.findByVentaId(ventaId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<EnvioResponse> buscarPorDireccion(Long direccionId) {
        log.info("Buscando envíos por dirección id: {}", direccionId);

        return envioRepo.findByDireccionId(direccionId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<EnvioResponse> buscarPorEstado(Long estadoId) {
        log.info("Buscando envíos por estado id: {}", estadoId);

        return envioRepo.findByEstadoId(estadoId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public EnvioResponse crearEnvio(EnvioRequest dto) {
        log.info("Creando envío para dirección id: {}", dto.getDireccionId());

        validarDireccionId(dto.getDireccionId());
        validarVentaId(dto.getVentaId());
        validarEstadoId(dto.getEstadoId());

        Envio envio = new Envio();
        envio.setFechaEnvio(dto.getFechaEnvio());
        envio.setFechaEmbargue(dto.getFechaEmbargue());
        envio.setComentarios(normalizarComentarios(dto.getComentarios()));
        envio.setDireccionId(dto.getDireccionId());
        envio.setVentaId(dto.getVentaId());
        envio.setEstadoId(dto.getEstadoId());

        EnvioResponse response = mapToDTO(envioRepo.save(envio));

        log.info("Envío creado correctamente con id: {}", response.getId());
        return response;
    }

    @Transactional
    public EnvioResponse actualizarEnvio(Long id, EnvioRequest dto) {
        log.info("Actualizando envío con id: {}", id);

        Envio envio = envioRepo.findById(id)
                .orElseThrow(() -> {
                    log.warn("No se puede actualizar. Envío no encontrado con id: {}", id);
                    return new RuntimeException("Envío no encontrado con id: " + id);
                });

        validarDireccionId(dto.getDireccionId());
        validarVentaId(dto.getVentaId());
        validarEstadoId(dto.getEstadoId());

        envio.setFechaEnvio(dto.getFechaEnvio());
        envio.setFechaEmbargue(dto.getFechaEmbargue());
        envio.setComentarios(normalizarComentarios(dto.getComentarios()));
        envio.setDireccionId(dto.getDireccionId());
        envio.setVentaId(dto.getVentaId());
        envio.setEstadoId(dto.getEstadoId());

        EnvioResponse response = mapToDTO(envioRepo.save(envio));

        log.info("Envío actualizado correctamente con id: {}", id);
        return response;
    }

    @Transactional
    public void eliminarEnvio(Long id) {
        log.info("Eliminando envío con id: {}", id);

        Envio envio = envioRepo.findById(id)
                .orElseThrow(() -> {
                    log.warn("No se puede eliminar. Envío no encontrado con id: {}", id);
                    return new RuntimeException("Envío no encontrado con id: " + id);
                });

        envioRepo.delete(envio);

        log.info("Envío eliminado correctamente con id: {}", id);
    }

    private void validarDireccionId(Long direccionId) {
        try {
            log.info("Validando dirección con id: {}", direccionId);
            direccionClient.buscarPorId(direccionId);
            log.info("Dirección validada correctamente con id: {}", direccionId);
        } catch (Exception e) {
            log.error("Error al validar dirección con id: {}", direccionId);
            throw new RuntimeException("La dirección con ID " + direccionId + " no existe o no se pudo conectar con direccion-service");
        }
    }

    private void validarVentaId(Long ventaId) {
        try {
            log.info("Validando venta con id: {}", ventaId);
            ventaClient.buscarPorId(ventaId);
            log.info("Venta validada correctamente con id: {}", ventaId);
        } catch (Exception e) {
            log.error("Error al validar venta con id: {}", ventaId);
            throw new RuntimeException("La venta con ID " + ventaId + " no existe o no se pudo conectar con venta-service");
        }
    }

    private void validarEstadoId(Long estadoId) {
        try {
            log.info("Validando estado con id: {}", estadoId);
            estadoClient.buscarPorId(estadoId);
            log.info("Estado validado correctamente con id: {}", estadoId);
        } catch (Exception e) {
            log.error("Error al validar estado con id: {}", estadoId);
            throw new RuntimeException("El estado con ID " + estadoId + " no existe o no se pudo conectar con estado-service");
        }
    }

    private String normalizarComentarios(String comentarios) {
        if (comentarios == null || comentarios.trim().isEmpty()) {
            return null;
        }

        return comentarios.trim();
    }
}