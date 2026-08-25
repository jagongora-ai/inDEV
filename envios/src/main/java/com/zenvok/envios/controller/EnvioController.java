package com.zenvok.envios.controller;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zenvok.envios.dto.EnvioRequest;
import com.zenvok.envios.dto.EnvioResponse;
import com.zenvok.envios.service.EnvioService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@RestController
@RequestMapping("/api/envios")
@RequiredArgsConstructor
public class EnvioController {

    private final EnvioService envioService;

    @Operation(summary = "Listar todos los envíos", description = "Obtiene una lista con todos los registros de envíos.")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente") })
    @GetMapping
    public ResponseEntity<List<EnvioResponse>> listarEnvios() {
        log.info("Petición recibida para listar envíos");
        return ResponseEntity.ok(envioService.listarEnvios());
    }

    @Operation(summary = "Obtener envío por ID", description = "Obtiene la información detallada de un envío por su ID.")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Envío encontrado"), @ApiResponse(responseCode = "404", description = "Envío no encontrado") })
    @GetMapping("/{id}")
    public ResponseEntity<EnvioResponse> obtenerPorId(@Parameter(description = "ID del envío", example = "1") @PathVariable Long id) {
        return ResponseEntity.ok(envioService.obtenerPorId(id));
    }

    @Operation(summary = "Buscar envíos por venta ID", description = "Obtiene los envíos asociados a una venta.")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Lista filtrada obtenida correctamente") })
    @GetMapping("/venta/{ventaId}")
    public ResponseEntity<List<EnvioResponse>> buscarPorVenta(@Parameter(description = "ID de la venta", example = "1") @PathVariable Long ventaId) {
        return ResponseEntity.ok(envioService.buscarPorVenta(ventaId));
    }

    @Operation(summary = "Buscar envíos por dirección ID", description = "Obtiene los envíos asociados a una dirección.")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Lista filtrada obtenida correctamente") })
    @GetMapping("/direccion/{direccionId}")
    public ResponseEntity<List<EnvioResponse>> buscarPorDireccion(@Parameter(description = "ID de la dirección", example = "1") @PathVariable Long direccionId) {
        return ResponseEntity.ok(envioService.buscarPorDireccion(direccionId));
    }

    @Operation(summary = "Buscar envíos por estado ID", description = "Obtiene los envíos asociados a un estado.")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Lista filtrada obtenida correctamente") })
    @GetMapping("/estado/{estadoId}")
    public ResponseEntity<List<EnvioResponse>> buscarPorEstado(@Parameter(description = "ID del estado", example = "1") @PathVariable Long estadoId) {
        return ResponseEntity.ok(envioService.buscarPorEstado(estadoId));
    }

    @Operation(summary = "Crear un nuevo envío", description = "Registra un nuevo envío en el sistema.")
    @ApiResponses(value = { @ApiResponse(responseCode = "201", description = "Envío creado correctamente"), @ApiResponse(responseCode = "400", description = "Datos inválidos") })
    @PostMapping
    public ResponseEntity<EnvioResponse> crearEnvio(@Valid @RequestBody EnvioRequest dto) {
        log.info("Petición recibida para crear envío");
        return ResponseEntity.status(HttpStatus.CREATED).body(envioService.crearEnvio(dto));
    }

    @Operation(summary = "Actualizar un envío por ID", description = "Modifica un envío existente utilizando su ID.")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Envío actualizado correctamente"), @ApiResponse(responseCode = "404", description = "Envío no encontrado") })
    @PutMapping("/{id}")
    public ResponseEntity<EnvioResponse> actualizarEnvio(
        @Parameter(description = "ID del envío a actualizar", example = "1") @PathVariable Long id,
        @Valid @RequestBody EnvioRequest dto
    ) {
        log.info("Petición recibida para actualizar envío con id: {}", id);
        return ResponseEntity.ok(envioService.actualizarEnvio(id, dto));
    }

    @Operation(summary = "Eliminar un envío", description = "Remueve un envío del sistema por su ID.")
    @ApiResponses(value = { @ApiResponse(responseCode = "204", description = "Envío eliminado correctamente") })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarEnvio(@Parameter(description = "ID del envío a eliminar", example = "1") @PathVariable Long id) {
        envioService.eliminarEnvio(id);
        return ResponseEntity.noContent().build();
    }
}