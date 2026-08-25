package com.zenvok.producto.controller;

import com.zenvok.producto.dto.ProductoRequestDTO;
import com.zenvok.producto.dto.ProductoResponseDTO;
import com.zenvok.producto.service.ProductoService;

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
@RequestMapping("/api/productos")
public class ProductoController {

@Autowired
    private ProductoService productoService;

    @Operation(
        summary = "Listar todos los productos",
        description = "Obtiene la lista completa de productos registrados."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente")
    })
    @GetMapping
    public ResponseEntity<List<ProductoResponseDTO>> listarProductos() {
        return ResponseEntity.ok(productoService.listar());
    }

    @Operation(
        summary = "Buscar producto por ID",
        description = "Obtiene la informacion de un producto mediante su identificador ID."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Producto encontrado correctamente"),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProductoResponseDTO> buscarProductoPorId(
            @Parameter(description = "ID del producto", example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(productoService.buscarPorId(id));
    }

    @Operation(
        summary = "Listar productos por categoria",
        description = "Obtiene todos los productos pertenecientes a una categoria especifica."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente")
    })
    @GetMapping("/categoria/{categoriaId}")
    public ResponseEntity<List<ProductoResponseDTO>> listarProductosPorCategoria(
            @Parameter(description = "ID de la categoria", example = "1")
            @PathVariable Long categoriaId) {
        return ResponseEntity.ok(productoService.listarPorCategoria(categoriaId));
    }

    @Operation(
        summary = "Crear un producto",
        description = "Registra un nuevo producto en el sistema."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Producto creado correctamente"),
        @ApiResponse(responseCode = "400", description = "Los datos enviados son invalidos")
    })
    @PostMapping
    public ResponseEntity<ProductoResponseDTO> crearProducto(
            @Valid @RequestBody ProductoRequestDTO request
    ) {
        ProductoResponseDTO nuevo = productoService.guardar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }

    @Operation(
        summary = "Actualizar un producto",
        description = "Modifica la informacion de un producto existente mediante su ID."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Producto actualizado correctamente"),
        @ApiResponse(responseCode = "400", description = "Los datos enviados son invalidos"),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ProductoResponseDTO> actualizarProducto(
            @Parameter(description = "ID del producto a modificar", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody ProductoRequestDTO request
    ) {
        return ResponseEntity.ok(productoService.actualizar(id, request));
    }

    @Operation(
        summary = "Eliminar un producto",
        description = "Elimina de forma permanente un producto del sistema mediante su ID."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Producto eliminado correctamente (No Content)"),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(
            @Parameter(description = "ID del producto a eliminar", example = "1")
            @PathVariable Long id) {
        productoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
        summary = "Buscar productos por nombre",
        description = "Obtiene una lista de productos cuyo nombre coincida con el parametro de busqueda."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Busqueda realizada correctamente")
    })
    @GetMapping("/buscar")
    public ResponseEntity<List<ProductoResponseDTO>> buscarPorNombre(
            @Parameter(description = "Nombre o parte del nombre del producto", example = "Teclado")
            @RequestParam String nombre) {
        return ResponseEntity.ok(productoService.buscarPorNombre(nombre));
    }

    @Operation(
        summary = "Listar productos por estado",
        description = "Obtiene todos los productos correspondientes a un ID de estado especifico."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente")
    })
    @GetMapping("/estado/{estadoId}")
    public ResponseEntity<List<ProductoResponseDTO>> listarPorEstado(
            @Parameter(description = "ID del estado del producto", example = "1")
            @PathVariable Long estadoId) {
        return ResponseEntity.ok(productoService.listarPorEstadoId(estadoId));
    }
}