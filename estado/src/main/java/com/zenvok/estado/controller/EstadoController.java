package com.zenvok.estado.controller;

import com.zenvok.estado.dto.EstadoRequestDTO;
import com.zenvok.estado.dto.EstadoResponseDTO;
import com.zenvok.estado.service.EstadoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/estados")
public class EstadoController {

    @Autowired
    private EstadoService estadoService;

    @Operation(
        summary = "Listar todos los estados",
        description = "Obtiene una lista con todos los estados registrados en el sistema."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de estados obtenida correctamente")
    })
    @GetMapping
    public ResponseEntity<List<EstadoResponseDTO>> listarEstados() {
        return ResponseEntity.ok(estadoService.listar());
    }

    @Operation(
        summary = "Buscar estado por ID",
        description = "Obtiene la informacion de un estado especifico mediante su identificador único."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Estado encontrado correctamente"),
        @ApiResponse(responseCode = "404", description = "Estado no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EstadoResponseDTO> buscarEstadoPorId(
            @Parameter(description = "ID del estado", example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(estadoService.buscarPorId(id));
    }

    @Operation(
        summary = "Buscar estados por nombre",
        description = "Obtiene una lista de estados cuyo nombre coincida con el parametro de busqueda."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Busqueda realizada correctamente")
    })
    @GetMapping("/buscar")
    public ResponseEntity<List<EstadoResponseDTO>> buscarPorNombre(
            @Parameter(description = "Nombre del estado a buscar", example = "Activo")
            @RequestParam String nombre){
        return ResponseEntity.ok(estadoService.buscarPorNombre(nombre));
    }

    @Operation(
        summary = "Crear un nuevo estado",
        description = "Registra un nuevo estado con su respectivo nombre y descripcion."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Estado creado correctamente"),
        @ApiResponse(responseCode = "400", description = "Los datos enviados son invalidos")
    })
    @PostMapping
    public ResponseEntity<EstadoResponseDTO> crearEstado(
            @Valid @RequestBody EstadoRequestDTO request
    ) {
        EstadoResponseDTO nuevo = estadoService.guardar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }

    @Operation(
        summary = "Actualizar un estado por ID",
        description = "Modifica los datos de un estado existente utilizando su identificador."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Estado actualizado correctamente"),
        @ApiResponse(responseCode = "400", description = "Los datos enviados son invalidos"),
        @ApiResponse(responseCode = "404", description = "Estado no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<EstadoResponseDTO> actualizarEstado(
            @Parameter(description = "ID del estado a actualizar", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody EstadoRequestDTO request
    ) {
        return ResponseEntity.ok(estadoService.actualizar(id, request));
    }

    @Operation(
        summary = "Eliminar un estado",
        description = "Remueve un estado del sistema de forma permanente mediante su ID."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "24", description = "Estado eliminado correctamente (No Content)"),
        @ApiResponse(responseCode = "404", description = "Estado no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarEstado(
            @Parameter(description = "ID del estado a eliminar", example = "1")
            @PathVariable Long id) {
        estadoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}