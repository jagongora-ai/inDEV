package com.zenvok.producto.service;

import com.zenvok.producto.client.EstadoClient;
import com.zenvok.producto.dto.EstadoResponseDTO;
import com.zenvok.producto.dto.ProductoRequestDTO;
import com.zenvok.producto.dto.ProductoResponseDTO;
import com.zenvok.producto.exception.CategoriaNotFoundException;
import com.zenvok.producto.exception.ProductoNotFoundException;
import com.zenvok.producto.model.Categoria;
import com.zenvok.producto.model.Producto;
import com.zenvok.producto.repository.CategoriaRepository;
import com.zenvok.producto.repository.ProductoRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductoServiceTest {

    @Mock
    private EstadoClient estadoClient;

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private CategoriaRepository categoriaRepository;

    @InjectMocks
    private ProductoService productoService;

    @Test
    void listar_DeberiaRetornarListaDeProductos() {
        Categoria categoria = crearCategoria();
        Producto producto = crearProducto(categoria);

        when(productoRepository.findAll()).thenReturn(List.of(producto));

        List<ProductoResponseDTO> resultado = productoService.listar();

        assertEquals(1, resultado.size());
        assertEquals(1L, resultado.get(0).getId());
        assertEquals("Notebook Lenovo", resultado.get(0).getNombre());
        assertEquals("Tecnología", resultado.get(0).getCategoriaNombre());

        verify(productoRepository).findAll();
    }

    @Test
    void buscarPorId_CuandoExiste_DeberiaRetornarProducto() {
        Categoria categoria = crearCategoria();
        Producto producto = crearProducto(categoria);

        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));

        ProductoResponseDTO resultado = productoService.buscarPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Notebook Lenovo", resultado.getNombre());
        assertEquals(new BigDecimal("450000"), resultado.getPrecio());
        assertEquals(1L, resultado.getEstadoId());
        assertEquals(1L, resultado.getCategoriaId());

        verify(productoRepository).findById(1L);
    }

    @Test
    void buscarPorId_CuandoNoExiste_DeberiaLanzarExcepcion() {
        when(productoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ProductoNotFoundException.class, () -> productoService.buscarPorId(99L));

        verify(productoRepository).findById(99L);
    }

    @Test
    void guardar_CuandoDatosValidos_DeberiaCrearProducto() {
        ProductoRequestDTO request = crearRequest();
        Categoria categoria = crearCategoria();
        Producto productoGuardado = crearProducto(categoria);
        EstadoResponseDTO estado = crearEstado();

        when(estadoClient.buscarPorId(1L)).thenReturn(estado);
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));
        when(productoRepository.save(any(Producto.class))).thenReturn(productoGuardado);

        ProductoResponseDTO resultado = productoService.guardar(request);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Notebook Lenovo", resultado.getNombre());
        assertEquals(new BigDecimal("450000"), resultado.getPrecio());
        assertEquals("Tecnología", resultado.getCategoriaNombre());

        verify(estadoClient).buscarPorId(1L);
        verify(categoriaRepository).findById(1L);
        verify(productoRepository).save(any(Producto.class));
    }

    @Test
    void guardar_CuandoCategoriaNoExiste_DeberiaLanzarExcepcion() {
        ProductoRequestDTO request = crearRequest();
        EstadoResponseDTO estado = crearEstado();

        when(estadoClient.buscarPorId(1L)).thenReturn(estado);
        when(categoriaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CategoriaNotFoundException.class, () -> productoService.guardar(request));

        verify(estadoClient).buscarPorId(1L);
        verify(categoriaRepository).findById(1L);
        verify(productoRepository, never()).save(any(Producto.class));
    }

    @Test
    void guardar_CuandoEstadoNoExiste_DeberiaLanzarExcepcion() {
        ProductoRequestDTO request = crearRequest();

        when(estadoClient.buscarPorId(1L)).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> productoService.guardar(request));

        verify(estadoClient).buscarPorId(1L);
        verify(categoriaRepository, never()).findById(anyLong());
        verify(productoRepository, never()).save(any(Producto.class));
    }

    @Test
    void guardar_CuandoEstadoSinId_DeberiaLanzarExcepcion() {
        ProductoRequestDTO request = crearRequest();

        EstadoResponseDTO estado = new EstadoResponseDTO();
        estado.setId(null);
        estado.setNombre("Activo");
        estado.setDescripcion("Estado activo");

        when(estadoClient.buscarPorId(1L)).thenReturn(estado);

        assertThrows(IllegalArgumentException.class, () -> productoService.guardar(request));

        verify(estadoClient).buscarPorId(1L);
        verify(categoriaRepository, never()).findById(anyLong());
        verify(productoRepository, never()).save(any(Producto.class));
    }

    @Test
    void actualizar_CuandoExiste_DeberiaActualizarProducto() {
        ProductoRequestDTO request = crearRequest();
        Categoria categoria = crearCategoria();
        Producto productoExistente = crearProducto(categoria);
        Producto productoActualizado = crearProducto(categoria);
        EstadoResponseDTO estado = crearEstado();

        when(productoRepository.findById(1L)).thenReturn(Optional.of(productoExistente));
        when(estadoClient.buscarPorId(1L)).thenReturn(estado);
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));
        when(productoRepository.save(any(Producto.class))).thenReturn(productoActualizado);

        ProductoResponseDTO resultado = productoService.actualizar(1L, request);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Notebook Lenovo", resultado.getNombre());
        assertEquals("Tecnología", resultado.getCategoriaNombre());

        verify(productoRepository).findById(1L);
        verify(estadoClient).buscarPorId(1L);
        verify(categoriaRepository).findById(1L);
        verify(productoRepository).save(any(Producto.class));
    }

    @Test
    void actualizar_CuandoProductoNoExiste_DeberiaLanzarExcepcion() {
        ProductoRequestDTO request = crearRequest();

        when(productoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ProductoNotFoundException.class, () -> productoService.actualizar(99L, request));

        verify(productoRepository).findById(99L);
        verify(estadoClient, never()).buscarPorId(anyLong());
        verify(productoRepository, never()).save(any(Producto.class));
    }

    @Test
    void actualizar_CuandoCategoriaNoExiste_DeberiaLanzarExcepcion() {
        ProductoRequestDTO request = crearRequest();
        Categoria categoria = crearCategoria();
        Producto productoExistente = crearProducto(categoria);
        EstadoResponseDTO estado = crearEstado();

        when(productoRepository.findById(1L)).thenReturn(Optional.of(productoExistente));
        when(estadoClient.buscarPorId(1L)).thenReturn(estado);
        when(categoriaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CategoriaNotFoundException.class, () -> productoService.actualizar(1L, request));

        verify(productoRepository).findById(1L);
        verify(estadoClient).buscarPorId(1L);
        verify(categoriaRepository).findById(1L);
        verify(productoRepository, never()).save(any(Producto.class));
    }

    @Test
    void eliminar_CuandoExiste_DeberiaEliminarProducto() {
        Categoria categoria = crearCategoria();
        Producto producto = crearProducto(categoria);

        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));

        productoService.eliminar(1L);

        verify(productoRepository).findById(1L);
        verify(productoRepository).delete(producto);
    }

    @Test
    void eliminar_CuandoNoExiste_DeberiaLanzarExcepcion() {
        when(productoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ProductoNotFoundException.class, () -> productoService.eliminar(99L));

        verify(productoRepository).findById(99L);
        verify(productoRepository, never()).delete(any(Producto.class));
    }

    @Test
    void buscarPorNombre_DeberiaRetornarProductosCoincidentes() {
        Categoria categoria = crearCategoria();
        Producto producto = crearProducto(categoria);

        when(productoRepository.findByNombreContainingIgnoreCase("note"))
                .thenReturn(List.of(producto));

        List<ProductoResponseDTO> resultado = productoService.buscarPorNombre("note");

        assertEquals(1, resultado.size());
        assertEquals("Notebook Lenovo", resultado.get(0).getNombre());

        verify(productoRepository).findByNombreContainingIgnoreCase("note");
    }

    @Test
    void listarPorCategoria_DeberiaRetornarProductosDeCategoria() {
        Categoria categoria = crearCategoria();
        Producto producto = crearProducto(categoria);

        when(productoRepository.findByCategoriaId(1L)).thenReturn(List.of(producto));

        List<ProductoResponseDTO> resultado = productoService.listarPorCategoria(1L);

        assertEquals(1, resultado.size());
        assertEquals(1L, resultado.get(0).getCategoriaId());

        verify(productoRepository).findByCategoriaId(1L);
    }

    @Test
    void listarPorEstadoId_DeberiaRetornarProductosDeEstado() {
        Categoria categoria = crearCategoria();
        Producto producto = crearProducto(categoria);

        when(productoRepository.findByEstadoId(1L)).thenReturn(List.of(producto));

        List<ProductoResponseDTO> resultado = productoService.listarPorEstadoId(1L);

        assertEquals(1, resultado.size());
        assertEquals(1L, resultado.get(0).getEstadoId());

        verify(productoRepository).findByEstadoId(1L);
    }

    private ProductoRequestDTO crearRequest() {
        ProductoRequestDTO request = new ProductoRequestDTO();
        request.setNombre("Notebook Lenovo");
        request.setDescripcion("Notebook para oficina");
        request.setPrecio(new BigDecimal("450000"));
        request.setEstadoId(1L);
        request.setCategoriaId(1L);
        return request;
    }

    private Categoria crearCategoria() {
        Categoria categoria = new Categoria();
        categoria.setId(1L);
        categoria.setNombre("Tecnología");
        categoria.setDescripcion("Productos tecnológicos");
        return categoria;
    }

    private EstadoResponseDTO crearEstado() {
        EstadoResponseDTO estado = new EstadoResponseDTO();
        estado.setId(1L);
        estado.setNombre("Activo");
        estado.setDescripcion("Estado activo");
        return estado;
    }

    private Producto crearProducto(Categoria categoria) {
        Producto producto = new Producto();
        producto.setId(1L);
        producto.setNombre("Notebook Lenovo");
        producto.setDescripcion("Notebook para oficina");
        producto.setPrecio(new BigDecimal("450000"));
        producto.setEstadoId(1L);
        producto.setCategoria(categoria);
        return producto;
    }
}