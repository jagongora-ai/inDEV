package com.zenvok.configuracion.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import com.zenvok.configuracion.dto.ConfiguracionDTO;
import com.zenvok.configuracion.model.Configuracion;
import com.zenvok.configuracion.repository.ConfiguracionRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ConfiguracionServiceTest {

    @Mock
    private ConfiguracionRepository repository;

    @InjectMocks
    private ConfiguracionService configuracionService;

    private Configuracion configuracion;

    @BeforeEach
    void setUp() {
        configuracion = new Configuracion(1L, "iva", "19");
    }

    @Test
    void guardarConfiguracion_DeberiaCrearConfiguracion() {
        ConfiguracionDTO request = new ConfiguracionDTO(null, "iva", "19");

        when(repository.findByClave("iva")).thenReturn(Optional.empty());
        when(repository.save(any(Configuracion.class))).thenReturn(configuracion);

        ConfiguracionDTO response = configuracionService.guardarConfiguracion(request);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("iva", response.getClave());
        assertEquals("19", response.getValor());
        verify(repository, times(1)).save(any(Configuracion.class));
    }

    @Test
    void guardarConfiguracion_DeberiaLanzarError_CuandoClaveExiste() {
        ConfiguracionDTO request = new ConfiguracionDTO(null, "iva", "19");

        when(repository.findByClave("iva")).thenReturn(Optional.of(configuracion));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> configuracionService.guardarConfiguracion(request));

        assertEquals("La clave ya existe: iva", exception.getMessage());
    }

    @Test
    void obtenerTodas_DeberiaRetornarLista() {
        when(repository.findAll()).thenReturn(List.of(configuracion));

        List<ConfiguracionDTO> response = configuracionService.obtenerTodas();

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals("iva", response.get(0).getClave());
        assertEquals("19", response.get(0).getValor());
    }

    @Test
    void obtenerTodas_DeberiaRetornarListaVacia() {
        when(repository.findAll()).thenReturn(List.of());

        List<ConfiguracionDTO> response = configuracionService.obtenerTodas();

        assertNotNull(response);
        assertTrue(response.isEmpty());
    }

    @Test
    void obtenerPorClave_DeberiaRetornarConfiguracion() {
        when(repository.findByClave("iva")).thenReturn(Optional.of(configuracion));

        Optional<ConfiguracionDTO> response = configuracionService.obtenerPorClave("iva");

        assertTrue(response.isPresent());
        assertEquals(1L, response.get().getId());
        assertEquals("iva", response.get().getClave());
        assertEquals("19", response.get().getValor());
    }

    @Test
    void obtenerPorClave_DeberiaRetornarVacio() {
        when(repository.findByClave("descuento")).thenReturn(Optional.empty());

        Optional<ConfiguracionDTO> response = configuracionService.obtenerPorClave("descuento");

        assertTrue(response.isEmpty());
    }

    @Test
    void modificarConfiguracion_DeberiaActualizarValor() {
        ConfiguracionDTO request = new ConfiguracionDTO(null, "iva", "20");
        Configuracion actualizada = new Configuracion(1L, "iva", "20");

        when(repository.findByClave("iva")).thenReturn(Optional.of(configuracion));
        when(repository.save(any(Configuracion.class))).thenReturn(actualizada);

        ConfiguracionDTO response = configuracionService.modificarConfiguracion("iva", request);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("iva", response.getClave());
        assertEquals("20", response.getValor());
        verify(repository, times(1)).save(any(Configuracion.class));
    }

    @Test
    void modificarConfiguracion_DeberiaLanzarError_CuandoClaveNoExiste() {
        ConfiguracionDTO request = new ConfiguracionDTO(null, "iva", "20");

        when(repository.findByClave("iva")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> configuracionService.modificarConfiguracion("iva", request));

        assertEquals("Clave no encontrada: iva", exception.getMessage());
    }
}
