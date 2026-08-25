package com.zenvok.Gest_Bode.controller;

import com.zenvok.Gest_Bode.dto.PasEstRequestDTO;
import com.zenvok.Gest_Bode.dto.PasEstResponseDTO;
import com.zenvok.Gest_Bode.service.PasEstService;
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
@RequestMapping("/api/bodega_estructuras")
@RequiredArgsConstructor
public class PasEstController {

    private final PasEstService pasEstService;

    @Operation(
        summary = "Asignar estante a pasillo",
        description = "Registra una nueva relacion o estructura vinculando un estante con un pasillo."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Asignacion creada correctamente"),
        @ApiResponse(responseCode = "400", description = "Los datos enviados son invalidos")
    })
    @PostMapping
    public ResponseEntity<PasEstResponseDTO> asignarEstanteAPasillo(@Valid @RequestBody PasEstRequestDTO request) {
        log.info("Petición recibida para asignar estante a pasillo");
        return new ResponseEntity<>(pasEstService.guardar(request), HttpStatus.CREATED);
    }

    @Operation(
        summary = "Listar todas las estructuras",
        description = "Obtiene la lista completa de todas las asociaciones de pasillos y estantes en bodega."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente")
    })
    @GetMapping
    public ResponseEntity<List<PasEstResponseDTO>> listarTodas() {
        log.info("Petición recibida para listar estructuras de bodega");
        return ResponseEntity.ok(pasEstService.obtenerTodos());
    }

    @Operation(
        summary = "Listar estructuras por pasillo",
        description = "Obtiene todos los estantes asociados a un pasillo especifico mediante su ID."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista filtrada obtenida correctamente")
    })
    @GetMapping("/pasillo/{idPasillo}")
    public ResponseEntity<List<PasEstResponseDTO>> listarPorPasillo(
            @Parameter(description = "ID del pasillo", example = "1")
            @PathVariable Long idPasillo) {
        return ResponseEntity.ok(pasEstService.listarEstantesPorPasillo(idPasillo));
    }
}