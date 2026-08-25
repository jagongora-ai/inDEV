package com.zenvok.gestionusuario.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zenvok.gestionusuario.dto.UsuarioRequestDTO;
import com.zenvok.gestionusuario.dto.UsuarioResponseDTO;
import com.zenvok.gestionusuario.model.TipoUsuario;
import com.zenvok.gestionusuario.model.Usuario;
import com.zenvok.gestionusuario.repository.TipoUsuarioRepository;
import com.zenvok.gestionusuario.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final TipoUsuarioRepository tipoUsuarioRepository;
    private final PasswordEncoder passwordEncoder;

    private UsuarioResponseDTO mapToDTO(Usuario usuario) {
        UsuarioResponseDTO dto = new UsuarioResponseDTO();
        dto.setId(usuario.getId());
        dto.setRut(usuario.getRut());
        dto.setNombre(usuario.getNombre());
        dto.setApellido(usuario.getApellido());
        dto.setCorreo(usuario.getCorreo());

        if (usuario.getTipoUsuario() != null) {
            dto.setNombreRol(usuario.getTipoUsuario().getNombreTipo());
        }

        return dto;
    }

    @Transactional(readOnly = true)
    public UsuarioResponseDTO inicioSesion(String correo, String contrasena) {
        log.info("Intentando iniciar sesión para correo: {}", correo);

        Usuario usuario = usuarioRepository.findAll()
                .stream()
                .filter(u -> u.getCorreo().equalsIgnoreCase(correo))
                .findFirst()
                .orElseThrow(() -> {
                    log.warn("Fallo de inicio de sesión: el correo {} no existe", correo);
                    return new RuntimeException("Credenciales incorrectas.");
                });

        if (!passwordEncoder.matches(contrasena, usuario.getContrasena())) {
            log.warn("Fallo de inicio de sesión: contraseña incorrecta para {}", correo);
            throw new RuntimeException("Credenciales incorrectas.");
        }

        log.info("Inicio de sesión exitoso para el usuario ID: {}", usuario.getId());
        return mapToDTO(usuario);
    }

    @Transactional(readOnly = true)
    public UsuarioResponseDTO buscarSesion(Long id) {
        log.info("Buscando sesión activa para el usuario ID: {}", id);

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("No se encontró sesión: el ID {} no existe", id);
                    return new RuntimeException("La sesión no existe o expiró.");
                });

        return mapToDTO(usuario);
    }

    @Transactional
    public void cambiarContrasena(Long id, String contrasenaActual, String nuevaContrasena) {
        log.info("Solicitud para cambiar contraseña del usuario ID: {}", id);

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Error al cambiar contraseña: ID {} no existe", id);
                    return new RuntimeException("Usuario no encontrado.");
                });

        if (!passwordEncoder.matches(contrasenaActual, usuario.getContrasena())) {
            log.warn("Error al cambiar contraseña: clave actual incorrecta para ID {}", id);
            throw new RuntimeException("La contraseña actual es incorrecta.");
        }

        usuario.setContrasena(passwordEncoder.encode(nuevaContrasena));
        usuarioRepository.save(usuario);
        log.info("Contraseña cambiada con éxito para el usuario ID: {}", id);
    }

    @Transactional
    public UsuarioResponseDTO crearUsuario(UsuarioRequestDTO request) {
        log.info("Creando nuevo usuario con RUT: {}", request.getRut());

        TipoUsuario tipo = tipoUsuarioRepository.findById(request.getIdTipoUsuario())
                .orElseThrow(() -> {
                    log.warn("Error al crear usuario: tipo de usuario {} no existe", request.getIdTipoUsuario());
                    return new RuntimeException("El tipo de usuario con ID: " + request.getIdTipoUsuario() + " no existe.");
                });

        Usuario usuario = new Usuario();
        usuario.setRut(request.getRut());
        usuario.setNombre(request.getNombre());
        usuario.setApellido(request.getApellido());
        usuario.setCorreo(request.getCorreo());
        usuario.setContrasena(passwordEncoder.encode(request.getContrasena()));
        usuario.setTipoUsuario(tipo);

        Usuario nuevoUsuario = usuarioRepository.save(usuario);
        log.info("Usuario creado exitosamente con ID: {}", nuevoUsuario.getId());
        return mapToDTO(nuevoUsuario);
    }

    @Transactional(readOnly = true)
    public List<UsuarioResponseDTO> obtenerTodosLosUsuarios() {
        log.info("Obteniendo la lista de todos los usuarios");

        return usuarioRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<UsuarioResponseDTO> obtenerUsuarioPorId(Long id) {
        log.info("Buscando usuario por ID: {}", id);
        return usuarioRepository.findById(id).map(this::mapToDTO);
    }

    @Transactional(readOnly = true)
    public List<UsuarioResponseDTO> obtenerUsuariosPorTipo(Long idTipo) {
        log.info("Obteniendo usuarios del tipo ID: {}", idTipo);

        return usuarioRepository.listarPorTipo(idTipo)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public UsuarioResponseDTO modificarUsuario(Long id, UsuarioRequestDTO request) {
        log.info("Modificando usuario con ID: {}", id);

        Usuario usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Error al modificar: usuario ID {} no existe", id);
                    return new RuntimeException("No se puede modificar. Usuario con ID " + id + " no existe.");
                });

        TipoUsuario tipo = tipoUsuarioRepository.findById(request.getIdTipoUsuario())
                .orElseThrow(() -> {
                    log.warn("Error al modificar: tipo de usuario {} no existe", request.getIdTipoUsuario());
                    return new RuntimeException("El tipo de usuario con ID: " + request.getIdTipoUsuario() + " no existe.");
                });

        usuarioExistente.setRut(request.getRut());
        usuarioExistente.setNombre(request.getNombre());
        usuarioExistente.setApellido(request.getApellido());
        usuarioExistente.setCorreo(request.getCorreo());
        usuarioExistente.setContrasena(passwordEncoder.encode(request.getContrasena()));
        usuarioExistente.setTipoUsuario(tipo);

        Usuario usuarioActualizado = usuarioRepository.save(usuarioExistente);
        log.info("Usuario ID {} modificado correctamente", id);
        return mapToDTO(usuarioActualizado);
    }

    @Transactional
    public void eliminarUsuarioPorRut(String rut) {
        log.info("Eliminando usuario con RUT: {}", rut);

        Usuario usuario = usuarioRepository.buscarPorRut(rut)
                .orElseThrow(() -> {
                    log.warn("Error al eliminar: RUT {} no existe", rut);
                    return new RuntimeException("No se puede eliminar. El usuario con RUT " + rut + " no existe.");
                });

        usuarioRepository.delete(usuario);
        log.info("Usuario con RUT {} eliminado correctamente", rut);
    }
}