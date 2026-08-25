package com.zenvok.gestionusuario.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
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

import com.zenvok.gestionusuario.dto.TipoUsuarioDTO;
import com.zenvok.gestionusuario.model.TipoUsuario;
import com.zenvok.gestionusuario.repository.TipoUsuarioRepository;

@ExtendWith(MockitoExtension.class)
class TipoUsuarioServiceTest {

    @Mock
    private TipoUsuarioRepository tipoUsuarioRepository;

    @InjectMocks
    private TipoUsuarioService tipoUsuarioService;

    private TipoUsuario tipoUsuario;
    private TipoUsuarioDTO tipoUsuarioDTO;

    @BeforeEach
    void setUp() {
        tipoUsuario = new TipoUsuario();
        tipoUsuario.setId(1L);
        tipoUsuario.setNombreTipo("Administrador");

        tipoUsuarioDTO = new TipoUsuarioDTO();
        tipoUsuarioDTO.setId(1L);
        tipoUsuarioDTO.setNombreTipo("Administrador");
    }

    @Test
    void crearTipo_DeberiaCrearTipoUsuario() {
        when(tipoUsuarioRepository.save(any(TipoUsuario.class))).thenReturn(tipoUsuario);

        TipoUsuarioDTO resultado = tipoUsuarioService.crearTipo(tipoUsuarioDTO);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Administrador", resultado.getNombreTipo());
        verify(tipoUsuarioRepository, times(1)).save(any(TipoUsuario.class));
    }

    @Test
    void obtenerTodosLosTipos_DeberiaRetornarLista() {
        List<TipoUsuario> lista = new ArrayList<>();
        lista.add(tipoUsuario);

        when(tipoUsuarioRepository.findAll()).thenReturn(lista);

        List<TipoUsuarioDTO> resultado = tipoUsuarioService.obtenerTodosLosTipos();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Administrador", resultado.get(0).getNombreTipo());
    }

    @Test
    void obtenerTipoPorId_DeberiaRetornarTipo_CuandoExiste() {
        when(tipoUsuarioRepository.findById(1L)).thenReturn(Optional.of(tipoUsuario));

        Optional<TipoUsuarioDTO> resultado = tipoUsuarioService.obtenerTipoPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals("Administrador", resultado.get().getNombreTipo());
    }

    @Test
    void obtenerTipoPorId_DeberiaRetornarVacio_CuandoNoExiste() {
        when(tipoUsuarioRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<TipoUsuarioDTO> resultado = tipoUsuarioService.obtenerTipoPorId(99L);

        assertTrue(resultado.isEmpty());
    }
}