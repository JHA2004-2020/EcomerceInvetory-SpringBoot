package com.EcomerceInventory.EcomerceInvetory;

import com.EcomerceInventory.EcomerceInvetory.dto.TarjetaPuntosDTO;
import com.EcomerceInventory.EcomerceInvetory.exception.ResourceNotFoundException;
import com.EcomerceInventory.EcomerceInvetory.model.Cliente;
import com.EcomerceInventory.EcomerceInvetory.model.TarjetaPuntos;
import com.EcomerceInventory.EcomerceInvetory.repository.ClienteRepository;
import com.EcomerceInventory.EcomerceInvetory.repository.TarjetaPuntosRepository;
import com.EcomerceInventory.EcomerceInvetory.service.ClienteService;
import com.EcomerceInventory.EcomerceInvetory.service.TarjetaPuntosService;
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


@ExtendWith(MockitoExtension.class) // Inicializa los mocks de Mockito
public class TarjetaPuntosServiceTest {

    @Mock
    private TarjetaPuntosRepository tarjetaPuntosRepository;
    @InjectMocks
    private TarjetaPuntosService tarjetaPuntosService;

    @Mock
    private ClienteRepository clienteRepository;
    @InjectMocks
    private ClienteService clienteService;

    private TarjetaPuntos tarjetaPuntosEjemplo;
    private Cliente clienteEjemplo;

    @BeforeEach
    void setUp() {
        clienteEjemplo = new Cliente();
        clienteEjemplo.setId(1L);
        clienteEjemplo.setActivo(true);
        clienteEjemplo.setNombre("Juan Perez");
        clienteEjemplo.setIdentificacion("12345");
        clienteEjemplo.setEdad(25);
        clienteEjemplo.setCorreo("juan@mail.com");
        clienteEjemplo.setPasword("segura123");

        tarjetaPuntosEjemplo = new TarjetaPuntos();
        tarjetaPuntosEjemplo.setActivo(true);
        tarjetaPuntosEjemplo.setPuntos(0);
        tarjetaPuntosEjemplo.setCodigo("1234");
        tarjetaPuntosEjemplo.setCliente(clienteEjemplo);
        tarjetaPuntosEjemplo.setId(1L);


    }

    //Convertir a DTO
    @Test
    @DisplayName("Debe convertir a DTO")
    void tarjetaConvertirADTOTest() {
        //Actuar
        TarjetaPuntosDTO respuesta = tarjetaPuntosService.convertirADTO(tarjetaPuntosEjemplo);

        //Acertar
        assertNotNull(respuesta);
        assertEquals("1234", respuesta.getCodigo());
        assertEquals(0, respuesta.getPuntos());
        assertEquals("Juan Perez", respuesta.getNombreCliente());
        assertTrue(respuesta.getActivo());
    }

    //Consulta por id
    @Test
    @DisplayName("Consulta exitosa por id")
    void consultaIdValidoTest() {
        //Arrancar
        when(tarjetaPuntosRepository.findById(1L)).thenReturn(Optional.of(tarjetaPuntosEjemplo));

        //Actuar
        TarjetaPuntosDTO respuesta = tarjetaPuntosService.consultaTarjetaPuntosId(1L);

        //Acertar
        assertNotNull(respuesta);
        assertEquals("1234", respuesta.getCodigo());

        verify(tarjetaPuntosRepository, times(1)).findById(1L);
    }

    //Crear tarjeta de puntos
    @Test
    @DisplayName("Crear la tarjeta de puntos")
    void crearTarjetaPuntosTest() {
        //Arrancar
        TarjetaPuntos tarjetaPuntosEjemploNuevo = new TarjetaPuntos();

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(clienteEjemplo));
        when(tarjetaPuntosRepository.save(any(TarjetaPuntos.class))).thenReturn(tarjetaPuntosEjemplo);

        //Actuar
        TarjetaPuntosDTO respuesta = tarjetaPuntosService.crearTarjetaPuntos(1L);

        //Acertar
        assertNotNull(respuesta);
        assertEquals("Juan Perez", respuesta.getNombreCliente());
        assertTrue(respuesta.getActivo());

        verify(tarjetaPuntosRepository, times(1)).save(any(TarjetaPuntos.class));
        verify(clienteRepository, times(1)).findById(1L);
    }

    //Actualizar tarjeta puntos activo
    @Test
    @DisplayName("La tajeta de puntos se actualiza correctamente el activo")
    void actualizarActivoExitoTest() {
        //Arrancar
        tarjetaPuntosEjemplo.setActivo(false);

        clienteEjemplo.setTarjetaPuntos(tarjetaPuntosEjemplo);

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(clienteEjemplo));
        when(tarjetaPuntosRepository.save(any(TarjetaPuntos.class))).thenReturn(tarjetaPuntosEjemplo);

        //Actuar
        TarjetaPuntosDTO respuesta = tarjetaPuntosService.actualizarTarjetaPuntosActivo(1L);

        //Acertar
        assertNotNull(respuesta);
        assertTrue(respuesta.getActivo());

        respuesta = tarjetaPuntosService.actualizarTarjetaPuntosActivo(1L);

        assertFalse(respuesta.getActivo());

        verify(clienteRepository, times(2)).findById(1L);
        verify(tarjetaPuntosRepository, times(2)).save(any(TarjetaPuntos.class));
    }

    @Test
    @DisplayName("El cliente no existe")
    void actualizarFallaIdClienteTest() {
        //Arrancar
        Long idCliente = 99L;
        when(clienteRepository.findById(idCliente)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> tarjetaPuntosService.actualizarTarjetaPuntosActivo(idCliente));
    }
    @Test
    @DisplayName("El cliente no tiene una tarjeta de puntos")
    void actualiarSinTarjetaFallaTest() {
        //Arrancar
        clienteEjemplo.setTarjetaPuntos(null);

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(clienteEjemplo));

        //Acertar
        assertThrows(ResourceNotFoundException.class, () -> tarjetaPuntosService.actualizarTarjetaPuntosActivo(1L));
    }

    //Actualizar tarjeta puntos
    @Test
    @DisplayName("Actualiza los puntos correctamente")
    void actualizaPuntosExitoTest(){
        //Arranque
        clienteEjemplo.setTarjetaPuntos(tarjetaPuntosEjemplo);
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(clienteEjemplo));

        //Actuar
        TarjetaPuntosDTO respuesta = tarjetaPuntosService.actualizarTarjetaPuntosPuntos(1L, 10);

        //Acertar
        assertNotNull(respuesta);
        assertEquals(10, respuesta.getPuntos());

        verify(clienteRepository, times(1)).findById(1L);
        verify(tarjetaPuntosRepository, times(1)).save(any(TarjetaPuntos.class));
    }
    @Test
    @DisplayName("El cliente no tiene una tarjeta de puntos")
    void actualiarPuntosSinTarjetaFallaTest() {
        //Arrancar
        clienteEjemplo.setTarjetaPuntos(null);

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(clienteEjemplo));

        //Acertar
        assertThrows(ResourceNotFoundException.class, () -> tarjetaPuntosService.actualizarTarjetaPuntosPuntos(1L, 10));
    }
    @Test
    @DisplayName("El cliente no existe")
    void actualizarPuntosFallaIdClienteTest() {
        //Arrancar
        Long idCliente = 99L;
        when(clienteRepository.findById(idCliente)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> tarjetaPuntosService.actualizarTarjetaPuntosPuntos(idCliente, 10));
    }

}
