package com.zenvok.producto.controller;

import com.zenvok.producto.dto.CategoriaRequestDTO;
import com.zenvok.producto.dto.CategoriaResponseDTO;
import com.zenvok.producto.service.CategoriaService;

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
@RequestMapping("/api/categorias")
public class CategoriaController {

@Autowired
    private CategoriaService categoriaService;

    @Operation(
        summary = "Listar todas las categorias",
        description = "Obtiene la lista completa de categorias registradas."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente")
    })
    @GetMapping
    public ResponseEntity<List<CategoriaResponseDTO>> listarCategorias() {
        return ResponseEntity.ok(categoriaService.listar());
    }

    @Operation(
        summary = "Buscar categoria por ID",
        description = "Obtiene la informacion de una categoria mediante su identificador ID."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Categoria encontrada correctamente"),
        @ApiResponse(responseCode = "404", description = "Categoria no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CategoriaResponseDTO> buscarCategoriaPorId(
            @Parameter(description = "ID de la categoria", example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(categoriaService.buscarPorId(id));
    }

    @Operation(
        summary = "Crear una categoria",
        description = "Registra una nueva categoria en el sistema."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Categoria creada correctamente"),
        @ApiResponse(responseCode = "400", description = "Los datos enviados son invalidos")
    })
    @PostMapping
    public ResponseEntity<CategoriaResponseDTO> crearCategoria(
            @Valid @RequestBody CategoriaRequestDTO request
    ) {
        CategoriaResponseDTO nueva = categoriaService.guardar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(nueva);
    }

    @Operation(
        summary = "Actualizar una categoria",
        description = "Modifica la informacion de una categoria existente mediante su ID."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Categoria actualizada correctamente"),
        @ApiResponse(responseCode = "400", description = "Los datos enviados son invalidos"),
        @ApiResponse(responseCode = "404", description = "Categoria no encontrada")
    })
    @PutMapping("/{id}")
    public ResponseEntity<CategoriaResponseDTO> actualizarCategoria(
            @Parameter(description = "ID de la categoria a modificar", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody CategoriaRequestDTO request
    ) {
        return ResponseEntity.ok(categoriaService.actualizar(id, request));
    }

    @Operation(
        summary = "Eliminar una categoria",
        description = "Elimina de forma permanente una categoria del sistema mediante su ID."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "244", description = "Categoria eliminada correctamente (No Content)"),
        @ApiResponse(responseCode = "404", description = "Categoria no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCategoria(
            @Parameter(description = "ID de la categoria a eliminar", example = "1")
            @PathVariable Long id) {
        categoriaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}