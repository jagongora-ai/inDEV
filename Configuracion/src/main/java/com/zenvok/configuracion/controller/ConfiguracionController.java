package com.zenvok.configuracion.controller;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zenvok.configuracion.dto.ConfiguracionDTO;
import com.zenvok.configuracion.service.ConfiguracionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/configuraciones")
@RequiredArgsConstructor
public class ConfiguracionController {

    private final ConfiguracionService configuracionService;

    @Operation(summary = "Crear una nueva configuracion", description = "Registra una nueva configuracion global en el sistema.")
    @ApiResponses(value = { @ApiResponse(responseCode = "201", description = "Configuracion creada correctamente"), @ApiResponse(responseCode = "400", description = "Datos invalidos") })
    @PostMapping
    public ResponseEntity<ConfiguracionDTO> crearConfiguracion(@Valid @RequestBody ConfiguracionDTO dto) {
        ConfiguracionDTO nueva = configuracionService.guardarConfiguracion(dto);
        return new ResponseEntity<>(nueva, HttpStatus.CREATED);
    }

    @Operation(summary = "Listar todas las configuraciones", description = "Obtiene una lista con todas las configuraciones del sistema.")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente") })
    @GetMapping
    public ResponseEntity<List<ConfiguracionDTO>> listarTodas() {
        return ResponseEntity.ok(configuracionService.obtenerTodas());
    }

    @Operation(summary = "Buscar configuracion por clave", description = "Busca los detalles de una configuracion especifica mediante su clave unica.")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Configuracion encontrada"), @ApiResponse(responseCode = "404", description = "Configuracion no encontrada") })
    @GetMapping("/clave/{clave}")
    public ResponseEntity<ConfiguracionDTO> buscarPorClave(@Parameter(description = "Clave unica de la configuracion", example = "MAX_RETRY_ATTEMPTS") @PathVariable String clave) {
        return configuracionService.obtenerPorClave(clave)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Actualizar configuracion por clave", description = "Modifica el valor o parametros de una configuracion existente por su clave.")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Configuracion actualizada correctamente"), @ApiResponse(responseCode = "404", description = "Configuracion no encontrada") })
    @PutMapping("/clave/{clave}")
    public ResponseEntity<ConfiguracionDTO> actualizarConfiguracion(@Parameter(description = "Clave unica de la configuracion a actualizar", example = "MAX_RETRY_ATTEMPTS") @PathVariable String clave, @Valid @RequestBody ConfiguracionDTO dto) {
        return ResponseEntity.ok(configuracionService.modificarConfiguracion(clave, dto));
    }
}