package com.EcomerceInventory.EcomerceInvetory.service;

import com.EcomerceInventory.EcomerceInvetory.dto.ClienteDTO;
import com.EcomerceInventory.EcomerceInvetory.exception.ResourceNotFoundException;
import com.EcomerceInventory.EcomerceInvetory.model.Cliente;
import com.EcomerceInventory.EcomerceInvetory.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;


//Check
@Service
public class ClienteService {
    @Autowired
    private ClienteRepository clienteRepository;
    //Convertir a dto
    public ClienteDTO convertirADTO(Cliente cliente){
        ClienteDTO dto = new ClienteDTO();

        dto.setNombre(cliente.getNombre());
        dto.setIdentificacion(cliente.getIdentificacion());
        dto.setEdad(cliente.getEdad());
        dto.setCorreo(cliente.getCorreo());
        dto.setPasword(cliente.getPasword());
        dto.setActivo(cliente.getActivo());
        if(cliente.getTarjetaPuntos() != null){
            dto.setTarjetaActivo(cliente.getTarjetaPuntos().getActivo());
            dto.setTarjetaPuntos(cliente.getTarjetaPuntos().getPuntos());
            dto.setTarjetaCodigo(cliente.getTarjetaPuntos().getCodigo());
        }

        return dto;
    }
    //Sentencias GET


    //ListarPaginacion
    public Page<ClienteDTO> listarPaginaCliente(int numeroPagina, int size){
        Pageable paginacion = PageRequest.of(numeroPagina, size, Sort.by("nombre").ascending());
        Page<Cliente> clientes = clienteRepository.findAll(paginacion);
        return clientes.map(this::convertirADTO);
    }

    //ListarClienteId
    public ClienteDTO listarClienteId(Long id){
        Cliente cliente = clienteRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("El cliente con ese id no existe"));
        return convertirADTO(cliente);
    }

    //ListarClienteNombre
    public List<ClienteDTO> listarClienteNombre(String nombre){
        List<Cliente> cliente = clienteRepository.findByNombreStartingWith(nombre);
        return cliente.stream().map(this::convertirADTO).toList();
    }

    //ListarClienteNombreActivo
    public List<ClienteDTO> listarClienteNombreActivo(String nombre, Boolean estado){
        List<Cliente> cliente = clienteRepository.findClienteByActivoAndNombreStartingWith(estado, nombre);
        return cliente.stream().map(this::convertirADTO).toList();
    }

    //Consultas POST

    //Crear cliente
    public ClienteDTO crearCliente(ClienteDTO dto){
        Cliente cliente = new Cliente();

        cliente.setNombre(dto.getNombre());
        cliente.setIdentificacion(dto.getIdentificacion());
        cliente.setEdad(dto.getEdad());
        cliente.setCorreo(dto.getCorreo());
        cliente.setPasword(dto.getPasword());
        cliente.setActivo(true);

        clienteRepository.save(cliente);

        return convertirADTO(cliente);
    }

    //Consultas PUT

    //Actualizar cliente
    public ClienteDTO actualizarCliente(Long id, ClienteDTO dto){
        Cliente cliente = clienteRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado"));

        if (dto.getNombre() != null && !dto.getNombre().isBlank()) {
            cliente.setNombre(dto.getNombre());
        }
        if(dto.getIdentificacion() != null && !dto.getIdentificacion().isBlank()){
            cliente.setIdentificacion(dto.getIdentificacion());
        }
        if(dto.getCorreo() != null && !dto.getCorreo().isBlank()){
            cliente.setCorreo(dto.getCorreo());
        }
        if(dto.getPasword() != null && !dto.getPasword().isBlank()){
            cliente.setPasword(dto.getPasword());
        }
        if(dto.getEdad() != null && dto.getEdad() > 18){
            cliente.setEdad(dto.getEdad());
        }
        if(dto.getActivo() != null){
            cliente.setActivo(dto.getActivo());
        }

        clienteRepository.save(cliente);

        return convertirADTO(cliente);
    }

}
