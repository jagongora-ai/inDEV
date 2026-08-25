package com.zenvok.gestionusuario.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zenvok.gestionusuario.dto.UsuarioRequestDTO;
import com.zenvok.gestionusuario.dto.UsuarioResponseDTO;
import com.zenvok.gestionusuario.service.UsuarioService;

@WebMvcTest(UsuarioController.class)
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UsuarioService usuarioService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void login_DeberiaRetornarUsuario() throws Exception {
        UsuarioResponseDTO response = new UsuarioResponseDTO();
        response.setId(1L);
        response.setNombre("Adbeel");
        response.setCorreo("adbeel@test.com");

        when(usuarioService.inicioSesion("adbeel@test.com", "12345678")).thenReturn(response);

        mockMvc.perform(post("/api/usuarios/login")
                .param("correo", "adbeel@test.com")
                .param("contrasena", "12345678"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Adbeel"));
    }

    @Test
    void buscarSesion_DeberiaRetornarUsuario() throws Exception {
        UsuarioResponseDTO response = new UsuarioResponseDTO();
        response.setId(1L);
        response.setNombre("Adbeel");

        when(usuarioService.buscarSesion(1L)).thenReturn(response);

        mockMvc.perform(get("/api/usuarios/sesion/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Adbeel"));
    }

    @Test
    void cambiarContrasena_DeberiaRetornarMensaje() throws Exception {
        doNothing().when(usuarioService).cambiarContrasena(1L, "12345678", "nueva123");

        mockMvc.perform(put("/api/usuarios/cambiar-contrasena/1")
                .param("contrasenaActual", "12345678")
                .param("nuevaContrasena", "nueva123"))
                .andExpect(status().isOk());
    }

    @Test
    void crearUsuario_DeberiaRetornarUsuarioCreado() throws Exception {
        UsuarioRequestDTO request = new UsuarioRequestDTO();
        request.setRut("12345678-9");
        request.setNombre("Adbeel");
        request.setApellido("Rodriguez");
        request.setCorreo("adbeel@test.com");
        request.setContrasena("12345678");
        request.setIdTipoUsuario(1L);

        UsuarioResponseDTO response = new UsuarioResponseDTO();
        response.setId(1L);
        response.setRut("12345678-9");
        response.setNombre("Adbeel");
        response.setApellido("Rodriguez");
        response.setCorreo("adbeel@test.com");
        response.setNombreRol("Administrador");

        when(usuarioService.crearUsuario(any(UsuarioRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/api/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Adbeel"))
                .andExpect(jsonPath("$.nombreRol").value("Administrador"));
    }

    @Test
    void crearUsuario_DeberiaRetornarBadRequest_CuandoDatosSonInvalidos() throws Exception {
        UsuarioRequestDTO request = new UsuarioRequestDTO();
        request.setRut("");
        request.setNombre("");
        request.setApellido("");
        request.setCorreo("correo-invalido");
        request.setContrasena("123");
        request.setIdTipoUsuario(null);

        mockMvc.perform(post("/api/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void obtenerTodosLosUsuarios_DeberiaRetornarLista() throws Exception {
        UsuarioResponseDTO response = new UsuarioResponseDTO();
        response.setId(1L);
        response.setNombre("Adbeel");

        when(usuarioService.obtenerTodosLosUsuarios()).thenReturn(Arrays.asList(response));

        mockMvc.perform(get("/api/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Adbeel"));
    }

    @Test
    void obtenerUsuarioPorId_DeberiaRetornarUsuario_CuandoExiste() throws Exception {
        UsuarioResponseDTO response = new UsuarioResponseDTO();
        response.setId(1L);
        response.setNombre("Adbeel");

        when(usuarioService.obtenerUsuarioPorId(1L)).thenReturn(Optional.of(response));

        mockMvc.perform(get("/api/usuarios/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Adbeel"));
    }

    @Test
    void obtenerUsuarioPorId_DeberiaRetornarNotFound_CuandoNoExiste() throws Exception {
        when(usuarioService.obtenerUsuarioPorId(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/usuarios/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void obtenerUsuariosPorTipo_DeberiaRetornarLista() throws Exception {
        UsuarioResponseDTO response = new UsuarioResponseDTO();
        response.setId(1L);
        response.setNombre("Adbeel");
        response.setNombreRol("Administrador");

        when(usuarioService.obtenerUsuariosPorTipo(1L)).thenReturn(Arrays.asList(response));

        mockMvc.perform(get("/api/usuarios/tipo/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombreRol").value("Administrador"));
    }

    @Test
    void modificarUsuario_DeberiaRetornarUsuarioModificado() throws Exception {
        UsuarioRequestDTO request = new UsuarioRequestDTO();
        request.setRut("12345678-9");
        request.setNombre("Camilo");
        request.setApellido("Jimenez");
        request.setCorreo("camilo@test.com");
        request.setContrasena("12345678");
        request.setIdTipoUsuario(1L);

        UsuarioResponseDTO response = new UsuarioResponseDTO();
        response.setId(1L);
        response.setNombre("Camilo");

        when(usuarioService.modificarUsuario(any(Long.class), any(UsuarioRequestDTO.class))).thenReturn(response);

        mockMvc.perform(put("/api/usuarios/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Camilo"));
    }

    @Test
    void eliminarUsuarioPorRut_DeberiaRetornarOk() throws Exception {
        doNothing().when(usuarioService).eliminarUsuarioPorRut("12345678-9");

        mockMvc.perform(delete("/api/usuarios/rut/12345678-9"))
                .andExpect(status().isOk());
    }
}