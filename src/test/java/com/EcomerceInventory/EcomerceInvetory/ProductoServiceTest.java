package com.EcomerceInventory.EcomerceInvetory;

import com.EcomerceInventory.EcomerceInvetory.dto.ProductoDTO;
import com.EcomerceInventory.EcomerceInvetory.exception.ResourceNotFoundException;
import com.EcomerceInventory.EcomerceInvetory.model.Categoria;
import com.EcomerceInventory.EcomerceInvetory.model.Producto;
import com.EcomerceInventory.EcomerceInvetory.repository.CategoriaRepository;
import com.EcomerceInventory.EcomerceInvetory.repository.ProductoRepository;
import com.EcomerceInventory.EcomerceInvetory.service.ProductoService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.mockito.Mockito.*; // Para when, times, any, etc.
import static org.junit.jupiter.api.Assertions.*; // Para assertThrows, assertEquals, etc.

import java.util.List;
import java.util.Optional; // ¡Vital para el findById!

@ExtendWith(MockitoExtension.class)
public class ProductoServiceTest {
    @Mock
    ProductoRepository productoRepository;
    @Mock
    CategoriaRepository categoriaRepository;
    @InjectMocks
    ProductoService productoService;

    private Producto productoEjemplo;
    private Categoria categoriaEjemplo;

    @BeforeEach
    void setUp(){
        //Configuracion inicial
        categoriaEjemplo = new Categoria();
        categoriaEjemplo.setId(1L);
        categoriaEjemplo.setNombre("Ropa");

        productoEjemplo = new Producto();
        productoEjemplo.setId(1L);
        productoEjemplo.setActivo(true);
        productoEjemplo.setNombre("Camisa");
        productoEjemplo.setDescripcion("Camisa con manga larga");
        productoEjemplo.setPrecio(2000.0);
        productoEjemplo.setStock(200);
        productoEjemplo.setCategoria(categoriaEjemplo);

        ProductoDTO productoDTOEjemplo = new ProductoDTO();
        productoDTOEjemplo.setActivo(true);
        productoDTOEjemplo.setNombre("Camisa");
        productoDTOEjemplo.setDescripcin("Camisa con manga larga");
        productoDTOEjemplo.setPrecio(2000.0);
        productoDTOEjemplo.setStock(200);
        productoDTOEjemplo.setCategoriaId(1L);
        productoDTOEjemplo.setCategoriaNombre("Ropa");
    }

    //Conversion a dto
    @Test
    @DisplayName("Convertir a dto")
    void convertirADTOTest() {
        //Actuar
        ProductoDTO respuesta = productoService.convertirADTO(productoEjemplo);

        assertNotNull(respuesta, "Deve haber una conversion");
        assertTrue(respuesta.getActivo());
        assertEquals(productoEjemplo.getNombre(), respuesta.getNombre());
        assertEquals(productoEjemplo.getDescripcion(),respuesta.getDescripcin());
        assertEquals(productoEjemplo.getPrecio(), respuesta.getPrecio());
        assertEquals(productoEjemplo.getStock(), respuesta.getStock());
        assertEquals(productoEjemplo.getCategoria().getId(), respuesta.getCategoriaId());
        assertEquals(productoEjemplo.getCategoria().getNombre(), respuesta.getCategoriaNombre());
    }

    //Paginacion
    @Test
    @DisplayName("Paginar los productos")
    void listarPaginaProductosExitoTest(){
        //Arrancar
        int pagina = 0;
        int tamano = 10;

        List<Producto> listarProductos = List.of(productoEjemplo);

        Page<Producto> paginaSimulada = new PageImpl<>(
                listarProductos,
                PageRequest.of(pagina, tamano),
                listarProductos.size()
        );

        when(productoRepository.findAll(any(Pageable.class))).thenReturn(paginaSimulada);

        //Actuar
        Page<ProductoDTO> resultado = productoService.listarPaginaProducto(pagina, tamano);

        //Acertar
        assertNotNull(resultado);
        assertEquals(1, resultado.getTotalElements(), "Debería haber 1 elemento en la página");
        assertEquals("Camisa", resultado.getContent().getFirst().getNombre());

        // Verificamos que se llamó al repositorio con la configuración correcta
        verify(productoRepository).findAll(any(Pageable.class));
    }

    //Busqueda por id
    @Test
    @DisplayName("Busqueda exitosa por id")
    void productoIdIExistenteTest() {
        //Arrancar
        when(productoRepository.findById(1L)).thenReturn(Optional.of(productoEjemplo));

        //Actuar
        ProductoDTO respuesta = productoService.listarProductoId(1L);

        //Acertar
        assertNotNull(respuesta, "Debe haber un producto");
        assertEquals(productoEjemplo.getNombre(), respuesta.getNombre());
        assertEquals(productoEjemplo.getActivo(), respuesta.getActivo());
        assertEquals(productoEjemplo.getPrecio(), respuesta.getPrecio());
        assertEquals(productoEjemplo.getStock(), respuesta.getStock());
        assertEquals(productoEjemplo.getCategoria().getNombre(), respuesta.getCategoriaNombre());
        assertEquals(productoEjemplo.getCategoria().getId(), respuesta.getCategoriaId());

        verify(productoRepository, times(1)).findById(1L);
    }
    @Test
    @DisplayName("Busqueda fallida por id")
    void productoIdInexistente() {
        //Arrancar
        Long IdInvalido = 100L;

        when(productoRepository.findById(IdInvalido)).thenReturn(Optional.empty());

        //Actuar, Acertar
        assertThrows(RuntimeException.class, () -> productoService.listarProductoId(IdInvalido));

        verify(productoRepository, times(1)).findById(IdInvalido);
    }

    //Busqueda por nombre
    @Test
    @DisplayName("Busquedad exito por nombre")
    void productoNombreExitoTest() {
        //Arrancar
        String nombre = "Camisa";

        when(productoRepository.findProductoByNombreStartingWith(nombre)).thenReturn(List.of(productoEjemplo));

        //Actuar
        List<ProductoDTO> respuesta = productoService.listarProductoNombre(nombre);
        //Acertar
        assertNotNull(respuesta);
        assertEquals(nombre, respuesta.getFirst().getNombre());

        verify(productoRepository, times(1)).findProductoByNombreStartingWith(nombre);
    }
    @Test
    @DisplayName("Busqueda fallida por nombre")
    void productoNombreFallidoTest() {
        //Actuar
        String nombreInvalido = "Nada";

        assertThrows(ResourceNotFoundException.class, () -> productoService.listarProductoNombre(nombreInvalido));
    }

    //Busqueda por nombre y activo
    @Test
    @DisplayName("Busqueda por nombre y activo")
    void productoNombreActivoTest() {
        //Arrancar
        String nombre = "Camisa";
        Boolean activo = true;

        when(productoRepository.findProductoByActivoAndNombreStartingWith(activo, nombre)).thenReturn(List.of(productoEjemplo));

        //Actuar
        List<ProductoDTO> respuesta = productoService.listarProductoNombreActivo(nombre, activo);

        //Acertar
        assertNotNull(respuesta);
        assertEquals(nombre, respuesta.getFirst().getNombre());
        assertTrue(respuesta.getFirst().getActivo());

        verify(productoRepository, times(1)).findProductoByActivoAndNombreStartingWith(activo, nombre);
    }
    @Test
    @DisplayName("Busqueda fallida por nombre y categoria")
    void productoNombreActivoFallidoTest() {
        //Arrancar
        String nombreInexistente = "Nada";
        Boolean activoInexistente = false;

        assertThrows(ResourceNotFoundException.class, () -> productoService.listarProductoNombreActivo(nombreInexistente,activoInexistente));
    }

    //Listar producto por categoria
    @Test
    @DisplayName("Busqueda de productos por el nombre de la categoria existe")
    void productoCategoriaExitoTest() {
        //Arrancar
        String nombre = "Ropa";
        when(categoriaRepository.findCategoriaByNombre(nombre)).thenReturn(categoriaEjemplo);
        when(productoRepository.findProductoByCategoria(categoriaEjemplo)).thenReturn(List.of(productoEjemplo));

        //Actuar
        List<ProductoDTO> respuesta = productoService.listarProductosCategoria(categoriaEjemplo.getNombre());

        //Acertar
        assertNotNull(respuesta);
        assertEquals(nombre, respuesta.getFirst().getCategoriaNombre());

        verify(productoRepository, times(1)).findProductoByCategoria(categoriaEjemplo);
    }
    @Test
    @DisplayName("Debe ser nulo por que la categoria no existe")
    void productoCategoriaFallaTest() {
        //Arrancar
        String nombreCategoriaInexistente = "Null";
        assertThrows(RuntimeException.class, () -> productoService.listarProductosCategoria(nombreCategoriaInexistente));
    }

    //Crear producto
    @Test
    @DisplayName("Crear producto")
    void crearProductoDatosValidosTest(){
        //Arrancar
        Producto productoEjemploNuevo = new Producto();
        productoEjemploNuevo.setId(1L);
        productoEjemploNuevo.setActivo(true);
        productoEjemploNuevo.setNombre("Camisa");
        productoEjemploNuevo.setDescripcion("Camisa con manga larga");
        productoEjemploNuevo.setPrecio(2000.0);
        productoEjemploNuevo.setStock(200);
        productoEjemploNuevo.setCategoria(categoriaEjemplo);

        ProductoDTO productoDTOEjemploNuevo = new ProductoDTO();
        productoDTOEjemploNuevo.setActivo(true);
        productoDTOEjemploNuevo.setNombre("Camisa");
        productoDTOEjemploNuevo.setDescripcin("Camisa con manga larga");
        productoDTOEjemploNuevo.setPrecio(2000.0);
        productoDTOEjemploNuevo.setStock(200);
        productoDTOEjemploNuevo.setCategoriaId(1L);
        productoDTOEjemploNuevo.setCategoriaNombre("Ropa");

        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoriaEjemplo));
        when(productoRepository.save(any(Producto.class))).thenReturn(productoEjemploNuevo);

        //Actuar
        ProductoDTO respuesta = productoService.crearProducto(productoDTOEjemploNuevo);

        //Acertar
        assertNotNull(respuesta);
        assertEquals(productoDTOEjemploNuevo.getActivo(), respuesta.getActivo());
        assertEquals(productoDTOEjemploNuevo.getNombre(), respuesta.getNombre());
        assertEquals(productoDTOEjemploNuevo.getDescripcin(), respuesta.getDescripcin());
        assertEquals(productoDTOEjemploNuevo.getPrecio(), respuesta.getPrecio());
        assertEquals(productoDTOEjemploNuevo.getStock(), respuesta.getStock());
        assertEquals(productoDTOEjemploNuevo.getCategoriaId(), respuesta.getCategoriaId());
        assertEquals(productoDTOEjemploNuevo.getCategoriaNombre(), respuesta.getCategoriaNombre());

        verify(productoRepository, times(1)).save(any(Producto.class));
    }

    //Actualizar producto
    @Test
    @DisplayName("Actualizar con datos validos")
    void productoActualizarDatosValidoTest() {
        //Arrancar
        ProductoDTO productoDTONuevo = new ProductoDTO();
        productoDTONuevo.setActivo(false);
        productoDTONuevo.setNombre("Pantalon");
        productoDTONuevo.setDescripcin("Botas anchas");
        productoDTONuevo.setPrecio(3000.0);
        productoDTONuevo.setStock(400);
        productoDTONuevo.setCategoriaId(1L);
        productoDTONuevo.setCategoriaNombre("Ropa");

        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoriaEjemplo));
        when(productoRepository.findById(1L)).thenReturn(Optional.of(productoEjemplo));

        ProductoDTO respuesta = productoService.actualizarProductoId(1L, productoDTONuevo);

        assertNotNull(respuesta);
        assertEquals(productoDTONuevo.getActivo(), respuesta.getActivo());
        assertEquals(productoDTONuevo.getNombre(), respuesta.getNombre());
        assertEquals(productoDTONuevo.getDescripcin(), respuesta.getDescripcin());
        assertEquals(productoDTONuevo.getPrecio(),respuesta.getPrecio());
        assertEquals(productoDTONuevo.getStock(), respuesta.getStock());
        assertEquals(productoDTONuevo.getCategoriaId(), respuesta.getCategoriaId());
        assertEquals(productoDTONuevo.getCategoriaNombre(), respuesta.getCategoriaNombre());

        verify(productoRepository, times(1)).save(any(Producto.class));
    }
    @Test
    @DisplayName("Los datos no cambian cuando son nulos")
    void actualizarProductoDatosNullTest() {
        //Arrancar
        ProductoDTO productoDTONuevo = new ProductoDTO();


        when(productoRepository.findById(1L)).thenReturn(Optional.of(productoEjemplo));

        //Actuar
        ProductoDTO respuesta = productoService.actualizarProductoId(1L, productoDTONuevo);

        //Acertar
        assertNotNull(respuesta);
        assertTrue(respuesta.getActivo());
        assertEquals("Camisa", respuesta.getNombre());
        assertEquals("Camisa con manga larga", respuesta.getDescripcin());
        assertEquals(2000.0, respuesta.getPrecio());
        assertEquals(200, respuesta.getStock());
        assertEquals(1L, respuesta.getCategoriaId());
        assertEquals("Ropa", respuesta.getCategoriaNombre());

        verify(productoRepository, times(1)).findById(1L);
        verify(productoRepository, times(1)).save(any(Producto.class));
    }
    @Test
    @DisplayName("Los datos no cambian cuando son vacios")
    void actualizarProductoDatosBlackTest() {
        //Arrancar
        ProductoDTO productoDTONuevo = new ProductoDTO();
        productoDTONuevo.setNombre("    ");
        productoDTONuevo.setDescripcin("    ");
        productoDTONuevo.setPrecio(-1.0);
        productoDTONuevo.setStock(-1);

        when(productoRepository.findById(1L)).thenReturn(Optional.of(productoEjemplo));

        //Actuar
        ProductoDTO respuesta = productoService.actualizarProductoId(1L, productoDTONuevo);

        //Acertar
        assertNotNull(respuesta);
        assertEquals("Camisa", respuesta.getNombre());
        assertEquals("Camisa con manga larga", respuesta.getDescripcin());
        assertEquals(2000.0, respuesta.getPrecio());
        assertEquals(200, respuesta.getStock());

        verify(productoRepository, times(1)).findById(1L);
        verify(productoRepository, times(1)).save(any(Producto.class));
    }


}
