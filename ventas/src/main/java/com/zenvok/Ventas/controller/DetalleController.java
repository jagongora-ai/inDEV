package com.zenvok.Ventas.controller;

import com.zenvok.Ventas.dto.DetalleRequestDTO;
import com.zenvok.Ventas.dto.DetalleResponseDTO;
import com.zenvok.Ventas.service.DetalleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("/api/detalles")
@RequiredArgsConstructor
public class DetalleController {

    private final DetalleService detalleService;

    @Operation(
        summary = "Crear un detalle de venta",
        description = "Registra un nuevo detalle o linea de item para una venta en el sistema."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Detalle de venta creado correctamente"),
        @ApiResponse(responseCode = "400", description = "Los datos enviados son invalidos")
    })
    @PostMapping
    public ResponseEntity<DetalleResponseDTO> crearDetalle(@Valid @RequestBody DetalleRequestDTO request) {
        log.info("Petición recibida para crear detalle de venta");
        DetalleResponseDTO nuevoDetalle = detalleService.guardar(request);
        return new ResponseEntity<>(nuevoDetalle, HttpStatus.CREATED);
    }

    @Operation(
        summary = "Listar todos los detalles",
        description = "Obtiene la lista completa de todos los detalles de venta registrados."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente")
    })
    @GetMapping
    public ResponseEntity<List<DetalleResponseDTO>> listarTodos() {
        log.info("Petición recibida para listar detalles de venta");
        List<DetalleResponseDTO> detalles = detalleService.listar();
        return ResponseEntity.ok(detalles);
    }

    @Operation(
        summary = "Buscar detalle por ID",
        description = "Obtiene la informacion de un detalle de venta mediante su identificador ID."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Detalle encontrado correctamente"),
        @ApiResponse(responseCode = "404", description = "Detalle no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<DetalleResponseDTO> buscarPorId(
            @Parameter(description = "ID del detalle", example = "1")
            @PathVariable Long id) {
        return detalleService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
        summary = "Listar detalles por venta",
        description = "Obtiene todos los detalles asociados a una venta especifica mediante su ID."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista filtrada obtenida correctamente")
    })
    @GetMapping("/venta/{idVenta}")
    public ResponseEntity<List<DetalleResponseDTO>> listarPorVenta(
            @Parameter(description = "ID de la venta", example = "1")
            @PathVariable Long idVenta) {
        List<DetalleResponseDTO> detallesFiltrados = detalleService.obtenerDetallesPorVenta(idVenta);
        return ResponseEntity.ok(detallesFiltrados);
    }
}