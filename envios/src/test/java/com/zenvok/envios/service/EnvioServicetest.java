package com.zenvok.envios.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.zenvok.envios.client.DireccionClient;
import com.zenvok.envios.client.EstadoClient;
import com.zenvok.envios.client.VentaClient;
import com.zenvok.envios.dto.DireccionResponse;
import com.zenvok.envios.dto.EnvioRequest;
import com.zenvok.envios.dto.EnvioResponse;
import com.zenvok.envios.model.Envio;
import com.zenvok.envios.repository.EnvioRepository;

@ExtendWith(MockitoExtension.class)
class EnvioServiceTest {

    @Mock
    private EnvioRepository envioRepo;

    @Mock
    private DireccionClient direccionClient;

    @Mock
    private VentaClient ventaClient;

    @Mock
    private EstadoClient estadoClient;

    @InjectMocks
    private EnvioService envioService;

    private Envio envio;
    private EnvioRequest request;
    private DireccionResponse direccionResponse;

    @BeforeEach
    void setUp() {
        envio = new Envio();
        envio.setId(1L);
        envio.setFechaEnvio("2026-06-20");
        envio.setFechaEmbargue("2026-06-21");
        envio.setComentarios("Entrega programada");
        envio.setDireccionId(1L);
        envio.setVentaId(1L);
        envio.setEstadoId(1L);

        request = new EnvioRequest();
        request.setFechaEnvio("2026-06-20");
        request.setFechaEmbargue("2026-06-21");
        request.setComentarios("Entrega programada");
        request.setDireccionId(1L);
        request.setVentaId(1L);
        request.setEstadoId(1L);

        direccionResponse = new DireccionResponse();
        direccionResponse.setId(1L);
        direccionResponse.setCalle("Alameda");
        direccionResponse.setNumeracion("1234");
        direccionResponse.setBlock("Torre A");
        direccionResponse.setEstado_ID_Estado(1L);
        direccionResponse.setComunas_ID_Comunas(1L);
        direccionResponse.setUsuarios_ID_Usuarios(1L);
    }

    @Test
    void listarEnvios_DeberiaRetornarLista() {
        when(envioRepo.findAll()).thenReturn(List.of(envio));

        List<EnvioResponse> resultado = envioService.listarEnvios();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("2026-06-20", resultado.get(0).getFechaEnvio());
        assertEquals(1L, resultado.get(0).getDireccionId());
    }

    @Test
    void obtenerPorId_DeberiaRetornarEnvio_CuandoExiste() {
        when(envioRepo.findById(1L)).thenReturn(Optional.of(envio));

        EnvioResponse resultado = envioService.obtenerPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Entrega programada", resultado.getComentarios());
    }

    @Test
    void obtenerPorId_DeberiaLanzarError_CuandoNoExiste() {
        when(envioRepo.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            envioService.obtenerPorId(99L);
        });

        assertTrue(exception.getMessage().contains("Envio no encontrado") || exception.getMessage().contains("Envío no encontrado"));
    }

    @Test
    void buscarPorVenta_DeberiaRetornarLista() {
        when(envioRepo.findByVentaId(1L)).thenReturn(List.of(envio));

        List<EnvioResponse> resultado = envioService.buscarPorVenta(1L);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(1L, resultado.get(0).getVentaId());
    }

    @Test
    void buscarPorDireccion_DeberiaRetornarLista() {
        when(envioRepo.findByDireccionId(1L)).thenReturn(List.of(envio));

        List<EnvioResponse> resultado = envioService.buscarPorDireccion(1L);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(1L, resultado.get(0).getDireccionId());
    }

    @Test
    void buscarPorEstado_DeberiaRetornarLista() {
        when(envioRepo.findByEstadoId(1L)).thenReturn(List.of(envio));

        List<EnvioResponse> resultado = envioService.buscarPorEstado(1L);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(1L, resultado.get(0).getEstadoId());
    }

    @Test
    void crearEnvio_DeberiaCrearEnvio_CuandoDatosSonValidos() {
        when(direccionClient.buscarPorId(1L)).thenReturn(direccionResponse);
        when(ventaClient.buscarPorId(1L)).thenReturn("Venta encontrada");
        when(estadoClient.buscarPorId(1L)).thenReturn("Estado encontrado");
        when(envioRepo.save(any(Envio.class))).thenReturn(envio);

        EnvioResponse resultado = envioService.crearEnvio(request);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("2026-06-20", resultado.getFechaEnvio());
        assertEquals(1L, resultado.getDireccionId());
        verify(envioRepo, times(1)).save(any(Envio.class));
    }

    @Test
    void crearEnvio_DeberiaLanzarError_CuandoDireccionNoExiste() {
        when(direccionClient.buscarPorId(1L)).thenThrow(new RuntimeException("No encontrado"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            envioService.crearEnvio(request);
        });

        assertTrue(exception.getMessage().contains("dirección") || exception.getMessage().contains("direccion"));
    }

    @Test
    void crearEnvio_DeberiaLanzarError_CuandoVentaNoExiste() {
        when(direccionClient.buscarPorId(1L)).thenReturn(direccionResponse);
        when(ventaClient.buscarPorId(1L)).thenThrow(new RuntimeException("No encontrado"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            envioService.crearEnvio(request);
        });

        assertTrue(exception.getMessage().contains("venta"));
    }

    @Test
    void crearEnvio_DeberiaLanzarError_CuandoEstadoNoExiste() {
        when(direccionClient.buscarPorId(1L)).thenReturn(direccionResponse);
        when(ventaClient.buscarPorId(1L)).thenReturn("Venta encontrada");
        when(estadoClient.buscarPorId(1L)).thenThrow(new RuntimeException("No encontrado"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            envioService.crearEnvio(request);
        });

        assertTrue(exception.getMessage().contains("estado"));
    }

    @Test
    void actualizarEnvio_DeberiaActualizarEnvio_CuandoDatosSonValidos() {
        Envio envioActualizado = new Envio();
        envioActualizado.setId(1L);
        envioActualizado.setFechaEnvio("2026-07-01");
        envioActualizado.setFechaEmbargue("2026-07-02");
        envioActualizado.setComentarios("Actualizado");
        envioActualizado.setDireccionId(1L);
        envioActualizado.setVentaId(1L);
        envioActualizado.setEstadoId(1L);

        request.setFechaEnvio("2026-07-01");
        request.setFechaEmbargue("2026-07-02");
        request.setComentarios("Actualizado");

        when(envioRepo.findById(1L)).thenReturn(Optional.of(envio));
        when(direccionClient.buscarPorId(1L)).thenReturn(direccionResponse);
        when(ventaClient.buscarPorId(1L)).thenReturn("Venta encontrada");
        when(estadoClient.buscarPorId(1L)).thenReturn("Estado encontrado");
        when(envioRepo.save(any(Envio.class))).thenReturn(envioActualizado);

        EnvioResponse resultado = envioService.actualizarEnvio(1L, request);

        assertNotNull(resultado);
        assertEquals("2026-07-01", resultado.getFechaEnvio());
        assertEquals("Actualizado", resultado.getComentarios());
    }

    @Test
    void actualizarEnvio_DeberiaLanzarError_CuandoEnvioNoExiste() {
        when(envioRepo.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            envioService.actualizarEnvio(99L, request);
        });

        assertTrue(exception.getMessage().contains("Envio no encontrado") || exception.getMessage().contains("Envío no encontrado"));
    }

    @Test
    void eliminarEnvio_DeberiaEliminarEnvio_CuandoExiste() {
        when(envioRepo.findById(1L)).thenReturn(Optional.of(envio));
        doNothing().when(envioRepo).delete(envio);

        envioService.eliminarEnvio(1L);

        verify(envioRepo, times(1)).delete(envio);
    }

    @Test
    void eliminarEnvio_DeberiaLanzarError_CuandoNoExiste() {
        when(envioRepo.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            envioService.eliminarEnvio(99L);
        });

        assertTrue(exception.getMessage().contains("Envio no encontrado") || exception.getMessage().contains("Envío no encontrado"));
    }
}