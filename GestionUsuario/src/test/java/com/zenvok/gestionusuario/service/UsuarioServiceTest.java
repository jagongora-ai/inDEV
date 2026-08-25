package com.zenvok.gestionusuario.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.zenvok.gestionusuario.dto.UsuarioRequestDTO;
import com.zenvok.gestionusuario.dto.UsuarioResponseDTO;
import com.zenvok.gestionusuario.model.TipoUsuario;
import com.zenvok.gestionusuario.model.Usuario;
import com.zenvok.gestionusuario.repository.TipoUsuarioRepository;
import com.zenvok.gestionusuario.repository.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private TipoUsuarioRepository tipoUsuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioService usuarioService;

    private TipoUsuario tipoUsuario;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        tipoUsuario = new TipoUsuario();
        tipoUsuario.setId(1L);
        tipoUsuario.setNombreTipo("Administrador");

        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setRut("12345678-9");
        usuario.setNombre("Adbeel");
        usuario.setApellido("Rodriguez");
        usuario.setCorreo("adbeel@test.com");
        usuario.setContrasena("hash_123");
        usuario.setTipoUsuario(tipoUsuario);
    }

    @Test
    void inicioSesion_DeberiaRetornarUsuario_CuandoCredencialesSonCorrectas() {
        List<Usuario> usuarios = new ArrayList<>();
        usuarios.add(usuario);

        when(usuarioRepository.findAll()).thenReturn(usuarios);
        when(passwordEncoder.matches("12345678", "hash_123")).thenReturn(true);

        UsuarioResponseDTO resultado = usuarioService.inicioSesion("adbeel@test.com", "12345678");

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Adbeel", resultado.getNombre());
        assertEquals("Administrador", resultado.getNombreRol());
    }

    @Test
    void inicioSesion_DeberiaLanzarError_CuandoCorreoNoExiste() {
        when(usuarioRepository.findAll()).thenReturn(new ArrayList<>());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            usuarioService.inicioSesion("otro@test.com", "12345678");
        });

        assertEquals("Credenciales incorrectas.", exception.getMessage());
    }

    @Test
    void inicioSesion_DeberiaLanzarError_CuandoContrasenaEsIncorrecta() {
        List<Usuario> usuarios = new ArrayList<>();
        usuarios.add(usuario);

        when(usuarioRepository.findAll()).thenReturn(usuarios);
        when(passwordEncoder.matches("incorrecta", "hash_123")).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            usuarioService.inicioSesion("adbeel@test.com", "incorrecta");
        });

        assertEquals("Credenciales incorrectas.", exception.getMessage());
    }

    @Test
    void buscarSesion_DeberiaRetornarUsuario_CuandoExiste() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        UsuarioResponseDTO resultado = usuarioService.buscarSesion(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("adbeel@test.com", resultado.getCorreo());
    }

    @Test
    void buscarSesion_DeberiaLanzarError_CuandoNoExiste() {
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            usuarioService.buscarSesion(99L);
        });

        assertEquals("La sesión no existe o expiró.", exception.getMessage());
    }

    @Test
    void cambiarContrasena_DeberiaActualizarContrasena_CuandoDatosSonCorrectos() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("12345678", "hash_123")).thenReturn(true);
        when(passwordEncoder.encode("nueva123")).thenReturn("hash_nuevo");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        usuarioService.cambiarContrasena(1L, "12345678", "nueva123");

        verify(usuarioRepository, times(1)).save(usuario);
        assertEquals("hash_nuevo", usuario.getContrasena());
    }

    @Test
    void cambiarContrasena_DeberiaLanzarError_CuandoUsuarioNoExiste() {
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            usuarioService.cambiarContrasena(99L, "12345678", "nueva123");
        });

        assertEquals("Usuario no encontrado.", exception.getMessage());
    }

    @Test
    void cambiarContrasena_DeberiaLanzarError_CuandoClaveActualEsIncorrecta() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("incorrecta", "hash_123")).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            usuarioService.cambiarContrasena(1L, "incorrecta", "nueva123");
        });

        assertEquals("La contraseña actual es incorrecta.", exception.getMessage());
    }

    @Test
    void crearUsuario_DeberiaCrearUsuario_CuandoDatosSonValidos() {
        UsuarioRequestDTO request = new UsuarioRequestDTO();
        request.setRut("12345678-9");
        request.setNombre("Adbeel");
        request.setApellido("Rodriguez");
        request.setCorreo("adbeel@test.com");
        request.setContrasena("12345678");
        request.setIdTipoUsuario(1L);

        when(tipoUsuarioRepository.findById(1L)).thenReturn(Optional.of(tipoUsuario));
        when(passwordEncoder.encode("12345678")).thenReturn("hash_123");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        UsuarioResponseDTO resultado = usuarioService.crearUsuario(request);

        assertNotNull(resultado);
        assertEquals("12345678-9", resultado.getRut());
        assertEquals("Administrador", resultado.getNombreRol());
    }

    @Test
    void crearUsuario_DeberiaLanzarError_CuandoTipoNoExiste() {
        UsuarioRequestDTO request = new UsuarioRequestDTO();
        request.setIdTipoUsuario(99L);

        when(tipoUsuarioRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            usuarioService.crearUsuario(request);
        });

        assertEquals("El tipo de usuario con ID: 99 no existe.", exception.getMessage());
    }

    @Test
    void obtenerTodosLosUsuarios_DeberiaRetornarLista() {
        List<Usuario> lista = new ArrayList<>();
        lista.add(usuario);

        when(usuarioRepository.findAll()).thenReturn(lista);

        List<UsuarioResponseDTO> resultado = usuarioService.obtenerTodosLosUsuarios();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Adbeel", resultado.get(0).getNombre());
    }

    @Test
    void obtenerUsuarioPorId_DeberiaRetornarUsuario_CuandoExiste() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        Optional<UsuarioResponseDTO> resultado = usuarioService.obtenerUsuarioPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals("Adbeel", resultado.get().getNombre());
    }

    @Test
    void obtenerUsuarioPorId_DeberiaRetornarVacio_CuandoNoExiste() {
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<UsuarioResponseDTO> resultado = usuarioService.obtenerUsuarioPorId(99L);

        assertTrue(resultado.isEmpty());
    }

    @Test
    void obtenerUsuariosPorTipo_DeberiaRetornarLista() {
        List<Usuario> lista = new ArrayList<>();
        lista.add(usuario);

        when(usuarioRepository.listarPorTipo(1L)).thenReturn(lista);

        List<UsuarioResponseDTO> resultado = usuarioService.obtenerUsuariosPorTipo(1L);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Administrador", resultado.get(0).getNombreRol());
    }

    @Test
    void modificarUsuario_DeberiaModificarUsuario_CuandoExiste() {
        UsuarioRequestDTO request = new UsuarioRequestDTO();
        request.setRut("11111111-1");
        request.setNombre("Camilo");
        request.setApellido("Jimenez");
        request.setCorreo("camilo@test.com");
        request.setContrasena("12345678");
        request.setIdTipoUsuario(1L);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(tipoUsuarioRepository.findById(1L)).thenReturn(Optional.of(tipoUsuario));
        when(passwordEncoder.encode("12345678")).thenReturn("hash_nuevo");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        UsuarioResponseDTO resultado = usuarioService.modificarUsuario(1L, request);

        assertNotNull(resultado);
        assertEquals("Camilo", usuario.getNombre());
        assertEquals("hash_nuevo", usuario.getContrasena());
    }

    @Test
    void modificarUsuario_DeberiaLanzarError_CuandoUsuarioNoExiste() {
        UsuarioRequestDTO request = new UsuarioRequestDTO();

        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            usuarioService.modificarUsuario(99L, request);
        });

        assertEquals("No se puede modificar. Usuario con ID 99 no existe.", exception.getMessage());
    }

    @Test
    void modificarUsuario_DeberiaLanzarError_CuandoTipoNoExiste() {
        UsuarioRequestDTO request = new UsuarioRequestDTO();
        request.setIdTipoUsuario(99L);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(tipoUsuarioRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            usuarioService.modificarUsuario(1L, request);
        });

        assertEquals("El tipo de usuario con ID: 99 no existe.", exception.getMessage());
    }

    @Test
    void eliminarUsuarioPorRut_DeberiaEliminarUsuario_CuandoExiste() {
        when(usuarioRepository.buscarPorRut("12345678-9")).thenReturn(Optional.of(usuario));
        doNothing().when(usuarioRepository).delete(usuario);

        usuarioService.eliminarUsuarioPorRut("12345678-9");

        verify(usuarioRepository, times(1)).delete(usuario);
    }

    @Test
    void eliminarUsuarioPorRut_DeberiaLanzarError_CuandoRutNoExiste() {
        when(usuarioRepository.buscarPorRut("99999999-9")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            usuarioService.eliminarUsuarioPorRut("99999999-9");
        });

        assertEquals("No se puede eliminar. El usuario con RUT 99999999-9 no existe.", exception.getMessage());
    }
}