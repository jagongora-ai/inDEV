package com.zenvok.gestionusuario.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.zenvok.gestionusuario.dto.TipoUsuarioDTO;
import com.zenvok.gestionusuario.service.TipoUsuarioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@Tag(name = "Tipos de Usuario", description = "Endpoints para la gestion de Tipos de Usuario, las cuales permite crear y consultar")
@RequestMapping("/api/usuarios/tipos")
public class TipoUsuarioController {

    private final TipoUsuarioService tipoUsuarioService = null;
    
    @Operation(
    summary = "Crear un tipo de usuario",
    description = "Registra un nuevo tipo de usuario en el sistema."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tipo de usuario creado correctamente"),
        @ApiResponse(responseCode = "400", description = "Los datos enviados son inválidos")
    })
    @PostMapping
    public ResponseEntity<TipoUsuarioDTO> crearTipo(@RequestBody TipoUsuarioDTO dto) {
        log.info("Controlador: Recibida petición para crear el tipo de usuario: {}", dto.getNombreTipo());
        TipoUsuarioDTO response = tipoUsuarioService.crearTipo(dto);
        return ResponseEntity.ok(response);
    }

    @Operation(
    summary = "Listar todos los usuarios",
    description = "Lista a todos los usuarios del sistema que esten registrados."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de tipos de usuario obtenida correctamente"),
        @ApiResponse(responseCode = "400", description = "No existen tipos de usuario registrados")
    })
    @GetMapping
    public ResponseEntity<List<TipoUsuarioDTO>> obtenerTodosLosTipos() {
        log.info("Controlador: Recibida petición para listar todos los tipos de usuario");
        List<TipoUsuarioDTO> tipos = tipoUsuarioService.obtenerTodosLosTipos();
        return ResponseEntity.ok(tipos);
    }

    @Operation(
    summary = "Buscar tipo de usuario por ID",
    description = "Obtiene la informacion de un tipo de usuaruio buscando por su id."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tipo de usuario encontrado correctamente"),
        @ApiResponse(responseCode = "404", description = "No existe un tipo de usuario con ese id")
    })
    @GetMapping("/{id}")
    public ResponseEntity<TipoUsuarioDTO> obtenerTipoPorId(
        @Parameter(description = "ID del tipo de usuario", example = "1")
        @PathVariable Long id) {

        log.info("Controlador: Recibida petición para buscar tipo de usuario ID: {}", id);
        return tipoUsuarioService.obtenerTipoPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}