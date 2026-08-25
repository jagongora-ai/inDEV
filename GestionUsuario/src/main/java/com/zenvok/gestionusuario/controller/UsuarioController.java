package com.zenvok.gestionusuario.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zenvok.gestionusuario.dto.UsuarioRequestDTO;
import com.zenvok.gestionusuario.dto.UsuarioResponseDTO;
import com.zenvok.gestionusuario.service.UsuarioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@Tag(
    name = "Usuarios", description = "Endpoints para la gestion de usuarios del sistema. Permite registrar, consultar, modificar, eliminar usuarios y administrar el inicio de sesion."
)
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

   @Operation(
    summary = "Iniciar sesion",
    description = "Ingreso de un usuario utilizando su correo y contraseña."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Inicio de sesion exitoso"),
        @ApiResponse(responseCode = "400", description = "Datos ingresados incorrectas")
    })
    @PostMapping("/login")
    public ResponseEntity<UsuarioResponseDTO> login(

            @Parameter(description = "Correo electronico del usuario", example = "adbeel@test.com")
            @RequestParam String correo, 

            @Parameter(description = "Contraseña del usuario", example = "12345678")
            @RequestParam String contrasena) {

        log.info("Controlador: recibida petición de login para el correo: {}", correo);
        UsuarioResponseDTO response = usuarioService.inicioSesion(correo, contrasena);
        return ResponseEntity.ok(response);
    }

   @Operation(
    summary = "Busca la sesion activa",
    description = "Obtiene la informacion correspondiente del usuario que este activo."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sesion encontrada correctamente"),
        @ApiResponse(responseCode = "400", description = "La sesion no existe o expiro")
    })
    @GetMapping("/sesion/{id}")
    public ResponseEntity<UsuarioResponseDTO> buscarSesion(

            @Parameter(description = "ID del usuario", example = "1")
            @PathVariable Long id) {

        log.info("Controlador: recibida petición para buscar sesión activa del ID: {}", id);
        UsuarioResponseDTO response = usuarioService.buscarSesion(id);
        return ResponseEntity.ok(response);
    }

    @Operation(
    summary = "Cambiar contraseña",
    description = "Permite modificar la contraseña de un usuario validando primero la contraseña actual."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Contraseña modificada correctamente"),
        @ApiResponse(responseCode = "400", description = "La contraseña actual es incorrecta o el usuario no existe")
    })
    @PutMapping("/cambiar-contrasena/{id}")
    public ResponseEntity<String> cambiarContrasena(

            @Parameter(description = "ID del usuario", example = "1")
            @PathVariable Long id,

            @Parameter(description = "Contraseña actual", example = "12345678")
            @RequestParam String contrasenaActual,

            @Parameter(description = "Nueva contraseña", example = "NuevaClave123")
            @RequestParam String nuevaContrasena) {

        log.info("Controlador: recibida petición de cambio de clave para usuario ID: {}", id);
        usuarioService.cambiarContrasena(id, contrasenaActual, nuevaContrasena);
        return ResponseEntity.ok("Contraseña modificada correctamente.");
    }

    @Operation(
        summary = "Crear un usuario",
        description = "Registra un nuevo usuario en el sistema."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario creado correctamente"),
        @ApiResponse(responseCode = "400", description = "Datos invalidos o tipo de usuario inexistente")
    })
    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> crearUsuario(@Valid @RequestBody UsuarioRequestDTO request) {
        log.info("Controlador: recibida petición para crear usuario con RUT: {}", request.getRut());
        UsuarioResponseDTO response = usuarioService.crearUsuario(request);
        return ResponseEntity.ok(response);
    }


    @Operation(
    summary = "Listar usuarios",
    description = "Obtiene la lista completa de usuarios registrados."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de usuarios obtenida correctamente")
    })
    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> obtenerTodosLosUsuarios() {
        log.info("Controlador: recibida petición para listar todos los usuarios");
        List<UsuarioResponseDTO> usuarios = usuarioService.obtenerTodosLosUsuarios();
        return ResponseEntity.ok(usuarios);
    }


    @Operation(
    summary = "Buscar usuario por ID",
    description = "Obtiene la informacion de un usuario mediante su identificador ID."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario encontrado"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> obtenerUsuarioPorId(

            @Parameter(description = "ID del usuario", example = "1")
            @PathVariable Long id) {

        log.info("Controlador: recibida petición para buscar usuario por ID: {}", id);
        return usuarioService.obtenerUsuarioPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
    summary = "Listar usuarios por tipo",
    description = "Obtiene todos los usuarios pertenecientes a un tipo de usuario especifico."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente")
    })
    @GetMapping("/tipo/{idTipo}")
    public ResponseEntity<List<UsuarioResponseDTO>> obtenerUsuariosPorTipo(

            @Parameter(description = "ID del tipo de usuario", example = "1")
            @PathVariable Long idTipo) {

        log.info("Controlador: recibida petición para listar usuarios del tipo ID: {}", idTipo);
        List<UsuarioResponseDTO> usuarios = usuarioService.obtenerUsuariosPorTipo(idTipo);
        return ResponseEntity.ok(usuarios);
    }

    @Operation(
        summary = "Modificar usuario",
        description = "Actualiza la informacion de un usuario existente."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario modificado correctamente"),
        @ApiResponse(responseCode = "400", description = "Datos invalidos o usuario no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> modificarUsuario(

        @Parameter(description = "ID del usuario a modificar", example = "1")
        @PathVariable Long id,

        @Valid @RequestBody UsuarioRequestDTO request) {

    log.info("Controlador: recibida petición para modificar usuario ID: {}", id);
    UsuarioResponseDTO response = usuarioService.modificarUsuario(id, request);
    return ResponseEntity.ok(response);
    }

    @Operation(
    summary = "Eliminar usuario por RUT",
    description = "Elimina un usuario del sistema utilizando su RUT."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario eliminado correctamente"),
        @ApiResponse(responseCode = "400", description = "El usuario indicado no existe")
    })
    @DeleteMapping("/rut/{rut}")
    public ResponseEntity<String> eliminarUsuarioPorRut(
            @Parameter(description = "RUT del usuario", example = "12345678-9")
            @PathVariable String rut) {

    log.info("Controlador: recibida petición para eliminar usuario con RUT: {}", rut);
    usuarioService.eliminarUsuarioPorRut(rut);
    return ResponseEntity.ok("Usuario eliminado correctamente.");
}
}