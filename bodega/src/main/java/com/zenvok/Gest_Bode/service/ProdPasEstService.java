package com.zenvok.Gest_Bode.service;

import com.zenvok.Gest_Bode.client.ProductoClient;
import com.zenvok.Gest_Bode.dto.ProdPasEstRequestDTO;
import com.zenvok.Gest_Bode.dto.ProdPasEstResponseDTO;
import com.zenvok.Gest_Bode.dto.ProductoResponseDTO;
import com.zenvok.Gest_Bode.model.Pas_Est;
import com.zenvok.Gest_Bode.model.ProdPasEst;
import com.zenvok.Gest_Bode.repository.PasEstRepository;
import com.zenvok.Gest_Bode.repository.ProdPasEstRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProdPasEstService {

    private final ProductoClient productoClient;
    private final ProdPasEstRepository prodPasEstRepository;
    private final PasEstRepository pasEstRepository;

    public ProdPasEstResponseDTO guardar(ProdPasEstRequestDTO dto) {
        log.info("Ubicando producto id: {} en pasillo-estante id: {}", dto.getProductoId(), dto.getPasEstId());

        Pas_Est pasEst = pasEstRepository.findById(dto.getPasEstId())
                .orElseThrow(() -> {
                    log.warn("Relación pasillo-estante no encontrada con id: {}", dto.getPasEstId());
                    return new RuntimeException("No existe la combinación Pasillo-Estante con ID: " + dto.getPasEstId());
                });

        validarProductoExiste(dto.getProductoId());

        ProdPasEst prodPasEst = new ProdPasEst();
        prodPasEst.setProductoId(dto.getProductoId());
        prodPasEst.setPasEst(pasEst);

        ProdPasEst guardado = prodPasEstRepository.save(prodPasEst);

        log.info("Producto ubicado correctamente con registro id: {}", guardado.getIdProdEst());

        return mapearAResponse(guardado);
    }

    public List<ProdPasEstResponseDTO> obtenerTodos() {
        log.info("Listando todas las ubicaciones de productos");

        List<ProdPasEstResponseDTO> ubicaciones = prodPasEstRepository.findAll()
                .stream()
                .map(this::mapearAResponse)
                .collect(Collectors.toList());

        log.info("Total de ubicaciones encontradas: {}", ubicaciones.size());

        return ubicaciones;
    }

    public Optional<ProdPasEstResponseDTO> buscarPorProductoId(Long productoId) {
        log.info("Buscando ubicación del producto id: {}", productoId);

        Optional<ProdPasEstResponseDTO> ubicacion = prodPasEstRepository.buscarUbicacionDeProducto(productoId)
                .map(this::mapearAResponse);

        if (ubicacion.isEmpty()) {
            log.warn("Ubicación no encontrada para producto id: {}", productoId);
        }

        return ubicacion;
    }

    private void validarProductoExiste(Long productoId) {
        try {
            log.info("Validando producto con id: {}", productoId);

            ProductoResponseDTO producto = productoClient.buscarPorId(productoId);

            if (producto == null || producto.getId() == null) {
                log.warn("Producto no encontrado con id: {}", productoId);
                throw new RuntimeException("Producto no encontrado con ID: " + productoId);
            }

            log.info("Producto validado correctamente con id: {}", productoId);
        } catch (Exception e) {
            log.error("Error al validar producto con id: {}", productoId);
            throw new RuntimeException("Producto no encontrado con ID: " + productoId);
        }
    }

    private ProdPasEstResponseDTO mapearAResponse(ProdPasEst entity) {
        ProdPasEstResponseDTO res = new ProdPasEstResponseDTO();
        res.setIdProdEst(entity.getIdProdEst());
        res.setProductoId(entity.getProductoId());
        res.setPasEstId(entity.getPasEst().getIdPasEst());
        res.setNombrePasillo(entity.getPasEst().getPasillo().getNombrePasillo());
        res.setNombreEstante(entity.getPasEst().getEstante().getNombreEstante());
        return res;
    }
}