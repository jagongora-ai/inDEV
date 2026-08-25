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

import com.zenvok.Ventas.client.BodegaClient;
import com.zenvok.Ventas.client.ProductoClient;
import com.zenvok.Ventas.dto.BodegaProductoResponseDTO;
import com.zenvok.Ventas.dto.DetalleRequestDTO;
import com.zenvok.Ventas.dto.DetalleResponseDTO;
import com.zenvok.Ventas.dto.ProductoResponseDTO;
import com.zenvok.Ventas.model.Detalle;
import com.zenvok.Ventas.model.Ventas;
import com.zenvok.Ventas.repository.DetalleRepository;
import com.zenvok.Ventas.repository.VentasRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("Test unitario para el servicio de Detalles de Venta")
public class DetalleServiceTest {

    @Mock
    private ProductoClient productoClient;

    @Mock
    private BodegaClient bodegaClient;

    @Mock
    private DetalleRepository detalleRepository;

    @Mock
    private VentasRepository ventasRepository;

    @InjectMocks
    private DetalleService detalleService;

    private Ventas ventaPadre;
    private Detalle detalleEntidad;
    private ProductoResponseDTO productoMock;
    private BodegaProductoResponseDTO bodegaMock;

    @BeforeEach
    void setUp() {
        ventaPadre = new Ventas();
        ventaPadre.setIdVenta(15L);
        ventaPadre.setFechaVentas(LocalDate.of(2026, 6, 20));
        ventaPadre.setTotal(new BigDecimal("1500.50"));

        detalleEntidad = new Detalle();
        detalleEntidad.setId(1L);
        detalleEntidad.setCantidad(2);
        detalleEntidad.setProductoId(100L);
        detalleEntidad.setSubtotal(new BigDecimal("500.25"));
        detalleEntidad.setVenta(ventaPadre);

        productoMock = new ProductoResponseDTO();
        productoMock.setId(100L);
        productoMock.setNombre("Audífonos Bluetooth");

        bodegaMock = new BodegaProductoResponseDTO();
        bodegaMock.setProductoId(100L);
    }

    @Test
    @DisplayName("guardar() debe validar existencias en microservicios, registrar el detalle y mapearlo a DTO")
    void guardar_DeberiaRegistrarDetalleExitosamente() {
        DetalleRequestDTO request = new DetalleRequestDTO();
        request.setCantidad(2);
        request.setProductoId(100L);
        request.setSubtotal(new BigDecimal("500.25"));
        request.setVentaId(15L);

        when(ventasRepository.findById(15L)).thenReturn(Optional.of(ventaPadre));
        when(productoClient.buscarPorId(100L)).thenReturn(productoMock);
        when(bodegaClient.buscarPorProducto(100L)).thenReturn(bodegaMock);
        when(detalleRepository.save(any(Detalle.class))).thenReturn(detalleEntidad);

        DetalleResponseDTO resultado = detalleService.guardar(request);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals(100L, resultado.getProductoId());
        assertEquals("Audífonos Bluetooth", resultado.getProductoNombre());
        assertEquals(15L, resultado.getVentaId());

        verify(detalleRepository, times(1)).save(any(Detalle.class));
    }

    @Test
    @DisplayName("guardar() debe lanzar error si la venta no existe")
    void guardar_DeberiaLanzarError_CuandoVentaNoExiste() {
        DetalleRequestDTO request = new DetalleRequestDTO();
        request.setCantidad(2);
        request.setProductoId(100L);
        request.setSubtotal(new BigDecimal("500.25"));
        request.setVentaId(99L);

        when(ventasRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> detalleService.guardar(request));

        assertEquals("No se encontró la venta con ID: 99", exception.getMessage());
    }

    @Test
    @DisplayName("guardar() debe lanzar error si producto no existe")
    void guardar_DeberiaLanzarError_CuandoProductoNoExiste() {
        DetalleRequestDTO request = new DetalleRequestDTO();
        request.setCantidad(2);
        request.setProductoId(99L);
        request.setSubtotal(new BigDecimal("500.25"));
        request.setVentaId(15L);

        when(ventasRepository.findById(15L)).thenReturn(Optional.of(ventaPadre));
        when(productoClient.buscarPorId(99L)).thenThrow(new RuntimeException("No encontrado"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> detalleService.guardar(request));

        assertEquals("Producto no encontrado con ID: 99", exception.getMessage());
    }

    @Test
    @DisplayName("guardar() debe lanzar error si producto viene nulo")
    void guardar_DeberiaLanzarError_CuandoProductoEsNull() {
        DetalleRequestDTO request = new DetalleRequestDTO();
        request.setCantidad(2);
        request.setProductoId(99L);
        request.setSubtotal(new BigDecimal("500.25"));
        request.setVentaId(15L);

        when(ventasRepository.findById(15L)).thenReturn(Optional.of(ventaPadre));
        when(productoClient.buscarPorId(99L)).thenReturn(null);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> detalleService.guardar(request));

        assertEquals("Producto no encontrado con ID: 99", exception.getMessage());
    }

    @Test
    @DisplayName("guardar() debe lanzar error si producto no está en bodega")
    void guardar_DeberiaLanzarError_CuandoProductoNoEstaEnBodega() {
        DetalleRequestDTO request = new DetalleRequestDTO();
        request.setCantidad(2);
        request.setProductoId(100L);
        request.setSubtotal(new BigDecimal("500.25"));
        request.setVentaId(15L);

        when(ventasRepository.findById(15L)).thenReturn(Optional.of(ventaPadre));
        when(productoClient.buscarPorId(100L)).thenReturn(productoMock);
        when(bodegaClient.buscarPorProducto(100L)).thenThrow(new RuntimeException("No ubicado"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> detalleService.guardar(request));

        assertEquals("Producto no ubicado en bodega con ID: 100", exception.getMessage());
    }

    @Test
    @DisplayName("guardar() debe lanzar error si ubicación en bodega viene nula")
    void guardar_DeberiaLanzarError_CuandoUbicacionEsNull() {
        DetalleRequestDTO request = new DetalleRequestDTO();
        request.setCantidad(2);
        request.setProductoId(100L);
        request.setSubtotal(new BigDecimal("500.25"));
        request.setVentaId(15L);

        when(ventasRepository.findById(15L)).thenReturn(Optional.of(ventaPadre));
        when(productoClient.buscarPorId(100L)).thenReturn(productoMock);
        when(bodegaClient.buscarPorProducto(100L)).thenReturn(null);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> detalleService.guardar(request));

        assertEquals("Producto no ubicado en bodega con ID: 100", exception.getMessage());
    }

    @Test
    @DisplayName("listar() debe retornar todos los detalles formateados con su nombre de producto")
    void listar_DeberiaRetornarListaDeDetalles() {
        when(detalleRepository.findAll()).thenReturn(Arrays.asList(detalleEntidad));
        when(productoClient.buscarPorId(100L)).thenReturn(productoMock);

        List<DetalleResponseDTO> detalles = detalleService.listar();

        assertNotNull(detalles);
        assertEquals(1, detalles.size());
        assertEquals("Audífonos Bluetooth", detalles.get(0).getProductoNombre());

        verify(detalleRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("listar() debe retornar producto no disponible si falla producto-service")
    void listar_DeberiaRetornarProductoNoDisponible_CuandoFallaProductoService() {
        when(detalleRepository.findAll()).thenReturn(Arrays.asList(detalleEntidad));
        when(productoClient.buscarPorId(100L)).thenThrow(new RuntimeException("Error"));

        List<DetalleResponseDTO> detalles = detalleService.listar();

        assertNotNull(detalles);
        assertEquals(1, detalles.size());
        assertEquals("Producto no disponible", detalles.get(0).getProductoNombre());
    }

    @Test
    @DisplayName("findById() debe retornar el DTO empaquetado en un Optional cuando el ID existe")
    void findById_DeberiaRetornarDetalle_CuandoExiste() {
        when(detalleRepository.findById(1L)).thenReturn(Optional.of(detalleEntidad));
        when(productoClient.buscarPorId(100L)).thenReturn(productoMock);

        Optional<DetalleResponseDTO> resultado = detalleService.findById(1L);

        assertTrue(resultado.isPresent());
        assertEquals(1L, resultado.get().getId());
        assertEquals("Audífonos Bluetooth", resultado.get().getProductoNombre());
    }

    @Test
    @DisplayName("findById() debe retornar Optional vacío cuando no existe")
    void findById_DeberiaRetornarVacio_CuandoNoExiste() {
        when(detalleRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<DetalleResponseDTO> resultado = detalleService.findById(99L);

        assertTrue(resultado.isEmpty());
    }

    @Test
    @DisplayName("obtenerDetallesPorVenta() debe retornar detalles de una venta")
    void obtenerDetallesPorVenta_DeberiaRetornarLista() {
        when(detalleRepository.findByVentaIdVenta(15L)).thenReturn(Arrays.asList(detalleEntidad));
        when(productoClient.buscarPorId(100L)).thenReturn(productoMock);

        List<DetalleResponseDTO> resultado = detalleService.obtenerDetallesPorVenta(15L);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(15L, resultado.get(0).getVentaId());
        assertEquals("Audífonos Bluetooth", resultado.get(0).getProductoNombre());
    }
}