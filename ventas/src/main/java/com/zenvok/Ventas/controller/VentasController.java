package com.zenvok.Ventas.controller;

import com.zenvok.Ventas.dto.VentaRequestDTO;
import com.zenvok.Ventas.dto.VentaResponseDTO;
import com.zenvok.Ventas.service.VentasService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ventas")
@RequiredArgsConstructor
public class VentasController {

    private final VentasService ventasService;

    @Operation(
        summary = "Crear una venta",
        description = "Registra una nueva venta en el sistema."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Venta creada correctamente"),
        @ApiResponse(responseCode = "400", description = "Los datos enviados son invalidos")
    })
    @PostMapping
    public ResponseEntity<VentaResponseDTO> crearVenta(@Valid @RequestBody VentaRequestDTO request) {
        VentaResponseDTO nuevaVenta = ventasService.guardar(request);
        return new ResponseEntity<>(nuevaVenta, HttpStatus.CREATED);
    }

    @Operation(
        summary = "Listar todas las ventas",
        description = "Obtiene la lista completa de ventas registradas."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente")
    })
    @GetMapping
    public ResponseEntity<List<VentaResponseDTO>> listarTodas() {
        List<VentaResponseDTO> ventas = ventasService.obtenerTodas();
        return ResponseEntity.ok(ventas);
    }

    @Operation(
        summary = "Buscar venta por ID",
        description = "Obtiene la informacion de una venta mediante su identificador ID."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Venta encontrada correctamente"),
        @ApiResponse(responseCode = "404", description = "Venta no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<VentaResponseDTO> buscarPorId(
            @Parameter(description = "ID de la venta", example = "1")
            @PathVariable Long id) {
        return ventasService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}