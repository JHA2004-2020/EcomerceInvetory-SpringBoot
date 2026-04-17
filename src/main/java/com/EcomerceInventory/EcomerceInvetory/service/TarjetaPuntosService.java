package com.EcomerceInventory.EcomerceInvetory.service;

import com.EcomerceInventory.EcomerceInvetory.dto.TarjetaPuntosDTO;
import com.EcomerceInventory.EcomerceInvetory.exception.ResourceNotFoundException;
import com.EcomerceInventory.EcomerceInvetory.model.Cliente;
import com.EcomerceInventory.EcomerceInvetory.model.TarjetaPuntos;
import com.EcomerceInventory.EcomerceInvetory.repository.ClienteRepository;
import com.EcomerceInventory.EcomerceInvetory.repository.TarjetaPuntosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TarjetaPuntosService {
    @Autowired
    TarjetaPuntosRepository tarjetaPuntosRepository;
    @Autowired
    ClienteRepository clienteRepository;

    //Convertir a DTO
    public TarjetaPuntosDTO convertirADTO(TarjetaPuntos tp){
        TarjetaPuntosDTO dto = new TarjetaPuntosDTO();

        dto.setCodigo(tp.getCodigo());
        dto.setPuntos(tp.getPuntos());
        dto.setNombreCliente(tp.getCliente().getNombre());
        dto.setActivo(tp.getActivo());

        return dto;
    }

    //Sentencias GET

    //Consulra por id
    public TarjetaPuntosDTO consultaTarjetaPuntosId(Long id){
        TarjetaPuntos tp = tarjetaPuntosRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Esta tarjeta no existe"));
        return convertirADTO(tp);
    }

    //Sentencias POST

    //Cear tarjeta de puntos
    public TarjetaPuntosDTO crearTarjetaPuntos(Long clienteId){
        Cliente cliente = clienteRepository.findById(clienteId).orElseThrow(() -> new ResourceNotFoundException("Este cliente no existe"));
        TarjetaPuntos tp = new TarjetaPuntos();
        String codigo = cliente.getNombre().substring(0,3) + cliente.getIdentificacion();
        tp.setActivo(true);
        tp.setCliente(cliente);
        tp.setCodigo(codigo);
        tp.setPuntos(0);

        tarjetaPuntosRepository.save(tp);

        return convertirADTO(tp);
    }

    //Sentencias PUT

    //Actualizar tarjeta de puntos estado
    public TarjetaPuntosDTO actualizarTarjetaPuntosActivo(Long clienteId){
        Cliente cliente = clienteRepository.findById(clienteId).orElseThrow(() -> new ResourceNotFoundException("Este cliente no existe"));
        if(cliente.getTarjetaPuntos() == null){
            throw new ResourceNotFoundException("El cliente no tiene una tarjeta de puntos");
        }

        TarjetaPuntos tarjetaPuntos = cliente.getTarjetaPuntos();

        if (tarjetaPuntos.getActivo() == false){
            tarjetaPuntos.setActivo(true);
        } else {
            tarjetaPuntos.setActivo(false);
        }
        tarjetaPuntosRepository.save(tarjetaPuntos);

        return convertirADTO(cliente.getTarjetaPuntos());
    }

    //Actualizar tarjeta de puntos puntos
    public TarjetaPuntosDTO actualizarTarjetaPuntosPuntos(Long clienteId, Integer puntos){
        Cliente cliente = clienteRepository.findById(clienteId).orElseThrow(() -> new ResourceNotFoundException("Este cliente no existe"));
        if(cliente.getTarjetaPuntos() == null){
            throw new ResourceNotFoundException("El cliente no tiene una tarjeta de puntos");
        }

        TarjetaPuntos tarjetaPuntos = cliente.getTarjetaPuntos();

        tarjetaPuntos.setPuntos(tarjetaPuntos.getPuntos() + puntos);

        tarjetaPuntosRepository.save(tarjetaPuntos);

        return convertirADTO(tarjetaPuntos);
    }



}
