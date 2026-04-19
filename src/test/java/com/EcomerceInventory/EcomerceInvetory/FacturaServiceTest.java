package com.EcomerceInventory.EcomerceInvetory;

import com.EcomerceInventory.EcomerceInvetory.dto.DetallesFacturaDTO;
import com.EcomerceInventory.EcomerceInvetory.dto.FacturaDTO;
import com.EcomerceInventory.EcomerceInvetory.dto.ProductoDTO;
import com.EcomerceInventory.EcomerceInvetory.model.*;
import com.EcomerceInventory.EcomerceInvetory.repository.*;
import com.EcomerceInventory.EcomerceInvetory.service.CategoriaService;
import com.EcomerceInventory.EcomerceInvetory.service.ClienteService;
import com.EcomerceInventory.EcomerceInvetory.service.FacturaService;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class FacturaServiceTest {
    @Mock
    DetallesFacturaRepository detallesFacturaRepository;
    @InjectMocks
    FacturaService facturaService;

    @Mock
    FacturaRepository facturaRepository;

    @Mock
    ProductoRepository productoRepository;
    @InjectMocks
    ProductoService productoService;

    @Mock
    CategoriaRepository categoriaRepository;
    @InjectMocks
    CategoriaService categoriaService;

    @Mock
    ClienteRepository clienteRepository;
    @InjectMocks
    ClienteService clienteService;

    private Factura facturaEjemplo;
    private DetallesFactura detallesFacturaEjemplo;
    private Cliente clienteEjemplo;
    private Categoria categoriaEjemplo;
    private Producto productoEjemplo;
    private DetallesFacturaDTO detallesFacturaDTOEjemplo;
    private List<DetallesFacturaDTO> detallesFacturaDTOS;

    @BeforeEach
    void setUp(){

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

        clienteEjemplo = new Cliente();
        clienteEjemplo.setId(1L);
        clienteEjemplo.setActivo(true);
        clienteEjemplo.setNombre("Juan Perez");
        clienteEjemplo.setIdentificacion("12345");
        clienteEjemplo.setEdad(25);
        clienteEjemplo.setCorreo("juan@mail.com");
        clienteEjemplo.setPasword("segura123");

        detallesFacturaEjemplo = new DetallesFactura();
        detallesFacturaEjemplo.setId(1L);
        detallesFacturaEjemplo.setProducto(productoEjemplo);
        detallesFacturaEjemplo.setPrecioMomento(2000.0);
        detallesFacturaEjemplo.setCantidad(20);

        detallesFacturaDTOEjemplo = new DetallesFacturaDTO();
        detallesFacturaDTOEjemplo.setNombreProducto("Camisa");
        detallesFacturaDTOEjemplo.setProductoId(1L);
        detallesFacturaDTOEjemplo.setPrecioMomento(2000.0);
        detallesFacturaDTOEjemplo.setCantidad(20);

        detallesFacturaDTOS = new ArrayList<>();
        detallesFacturaDTOS.add(detallesFacturaDTOEjemplo);

        List<DetallesFactura> detallesFacturaList = new ArrayList<>();
        detallesFacturaList.add(detallesFacturaEjemplo);

        facturaEjemplo = new Factura();
        facturaEjemplo.setId(1L);
        facturaEjemplo.setCliente(clienteEjemplo);
        facturaEjemplo.setDetallesFacturas(detallesFacturaList);
        facturaEjemplo.setFecha(LocalDateTime.of(2026, 4, 18, 14, 30, 5, 123456789));
        facturaEjemplo.setTotal(2000.0);

        FacturaDTO facturaDTOEjemplo = new FacturaDTO();
        facturaDTOEjemplo.setCliente(1L);
        facturaDTOEjemplo.setFecha(LocalDateTime.of(2026, 4, 18, 14, 30, 5, 123456789));
        facturaDTOEjemplo.setDetallesFacturas(detallesFacturaDTOS);
    }

    //Convertir a dto
    @Test
    @DisplayName("Convertir a dto")
    void convertADTOTest() {
        //Actuar
        FacturaDTO respuesta = facturaService.convertirADTO(facturaEjemplo);

        assertNotNull(respuesta, "La factura no puede estar vacia");
        assertEquals(1L, respuesta.getCliente());
        assertEquals(LocalDateTime.of(2026, 4, 18, 14, 30, 5, 123456789), respuesta.getFecha());
        assertEquals(facturaEjemplo.getDetallesFacturas().getFirst().getId(), respuesta.getDetallesFacturas().getFirst().getProductoId());

    }

    //Consulta por id
    @Test
    @DisplayName("Consultar por id existente")
    void facturaIdExistenteTest() {
        //Arrancar
        Long idValido = 1L;
        when(facturaRepository.findById(idValido)).thenReturn(Optional.of(facturaEjemplo));

        //Actuar
        FacturaDTO respuesta = facturaService.consultarFacturaId(idValido);

        //Acertar
        assertNotNull(respuesta, "Debe haber una factura");
        assertEquals(1L, respuesta.getCliente());
        assertEquals(LocalDateTime.of(2026, 4, 18, 14, 30, 5, 123456789),respuesta.getFecha());
        assertEquals(2000.0, respuesta.getTotal());
        assertEquals(detallesFacturaEjemplo.getId(), respuesta.getDetallesFacturas().getFirst().getProductoId());

        verify(facturaRepository, times(1)).findById(1L);
    }
    @Test
    @DisplayName("Consultar por id del cliente")
    void facturaIdClienteExistenteTest() {
        //Arrancar
        Long idCliente = 1L;

        when(clienteRepository.findById(idCliente)).thenReturn(Optional.of(clienteEjemplo));
        when(facturaRepository.findFacturaByCliente_Id(idCliente)).thenReturn(List.of(facturaEjemplo));

        //Actuar
        List<FacturaDTO> respuesta = facturaService.consultarFacturaClienteId(idCliente);

        //Acertar
        assertNotNull(respuesta, "Debe haber una factura");
        assertEquals(idCliente, respuesta.getFirst().getCliente());
        assertEquals(1L, respuesta.getFirst().getDetallesFacturas().getFirst().getProductoId());
        assertEquals(LocalDateTime.of(2026, 4, 18, 14, 30, 5, 123456789), respuesta.getFirst().getFecha());

        verify(clienteRepository, times(1)).findById(idCliente);
    }

    //Crear lista de productos validos
    @Test
    @DisplayName("Crear lista de productos")
    void listaDetalleFacturaTest() {
        //Arrancar

        when(productoRepository.findById(1L)).thenReturn(Optional.of(productoEjemplo));

        //Actuar
        List<DetallesFactura> respuesta = facturaService.crearDetallesFactura(detallesFacturaDTOS);

        //Acertar
        assertNotNull(respuesta, "Los detalles de las facturas no pueden ser nulos");
        assertEquals(detallesFacturaDTOS.getFirst().getProductoId(), respuesta.getFirst().getProducto().getId());
        assertEquals(detallesFacturaDTOS.getFirst().getPrecioMomento() * 20, respuesta.getFirst().getPrecioMomento());
        assertEquals(detallesFacturaDTOS.getFirst().getCantidad(), respuesta.getFirst().getCantidad());
    }

    //Crear una factura inicial
    @Test
    @DisplayName("Crear factura incial")
    void facturaCrearFacturaInicialTest() {
        //Arrancar
        Long idCliente = 1L;

        when(clienteRepository.findById(idCliente)).thenReturn(Optional.of(clienteEjemplo));

        //Actuar
        Factura respuesta = facturaService.crearFactura(idCliente);

        //Acertar
        assertNotNull(respuesta, "Debe haber una factura inicializada");
        assertEquals(idCliente, respuesta.getCliente().getId());

        verify(facturaRepository, times(1)).save(any(Factura.class));
    }

    //Crear factura completa
    @Test
    @DisplayName("Debería crear una factura completa procesando stock y totales")
    void facturaCrearFacturaCompletaTest() throws Exception {
        //Arranque

        when(productoRepository.findById(1L)).thenReturn(Optional.of(productoEjemplo));
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(clienteEjemplo));
        when(facturaRepository.save(any(Factura.class))).thenReturn(facturaEjemplo);

        //Actuar
        FacturaDTO respuestaFacturaCompleta = facturaService.crearFacturaCompleta(detallesFacturaDTOS, 1L);

        assertNotNull(respuestaFacturaCompleta);
        assertEquals(1L, respuestaFacturaCompleta.getCliente());
        assertEquals(1L, respuestaFacturaCompleta.getDetallesFacturas().getFirst().getProductoId());
    }
    //Crear factura completa
    @Test
    @DisplayName("Debería generar error por stock insuficiente")
    void facturaSinStockTest() throws Exception {
        //Arranque
        Long idCliente = 1L;

        productoEjemplo.setStock(0);

        when(clienteRepository.findById(idCliente)).thenReturn(Optional.of(clienteEjemplo));
        when(productoRepository.findById(1L)).thenReturn(Optional.of(productoEjemplo));
        when(facturaRepository.save(any(Factura.class))).thenReturn(facturaEjemplo);

        // 2. ACTUAR / ACERTAR
        // Verificamos que se lanza la Exception
        Exception excepcion = assertThrows(Exception.class, () -> {
            facturaService.crearFacturaCompleta(detallesFacturaDTOS, idCliente);
        });

        verify(detallesFacturaRepository, never()).save(any());
    }

}
