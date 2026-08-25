package com.zenvok.Ventas.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.zenvok.Ventas.client.EstadoClient;
import com.zenvok.Ventas.client.UsuarioClient;
import com.zenvok.Ventas.dto.EstadoResponseDTO;
import com.zenvok.Ventas.dto.UsuarioResponseDTO;
import com.zenvok.Ventas.dto.VentaRequestDTO;
import com.zenvok.Ventas.dto.VentaResponseDTO;
import com.zenvok.Ventas.model.Ventas;
import com.zenvok.Ventas.repository.VentasRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("Test unitario para el servicio de Ventas")
public class VentaServiceTest {

    @Mock
    private VentasRepository ventasRepository;

    @Mock
    private UsuarioClient usuarioClient;

    @Mock
    private EstadoClient estadoClient;

    @InjectMocks
    private VentasService ventasService;

    private Ventas ventaEntidad;
    private UsuarioResponseDTO usuarioMock;
    private EstadoResponseDTO estadoMock;

    @BeforeEach
    void setUp() {
        ventaEntidad = new Ventas();
        ventaEntidad.setIdVenta(10L);
        ventaEntidad.setFechaVentas(LocalDate.of(2026, 6, 20));
        ventaEntidad.setTotal(new BigDecimal("1500.50"));
        ventaEntidad.setUsuarioId(1L);
        ventaEntidad.setEstadoId(2L);

        usuarioMock = new UsuarioResponseDTO();
        usuarioMock.setId(1L);
        usuarioMock.setNombre("Juan");
        usuarioMock.setApellido("Pérez");

        estadoMock = new EstadoResponseDTO();
        estadoMock.setId(2L);
        estadoMock.setNombre("APROBADO");
    }

    @Test
    @DisplayName("guardar() debe procesar validaciones externas, persistir y retornar VentaResponseDTO armado")
    void guardar_DeberiaRegistrarVentaExitosamente() {
        VentaRequestDTO request = new VentaRequestDTO();
        request.setFechaVentas(LocalDate.of(2026, 6, 20));
        request.setTotal(new BigDecimal("1500.50"));
        request.setUsuarioId(1L);
        request.setEstadoId(2L);

        when(usuarioClient.buscarPorId(1L)).thenReturn(usuarioMock);
        when(estadoClient.buscarPorId(2L)).thenReturn(estadoMock);
        when(ventasRepository.save(any(Ventas.class))).thenReturn(ventaEntidad);

        VentaResponseDTO resultado = ventasService.guardar(request);

        assertNotNull(resultado);
        assertEquals(10L, resultado.getId());
        assertEquals(new BigDecimal("1500.50"), resultado.getTotal());
        assertEquals("Juan Pérez", resultado.getUsuarioNombre());
        assertEquals("APROBADO", resultado.getEstadoNombre());

        verify(ventasRepository, times(1)).save(any(Ventas.class));
    }

    @Test
    @DisplayName("guardar() debe lanzar error si usuario no existe")
    void guardar_DeberiaLanzarError_CuandoUsuarioNoExiste() {
        VentaRequestDTO request = new VentaRequestDTO();
        request.setFechaVentas(LocalDate.of(2026, 6, 20));
        request.setTotal(new BigDecimal("1500.50"));
        request.setUsuarioId(99L);
        request.setEstadoId(2L);

        when(usuarioClient.buscarPorId(99L)).thenThrow(new RuntimeException("No encontrado"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> ventasService.guardar(request));

        assertEquals("Usuario no encontrado con ID: 99", exception.getMessage());
    }

    @Test
    @DisplayName("guardar() debe lanzar error si usuario viene nulo")
    void guardar_DeberiaLanzarError_CuandoUsuarioEsNull() {
        VentaRequestDTO request = new VentaRequestDTO();
        request.setFechaVentas(LocalDate.of(2026, 6, 20));
        request.setTotal(new BigDecimal("1500.50"));
        request.setUsuarioId(99L);
        request.setEstadoId(2L);

        when(usuarioClient.buscarPorId(99L)).thenReturn(null);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> ventasService.guardar(request));

        assertEquals("Usuario no encontrado con ID: 99", exception.getMessage());
    }

    @Test
    @DisplayName("guardar() debe lanzar error si estado no existe")
    void guardar_DeberiaLanzarError_CuandoEstadoNoExiste() {
        VentaRequestDTO request = new VentaRequestDTO();
        request.setFechaVentas(LocalDate.of(2026, 6, 20));
        request.setTotal(new BigDecimal("1500.50"));
        request.setUsuarioId(1L);
        request.setEstadoId(99L);

        when(usuarioClient.buscarPorId(1L)).thenReturn(usuarioMock);
        when(estadoClient.buscarPorId(99L)).thenThrow(new RuntimeException("No encontrado"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> ventasService.guardar(request));

        assertEquals("Estado no encontrado con ID: 99", exception.getMessage());
    }

    @Test
    @DisplayName("guardar() debe lanzar error si estado viene nulo")
    void guardar_DeberiaLanzarError_CuandoEstadoEsNull() {
        VentaRequestDTO request = new VentaRequestDTO();
        request.setFechaVentas(LocalDate.of(2026, 6, 20));
        request.setTotal(new BigDecimal("1500.50"));
        request.setUsuarioId(1L);
        request.setEstadoId(99L);

        when(usuarioClient.buscarPorId(1L)).thenReturn(usuarioMock);
        when(estadoClient.buscarPorId(99L)).thenReturn(null);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> ventasService.guardar(request));

        assertEquals("Estado no encontrado con ID: 99", exception.getMessage());
    }

    @Test
    @DisplayName("obtenerTodas() debe traer el listado completo resolviendo los nombres")
    void obtenerTodas_DeberiaRetornarListaDeVentas() {
        when(ventasRepository.findAll()).thenReturn(Arrays.asList(ventaEntidad));
        when(usuarioClient.buscarPorId(1L)).thenReturn(usuarioMock);
        when(estadoClient.buscarPorId(2L)).thenReturn(estadoMock);

        List<VentaResponseDTO> resultado = ventasService.obtenerTodas();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Juan Pérez", resultado.get(0).getUsuarioNombre());
        assertEquals("APROBADO", resultado.get(0).getEstadoNombre());

        verify(ventasRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("obtenerTodas() debe retornar nombres no disponibles si fallan los clientes")
    void obtenerTodas_DeberiaRetornarNombresNoDisponibles_CuandoFallanClientes() {
        when(ventasRepository.findAll()).thenReturn(Arrays.asList(ventaEntidad));
        when(usuarioClient.buscarPorId(1L)).thenThrow(new RuntimeException("Error"));
        when(estadoClient.buscarPorId(2L)).thenThrow(new RuntimeException("Error"));

        List<VentaResponseDTO> resultado = ventasService.obtenerTodas();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Usuario no disponible", resultado.get(0).getUsuarioNombre());
        assertEquals("Estado no disponible", resultado.get(0).getEstadoNombre());
    }

    @Test
    @DisplayName("obtenerPorId() debe retornar un Optional con el DTO mapeado si el registro existe")
    void obtenerPorId_DeberiaRetornarVenta_CuandoExiste() {
        when(ventasRepository.findById(10L)).thenReturn(Optional.of(ventaEntidad));
        when(usuarioClient.buscarPorId(1L)).thenReturn(usuarioMock);
        when(estadoClient.buscarPorId(2L)).thenReturn(estadoMock);

        Optional<VentaResponseDTO> resultado = ventasService.obtenerPorId(10L);

        assertTrue(resultado.isPresent());
        assertEquals(10L, resultado.get().getId());
        assertEquals("Juan Pérez", resultado.get().getUsuarioNombre());
        assertEquals("APROBADO", resultado.get().getEstadoNombre());
    }

    @Test
    @DisplayName("obtenerPorId() debe retornar Optional vacío si no existe")
    void obtenerPorId_DeberiaRetornarVacio_CuandoNoExiste() {
        when(ventasRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<VentaResponseDTO> resultado = ventasService.obtenerPorId(99L);

        assertTrue(resultado.isEmpty());
    }
}