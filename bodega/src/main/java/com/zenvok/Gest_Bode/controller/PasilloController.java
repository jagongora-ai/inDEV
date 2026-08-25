package com.zenvok.Gest_Bode.controller;

import com.zenvok.Gest_Bode.dto.PasilloRequestDTO;
import com.zenvok.Gest_Bode.dto.PasilloResponseDTO;
import com.zenvok.Gest_Bode.service.PasilloService;
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
@RequestMapping("/api/pasillos")
@RequiredArgsConstructor
public class PasilloController {

    private final PasilloService pasilloService;

    @Operation(
        summary = "Crear un pasillo",
        description = "Registra un nuevo pasillo en el modulo de gestion de bodega."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Pasillo creado correctamente"),
        @ApiResponse(responseCode = "400", description = "Los datos enviados son invalidos")
    })
    @PostMapping
    public ResponseEntity<PasilloResponseDTO> crear(@Valid @RequestBody PasilloRequestDTO request) {
        log.info("Petición recibida para crear pasillo");
        return new ResponseEntity<>(pasilloService.guardar(request), HttpStatus.CREATED);
    }

    @Operation(
        summary = "Listar todos los pasillos",
        description = "Obtiene la lista completa de todos los pasillos registrados en la bodega."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de pasillos obtenida correctamente")
    })
    @GetMapping
    public ResponseEntity<List<PasilloResponseDTO>> listarTodos() {
        log.info("Petición recibida para listar pasillos");
        return ResponseEntity.ok(pasilloService.obtenerTodos());
    }

    @Operation(
        summary = "Buscar pasillo por ID",
        description = "Obtiene la informacion de un pasillo especifico mediante su identificador ID."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pasillo encontrado correctamente"),
        @ApiResponse(responseCode = "404", description = "Pasillo no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<PasilloResponseDTO> buscarPorId(
            @Parameter(description = "ID del pasillo", example = "1")
            @PathVariable Long id) {
        return pasilloService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
        summary = "Buscar pasillos por nombre",
        description = "Obtiene una lista de pasillos cuyo nombre o codigo coincida con el parametro de busqueda."
    )
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Busqueda realizada correctamente")})
    @GetMapping("/buscar")
    public ResponseEntity<List<PasilloResponseDTO>> buscarPorNombre(
            @Parameter(description = "Nombre o codigo del pasillo", example = "A1")
            @RequestParam String nombre) {
        return ResponseEntity.ok(pasilloService.buscarPorNombre(nombre));
    }
}