package com.EcomerceInventory.EcomerceInvetory;

import com.EcomerceInventory.EcomerceInvetory.dto.ClienteDTO;
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
import static org.mockito.Mockito.*; // Para when, times, any, etc.
import static org.junit.jupiter.api.Assertions.*; // Para assertThrows, assertEquals, etc.

import java.util.Optional; // ¡Vital para el findById!
// Importa también tus clases: Cliente, ClienteDTO, ClienteRepository, ResourceNotFoundException

@ExtendWith(MockitoExtension.class) // Inicializa los mocks de Mockito
class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository; // Simulamos la base de datos

    @Mock
    private TarjetaPuntosRepository tarjetaPuntosRepository;

    @InjectMocks
    private ClienteService clienteService; // Inyecta el mock del repo en el servicio real

    @InjectMocks
    private TarjetaPuntosService tarjetaPuntosService;


    private Cliente clienteEjemplo;
    private ClienteDTO clienteDTOEjemplo;
    private TarjetaPuntos tarjetaPuntosEjemplo;

    @BeforeEach
    void setUp() {
        // Configuramos datos de prueba que usaremos en varios métodos
        clienteEjemplo = new Cliente();
        clienteEjemplo.setId(1L);
        clienteEjemplo.setActivo(true);
        clienteEjemplo.setNombre("Juan Perez");
        clienteEjemplo.setIdentificacion("12345");
        clienteEjemplo.setEdad(25);
        clienteEjemplo.setCorreo("juan@mail.com");
        clienteEjemplo.setPasword("segura123");


        clienteDTOEjemplo = new ClienteDTO();
        clienteDTOEjemplo.setActivo(true);
        clienteDTOEjemplo.setNombre("Juan Perez");
        clienteDTOEjemplo.setIdentificacion("12345");
        clienteDTOEjemplo.setEdad(25);
        clienteDTOEjemplo.setCorreo("juan@mail.com");
        clienteDTOEjemplo.setPasword("segura123");

//        tarjetaPuntosEjemplo = new TarjetaPuntos();
//        tarjetaPuntosEjemplo.setActivo(true);
//        tarjetaPuntosEjemplo.setId(1L);
//        tarjetaPuntosEjemplo.setPuntos(20);
//        tarjetaPuntosEjemplo.setCliente(clienteEjemplo);
//        tarjetaPuntosEjemplo.setCodigo("1L");



    }

    // Aquí irán los tests...

    //ClienteService

    //ConvertirADTO
    @Test
    @DisplayName("Deberia covertir un Cliente a ClienteDTO sin una tarjeta")
    void convertirADTOSinTarjetaExitoTest(){
        //Arranque
        ClienteDTO conversion = clienteService.convertirADTO(clienteEjemplo);

        assertNotNull(conversion, "El dto no deberia ser null");
        assertNull(clienteEjemplo.getTarjetaPuntos(), "El cliente tiene una tarjeta");
        assertEquals(clienteEjemplo.getNombre(), conversion.getNombre(), "El nombre no es igual");
        assertEquals(clienteEjemplo.getIdentificacion(), conversion.getIdentificacion(), "La identificacion no es igual");
        assertEquals(clienteEjemplo.getEdad(), conversion.getEdad(), "La edad no es igual");
        assertEquals(clienteEjemplo.getCorreo(), conversion.getCorreo(), "El correo no es igual");
        assertEquals(clienteEjemplo.getPasword(), conversion.getPasword(), "La contrasena no es igual");
    }
    @Test
    @DisplayName("Debería convertir un Cliente a ClienteDTO incluyendo su Tarjeta de Puntos")
    void convertirADTOConTarjetaTest() {
        // 1. ARRANGE (Preparar el escenario)
        TarjetaPuntos tarjeta = new TarjetaPuntos();
        tarjeta.setId(1L);
        tarjeta.setPuntos(100);
        tarjeta.setCodigo("GOLD-2026");
        tarjeta.setActivo(true);


        // Conectamos la tarjeta al cliente manualmente
        clienteEjemplo.setTarjetaPuntos(tarjeta);

        // 2. ACT (La acción)
        ClienteDTO resultado = clienteService.convertirADTO(clienteEjemplo);

        // 3. ASSERT (La verificación)
        assertNotNull(resultado, "El DTO no debería ser nulo");

        // Verificamos el objeto anidado
        assertNotNull(resultado.getTarjetaPuntos(), "El DTO debería contener la tarjeta de puntos");
        assertEquals(100, resultado.getTarjetaPuntos(), "Los puntos deben coincidir");
        assertEquals("GOLD-2026", resultado.getTarjetaCodigo(), "El código de tarjeta debe coincidir");
        assertEquals(true, resultado.getActivo());

        // Verificamos que el dueño sea el correcto
        assertEquals(clienteEjemplo.getNombre(), resultado.getNombre(), "El nombre del cliente debe ser igual");
    }

    //Verifica que de error el buscar un Id que no existe
    @Test
    @DisplayName("Debería lanzar ResourceNotFoundException cuando el ID no existe")
    void listarClienteIdFallaTest() {
        // Arrange
        Long idInexistente = 99L;
        when(clienteRepository.findById(idInexistente)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            clienteService.listarClienteId(idInexistente);
        });
    }


    @Test
    @DisplayName("No debe cambiar la edad si el DTO trae un menor de edad")
    void actualizarClienteEdadFallaTest() {
        //Arrancar
        ClienteDTO clienteInvalido = new ClienteDTO();

        clienteInvalido.setEdad(16);

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(clienteEjemplo));

        //Actuar
        ClienteDTO respuesta = clienteService.actualizarCliente(1L, clienteInvalido);

        //Acierta

        assertEquals(25, respuesta.getEdad(), "La edad no debio cambiar porque 15 es menor que 18");
        //Se guardo de todos modos? si pues debido a que se guardan los datos validos
        verify(clienteRepository).save(any(Cliente.class));

    }



}