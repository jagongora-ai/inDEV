package com.zenvok.Gest_Bode.controller;

import com.zenvok.Gest_Bode.dto.ProdPasEstRequestDTO;
import com.zenvok.Gest_Bode.dto.ProdPasEstResponseDTO;
import com.zenvok.Gest_Bode.service.ProdPasEstService;
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
@RequestMapping("/api/productos_ubicaciones")
@RequiredArgsConstructor
public class ProdPasEstController {

    private final ProdPasEstService prodPasEstService;

    @Operation(
        summary = "Ubicar un producto en bodega",
        description = "Registra una nueva ubicacion vinculando un producto con una estructura de pasillo y estante."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Ubicacion del producto registrada correctamente"),
        @ApiResponse(responseCode = "400", description = "Los datos enviados son invalidos")
    })
    @PostMapping
    public ResponseEntity<ProdPasEstResponseDTO> ubicarProducto(@Valid @RequestBody ProdPasEstRequestDTO request) {
        log.info("Petición recibida para ubicar producto");
        return new ResponseEntity<>(prodPasEstService.guardar(request), HttpStatus.CREATED);
    }

    @Operation(
        summary = "Listar todas las ubicaciones",
        description = "Obtiene la lista completa de todas las ubicaciones de productos asignadas en la bodega."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de ubicaciones obtenida correctamente")
    })
    @GetMapping
    public ResponseEntity<List<ProdPasEstResponseDTO>> listarTodas() {
        log.info("Petición recibida para listar ubicaciones de productos");
        return ResponseEntity.ok(prodPasEstService.obtenerTodos());
    }

    @Operation(
        summary = "Buscar ubicacion por producto ID",
        description = "Obtiene la informacion de ubicacion en bodega asignada a un producto especifico mediante su ID."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Ubicacion del producto encontrada correctamente"),
        @ApiResponse(responseCode = "404", description = "Ubicacion no encontrada para el ID de producto provisto")
    })
    @GetMapping("/producto/{productoId}")
    public ResponseEntity<ProdPasEstResponseDTO> buscarPorProducto(
            @Parameter(description = "ID del producto a consultar", example = "1")
            @PathVariable Long productoId) {
        return prodPasEstService.buscarPorProductoId(productoId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}