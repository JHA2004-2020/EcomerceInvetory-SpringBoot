package com.EcomerceInventory.EcomerceInvetory;

import com.EcomerceInventory.EcomerceInvetory.dto.ClienteDTO;
import com.EcomerceInventory.EcomerceInvetory.exception.ResourceNotFoundException;
import com.EcomerceInventory.EcomerceInvetory.model.Cliente;
import com.EcomerceInventory.EcomerceInvetory.model.TarjetaPuntos;
import com.EcomerceInventory.EcomerceInvetory.repository.ClienteRepository;
import com.EcomerceInventory.EcomerceInvetory.service.ClienteService;
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
class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository; // Simulamos la base de datos
    @InjectMocks
    private ClienteService clienteService; // Inyecta el mock del repo en el servicio real



    private Cliente clienteEjemplo;
    private ClienteDTO clienteDTOEjemplo;

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

    }

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

    //Paginacion de clientes
    @Test
    @DisplayName("Debería retornar una página de clientes correctamente")
    void listarPaginaClienteTest() {
        // 1. ARRANGE
        int pagina = 0;
        int tamano = 10;

        // Creamos una lista con nuestro cliente de ejemplo
        List<Cliente> listaClientes = List.of(clienteEjemplo);

        // Creamos el objeto Page que el repositorio debería devolver
        // PageImpl necesita: la lista, el objeto pageable y el total de elementos
        Page<Cliente> paginaSimulada = new PageImpl<>(
                listaClientes,
                PageRequest.of(pagina, tamano),
                listaClientes.size()
        );

        // EL GUION: Cuando se llame a findAll con CUALQUIER Pageable, devuelve nuestra página
        when(clienteRepository.findAll(any(Pageable.class))).thenReturn(paginaSimulada);

        // 2. ACT
        Page<ClienteDTO> resultado = clienteService.listarPaginaCliente(pagina, tamano);

        // 3. ASSERT
        assertNotNull(resultado);
        assertEquals(1, resultado.getTotalElements(), "Debería haber 1 elemento en la página");
        assertEquals("Juan Perez", resultado.getContent().getFirst().getNombre());

        // Verificamos que se llamó al repositorio con la configuración correcta
        verify(clienteRepository).findAll(any(Pageable.class));
    }

    //Busqueda por id
    @Test
    @DisplayName("Busqueda exitosa de cliente por id")
    void clienteIdExistenteTest(){
        //Arranque
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(clienteEjemplo));
        ClienteDTO respuesta = clienteService.listarClienteId(1L);
        //Actuar, Acertar
        assertNotNull(respuesta, "El cliente no fue traido");
        assertEquals(respuesta.getIdentificacion(), clienteEjemplo.getIdentificacion(), "El cliente traido no es el mismo");
    }

    @Test
    @DisplayName("Busqueda falla de clente por id")
    void clienteIdInexistenteTest(){
        Long IdInvalido = 376L;
        when(clienteRepository.findById(IdInvalido)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            clienteService.listarClienteId(IdInvalido);
        });
    }

    //Busqueda por Nombre
    @Test
    @DisplayName("Busqueda exitosa clientes por nombre")
    void clienteExitoNombreTest(){
        //Arrancar
        String nombre = "Juan";

        when(clienteRepository.findByNombreStartingWith(nombre)).thenReturn(List.of(clienteEjemplo));

        //Actuar
        List<ClienteDTO> resultado = clienteService.listarClienteNombre(nombre);

        //Acertar
        assertNotNull(resultado, "La lista no debe ser nula");
        assertEquals(1, resultado.size(), "Deberia haber solo un cliente");
        assertEquals("Juan Perez", resultado.getFirst().getNombre(), "El nombre no coincide con el mapeo original");

        verify(clienteRepository).findByNombreStartingWith(nombre);
    }

    //Busqueda por nombre y activo
    @Test
    @DisplayName("Busqueda de clientes por nombre y activo")
    void clienteNombreActivoTest() {
        //Arrancar
        String nombre = "Juan";
        Boolean activo = true;

        when(clienteRepository.findClienteByActivoAndNombreStartingWith(activo, nombre)).thenReturn(List.of(clienteEjemplo));

        //Actuar
        List<ClienteDTO> respuesta = clienteService.listarClienteNombreActivo(nombre,activo);

        //Verificar
        assertNotNull(respuesta, "La lista no puede ser nula");
        assertEquals(1, respuesta.size());
        assertEquals("Juan Perez", respuesta.getFirst().getNombre());
        assertEquals(true, respuesta.getFirst().getActivo());

        verify(clienteRepository).findClienteByActivoAndNombreStartingWith(activo,nombre);
    }

    //Crear cliente
    @Test
    @DisplayName("Crear cliente")
    void crearClienteDatosValidosTest(){
        //Arrancar
        //Guardado
        ClienteDTO clienteNuevoDTO = new ClienteDTO();
        clienteNuevoDTO.setActivo(true);
        clienteNuevoDTO.setNombre("Jero");
        clienteNuevoDTO.setIdentificacion("123456");
        clienteNuevoDTO.setEdad(25);
        clienteNuevoDTO.setCorreo("jero@mail.com");
        clienteNuevoDTO.setPasword("segura123");

        //Devuelto
        Cliente clienteNuevo = new Cliente();
        clienteNuevo.setId(100L);
        clienteNuevo.setActivo(true);
        clienteNuevo.setNombre("Jero");
        clienteNuevo.setIdentificacion("123456");
        clienteNuevo.setEdad(25);
        clienteNuevo.setCorreo("jero@mail.com");
        clienteNuevo.setPasword("segura123");



        when(clienteRepository.save(any(Cliente.class))).thenReturn(clienteNuevo);

        //Actuar
        ClienteDTO respuesta = clienteService.crearCliente(clienteNuevoDTO);

        //Acertar
        assertNotNull(respuesta, "El guardado no puede ser nulo");
        assertEquals("123456", respuesta.getIdentificacion());
        assertEquals("Jero", respuesta.getNombre());

        verify(clienteRepository, times(1)).save(any(Cliente.class));

    }

    //Actaulizar Cliente
    //Nombre
    @Test
    @DisplayName("No debe cambiar el nombre cuando es nullo")
    void actualizarClienteNombreNuloFallaTest(){
        //Arrancar
        String nombreOriginal = clienteEjemplo.getNombre();
        clienteDTOEjemplo.setNombre(null);

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(clienteEjemplo));

        //Actuar
        ClienteDTO respuesta = clienteService.actualizarCliente(1L, clienteDTOEjemplo);

        //Acertar
        assertEquals(nombreOriginal, respuesta.getNombre());

        verify(clienteRepository).save(any(Cliente.class));
    }
    @Test
    @DisplayName("No debe cambiar el nombre cuando esta vacio")
    void actualizarClienteNombreVacioFallaTest(){
        //Arrancar
        String nombreOriginal = clienteEjemplo.getNombre();
        clienteDTOEjemplo.setNombre("");

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(clienteEjemplo));

        //Actuar
        ClienteDTO respuesta = clienteService.actualizarCliente(1L, clienteDTOEjemplo);

        //Acertar
        assertEquals(nombreOriginal, respuesta.getNombre());

        verify(clienteRepository).save(any(Cliente.class));
    }
    @Test
    @DisplayName("No debe cambiar el nombre cuando tiene espacios en blanco")
    void actualizarClienteNombreEspacioFallaTest(){
        //Arrancar
        String nombreOriginal = clienteEjemplo.getNombre();
        clienteDTOEjemplo.setNombre("       ");

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(clienteEjemplo));

        //Actuar
        ClienteDTO respuesta = clienteService.actualizarCliente(1L, clienteDTOEjemplo);

        //Acertar
        assertEquals(nombreOriginal, respuesta.getNombre());

        verify(clienteRepository).save(any(Cliente.class));
    }
    @Test
    @DisplayName("El nombre cambia cuando es valido")
    void actualizarClienteNombreExitoTest(){
        //Arrancar
        clienteDTOEjemplo.setNombre("Jeronimo");

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(clienteEjemplo));

        //Actuar
        ClienteDTO respuesta = clienteService.actualizarCliente(1L, clienteDTOEjemplo);

        //Acertar
        assertEquals("Jeronimo", respuesta.getNombre(), "El nombre no se actualizo");

        verify(clienteRepository).save(any(Cliente.class));
    }

    //Identificiacion
    @Test
    @DisplayName("No debe cambiar la identificacion cuando es nullo")
    void actualizarClienteIdentificacionNulaFallaTest(){
        //Arrancar
        String identificacionOriginal = clienteEjemplo.getIdentificacion();
        clienteDTOEjemplo.setIdentificacion(null);

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(clienteEjemplo));

        //Actuar
        ClienteDTO respuesta = clienteService.actualizarCliente(1L, clienteDTOEjemplo);

        //Acertar
        assertEquals(identificacionOriginal, respuesta.getIdentificacion());

        verify(clienteRepository).save(any(Cliente.class));
    }
    @Test
    @DisplayName("No debe cambiar la identifiacion cuando esta vacio")
    void actualizarClienteIdentificacionVacioFallaTest(){
        //Arrancar
        String identificacionOriginal = clienteEjemplo.getIdentificacion();
        clienteDTOEjemplo.setIdentificacion("");

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(clienteEjemplo));

        //Actuar
        ClienteDTO respuesta = clienteService.actualizarCliente(1L, clienteDTOEjemplo);

        //Acertar
        assertEquals(identificacionOriginal, respuesta.getIdentificacion());

        verify(clienteRepository).save(any(Cliente.class));
    }
    @Test
    @DisplayName("No debe cambiar la identificacion cuando tiene espacios en blanco")
    void actualizarClienteIdentificacionEspacioFallaTest(){
        //Arrancar
        String identificacionOriginal = clienteEjemplo.getIdentificacion();
        clienteDTOEjemplo.setIdentificacion("       ");

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(clienteEjemplo));

        //Actuar
        ClienteDTO respuesta = clienteService.actualizarCliente(1L, clienteDTOEjemplo);

        //Acertar
        assertEquals(identificacionOriginal, respuesta.getIdentificacion());

        verify(clienteRepository).save(any(Cliente.class));
    }
    @Test
    @DisplayName("La identificacion cambia cuando es valido")
    void actualizarClienteIdentificacionExitoTest(){
        //Arrancar
        clienteDTOEjemplo.setIdentificacion("4321");

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(clienteEjemplo));

        //Actuar
        ClienteDTO respuesta = clienteService.actualizarCliente(1L, clienteDTOEjemplo);

        //Acertar
        assertEquals("4321", respuesta.getIdentificacion(), "La identificacion no se actualizo");

        verify(clienteRepository).save(any(Cliente.class));
    }

    //Correo
    @Test
    @DisplayName("No debe cambiar el Correo cuando es nullo")
    void actualizarClienteCorreoNulaFallaTest(){
        //Arrancar
        String correoOriginal = clienteEjemplo.getCorreo();
        clienteDTOEjemplo.setCorreo(null);

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(clienteEjemplo));

        //Actuar
        ClienteDTO respuesta = clienteService.actualizarCliente(1L, clienteDTOEjemplo);

        //Acertar
        assertEquals(correoOriginal, respuesta.getCorreo());

        verify(clienteRepository).save(any(Cliente.class));
    }
    @Test
    @DisplayName("No debe cambiar el correo cuando esta vacio")
    void actualizarClienteCorreoVacioFallaTest(){
        //Arrancar
        String correoOriginal = clienteEjemplo.getCorreo();
        clienteDTOEjemplo.setCorreo("");

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(clienteEjemplo));

        //Actuar
        ClienteDTO respuesta = clienteService.actualizarCliente(1L, clienteDTOEjemplo);

        //Acertar
        assertEquals(correoOriginal, respuesta.getCorreo());

        verify(clienteRepository).save(any(Cliente.class));
    }
    @Test
    @DisplayName("No debe cambiar el Correo cuando tiene espacios en blanco")
    void actualizarClienteCorreoEspacioFallaTest(){
        //Arrancar
        String correoOriginal = clienteEjemplo.getCorreo();
        clienteDTOEjemplo.setCorreo("       ");

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(clienteEjemplo));

        //Actuar
        ClienteDTO respuesta = clienteService.actualizarCliente(1L, clienteDTOEjemplo);

        //Acertar
        assertEquals(correoOriginal, respuesta.getCorreo());

        verify(clienteRepository).save(any(Cliente.class));
    }
    @Test
    @DisplayName("El Correo cambia cuando es valido")
    void actualizarClienteCorreoExitoTest(){
        //Arrancar
        clienteDTOEjemplo.setCorreo("4321");

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(clienteEjemplo));

        //Actuar
        ClienteDTO respuesta = clienteService.actualizarCliente(1L, clienteDTOEjemplo);

        //Acertar
        assertEquals("4321", respuesta.getCorreo(), "La Correo no se actualizo");

        verify(clienteRepository).save(any(Cliente.class));
    }

    //Pasword
    @Test
    @DisplayName("No debe cambiar la pasword cuando es nullo")
    void actualizarClientePaswordNulaFallaTest(){
        //Arrancar
        String paswordOriginal = clienteEjemplo.getPasword();
        clienteDTOEjemplo.setPasword(null);

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(clienteEjemplo));

        //Actuar
        ClienteDTO respuesta = clienteService.actualizarCliente(1L, clienteDTOEjemplo);

        //Acertar
        assertEquals(paswordOriginal, respuesta.getPasword());

        verify(clienteRepository).save(any(Cliente.class));
    }
    @Test
    @DisplayName("No debe cambiar la pasword cuando esta vacio")
    void actualizarClientePaswordVacioFallaTest(){
        //Arrancar
        String paswordOriginal = clienteEjemplo.getPasword();
        clienteDTOEjemplo.setPasword("");

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(clienteEjemplo));

        //Actuar
        ClienteDTO respuesta = clienteService.actualizarCliente(1L, clienteDTOEjemplo);

        //Acertar
        assertEquals(paswordOriginal, respuesta.getPasword());

        verify(clienteRepository).save(any(Cliente.class));
    }
    @Test
    @DisplayName("No debe cambiar la pasword cuando tiene espacios en blanco")
    void actualizarClientePaswordEspacioFallaTest(){
        //Arrancar
        String paswordOriginal = clienteEjemplo.getPasword();
        clienteDTOEjemplo.setPasword("       ");

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(clienteEjemplo));

        //Actuar
        ClienteDTO respuesta = clienteService.actualizarCliente(1L, clienteDTOEjemplo);

        //Acertar
        assertEquals(paswordOriginal, respuesta.getPasword());

        verify(clienteRepository).save(any(Cliente.class));
    }
    @Test
    @DisplayName("La pasword cambia cuando es valido")
    void actualizarClientePaswordExitoTest(){
        //Arrancar
        clienteDTOEjemplo.setPasword("4321");

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(clienteEjemplo));

        //Actuar
        ClienteDTO respuesta = clienteService.actualizarCliente(1L, clienteDTOEjemplo);

        //Acertar
        assertEquals("4321", respuesta.getPasword(), "La pasword no se actualizo");

        verify(clienteRepository).save(any(Cliente.class));
    }


    //Edad
    @Test
    @DisplayName("No debe cambiar la edad cuando es nula")
    void actualizarClienteEdadNuloFallaTest() {
        //Actuar
        Integer edadOriginal = clienteEjemplo.getEdad();
        clienteDTOEjemplo.setEdad(null);

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(clienteEjemplo));

        //Actuar
        ClienteDTO respuesta = clienteService.actualizarCliente(1L, clienteDTOEjemplo);

        //Acertar
        assertEquals(edadOriginal, respuesta.getEdad());

        verify(clienteRepository).save(any(Cliente.class));
    }
    @Test
    @DisplayName("No debe cambiar la edad cuando es menor de edad")
    void actualizarClienteEdadMenorFallaTest() {
        //Arrancar
        Integer edadOriginal = clienteEjemplo.getEdad();
        clienteDTOEjemplo.setEdad(16);

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(clienteEjemplo));

        //Actuar
        ClienteDTO respuesta = clienteService.actualizarCliente(1L, clienteDTOEjemplo);

        //Acertar
        assertEquals(edadOriginal, respuesta.getEdad());

        verify(clienteRepository, times(1)).save(any(Cliente.class));
    }

    //Activo
    @Test
    @DisplayName("No debe cambiar el activo cuando es null")
    void actualizarClienteActivoFallaTest() {
        //Arrancar
        Boolean activoOriginal = clienteEjemplo.getActivo();
        clienteDTOEjemplo.setActivo(null);

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(clienteEjemplo));

        //Actuar
        ClienteDTO respuesta = clienteService.actualizarCliente(1L, clienteDTOEjemplo);

        //Acertar
        assertTrue(respuesta.getActivo());

        verify(clienteRepository, times(1)).save(any(Cliente.class));
    }
    @Test
    @DisplayName("Debe cambiar el estado de true a false")
    void actualizarClienteActivoExitoTest() {
        //Arrancar
        clienteDTOEjemplo.setActivo(false);

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(clienteEjemplo));

        //Actuar
        ClienteDTO respuesta = clienteService.actualizarCliente(1L, clienteDTOEjemplo);

        //Acertar
        assertFalse(respuesta.getActivo());

        verify(clienteRepository, times(1)).save(any(Cliente.class));
    }













}