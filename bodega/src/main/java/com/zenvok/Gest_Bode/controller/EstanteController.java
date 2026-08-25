package com.zenvok.Gest_Bode.controller;

import com.zenvok.Gest_Bode.dto.EstanteRequestDTO;
import com.zenvok.Gest_Bode.dto.EstanteResponseDTO;
import com.zenvok.Gest_Bode.service.EstanteService;
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
@RequestMapping("/api/estantes")
@RequiredArgsConstructor
public class EstanteController {

    private final EstanteService estanteService;

    @Operation(
        summary = "Crear un estante",
        description = "Registra un nuevo estante en el modulo de gestion de bodega."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Estante creado correctamente"),
        @ApiResponse(responseCode = "400", description = "Los datos enviados son invalidos")
    })
    @PostMapping
    public ResponseEntity<EstanteResponseDTO> crear(@Valid @RequestBody EstanteRequestDTO request) {
        log.info("Petición recibida para crear estante");
        return new ResponseEntity<>(estanteService.guardar(request), HttpStatus.CREATED);
    }

    @Operation(
        summary = "Listar todos los estantes",
        description = "Obtiene la lista completa de todos los estantes registrados en la bodega."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente")
    })
    @GetMapping
    public ResponseEntity<List<EstanteResponseDTO>> listarTodos() {
        log.info("Petición recibida para listar estantes");
        return ResponseEntity.ok(estanteService.obtenerTodos());
    }

    @Operation(
        summary = "Buscar estante por ID",
        description = "Obtiene la informacion de un estante especifico mediante su identificador ID."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Estante encontrado correctamente"),
        @ApiResponse(responseCode = "404", description = "Estante no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EstanteResponseDTO> buscarPorId(
            @Parameter(description = "ID del estante", example = "1")
            @PathVariable Long id) {
        return estanteService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
        summary = "Buscar estantes por nombre",
        description = "Obtiene una lista de estantes cuyo nombre o codigo coincida con el parametro de busqueda."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Busqueda realizada correctamente")
    })
    @GetMapping("/buscar")
    public ResponseEntity<List<EstanteResponseDTO>> buscarPorNombre(
            @Parameter(description = "Nombre o codigo del estante", example = "Estante A1")
            @RequestParam String nombre) {
        return ResponseEntity.ok(estanteService.buscarPorNombre(nombre));
    }
}